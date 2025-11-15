package ru.practicum.telemetry.analyzer.service;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.telemetry.analyzer.service.model.*;
import ru.practicum.telemetry.analyzer.service.repositories.*;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerService {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    public Optional<List<DeviceActionRequest>> analyze(SensorsSnapshotAvro value) {
        List<DeviceActionRequest> scenarioList = scenarioRepository.findByHubId(value.getHubId()).stream()
                .filter(scenario -> scenario.checkConditions(value.getSensorsState()))
                .flatMap(this::mapToDeviceActionRequest)
                .toList();
        return scenarioList.isEmpty() ? Optional.empty() : Optional.of(scenarioList);
    }

    public void analyze(HubEventAvro value) {
        switch (value.getPayload()) {
            case DeviceAddedEventAvro deviceAdded -> addNewSensor(value, deviceAdded);
            case DeviceRemovedEventAvro deviceRemoved -> deleteSensor(value, deviceRemoved);
            case ScenarioAddedEventAvro scenarioAdded -> addNewScenario(value, scenarioAdded);
            case ScenarioRemovedEventAvro scenarioRemoved -> deleteScenario(value, scenarioRemoved);
            case null, default ->
                    throw new IllegalArgumentException("Unknown type of hub event: " + value.getPayload());
        }
    }

    @Transactional
    private void addNewSensor(HubEventAvro hubEvent, DeviceAddedEventAvro device) {
        sensorRepository.save(new Sensor(device.getId(), hubEvent.getHubId()));
    }

    @Transactional
    private void deleteSensor(HubEventAvro hubEvent, DeviceRemovedEventAvro device) {
        sensorRepository.findByIdAndHubId(device.getId(), hubEvent.getHubId())
                .ifPresent(sensorRepository::delete);
    }

    @Transactional
    private void deleteScenario(HubEventAvro hubEvent, ScenarioRemovedEventAvro scenario) {
        scenarioRepository.findByHubIdAndName(hubEvent.getHubId(), scenario.getName())
                .ifPresent(scenarioRepository::delete);
    }

    @Transactional
    private void addNewScenario(HubEventAvro hubEvent, ScenarioAddedEventAvro scenario) {
        Scenario newScenario = new Scenario();
        newScenario.setName(scenario.getName());
        newScenario.setHubId(hubEvent.getHubId());

        Map<String, Condition> conditionMap = new HashMap<>();
        Map<String, Action> actionMap = new HashMap<>();

        scenario.getConditions().forEach(c -> {

            Sensor sensor = sensorRepository.findByIdAndHubId(c.getSensorId(), hubEvent.getHubId())
                    .orElseGet(() -> {
                        Sensor newSensor = new Sensor(c.getSensorId(), hubEvent.getHubId());
                        return sensorRepository.save(newSensor);
                    });

            Condition condition = new Condition();
            condition.setType(c.getType().toString());
            condition.setOperation(c.getOperation().toString());
            condition.setValue(getCaseValue(c.getValue()));

            Condition newCondition = conditionRepository.save(condition);
            conditionMap.put(sensor.getId(), newCondition);
        });

        scenario.getActions().forEach(a -> {
            Sensor sensor = sensorRepository.findByIdAndHubId(a.getSensorId(), hubEvent.getHubId())
                    .orElseGet(() -> {
                        Sensor newSensor = new Sensor(a.getSensorId(), hubEvent.getHubId());
                        return sensorRepository.save(newSensor);
                    });

            Action action = new Action();
            action.setType(a.getType().toString());
            action.setValue(a.getValue());

            Action newAction = actionRepository.save(action);
            actionMap.put(sensor.getId(), newAction);
        });

        newScenario.setConditions(conditionMap);
        newScenario.setActions(actionMap);
        scenarioRepository.save(newScenario);
    }

    private Integer getCaseValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        } else {
            return null;
        }
    }

    private Stream<DeviceActionRequest> mapToDeviceActionRequest(Scenario scenario) {
        return scenario.getActions().entrySet().stream().map(entry -> {
            Instant now = Instant.now();
            return DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setScenarioName(scenario.getName())
                    .setAction(DeviceActionProto.newBuilder()
                            .setSensorId(entry.getKey())
                            .setType(ActionTypeProto.valueOf(entry.getValue().getType()))
                            .setValue(entry.getValue().getValue())
                            .build())
                    .setTimestamp(Timestamp.newBuilder()
                            .setSeconds(now.getEpochSecond())
                            .setNanos(now.getNano())
                            .build())
                    .build();
        });
    }
}