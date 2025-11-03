package ru.practicum.telemetry.collector.events.converters.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.interfaces.HubEventConverter;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class ScenarioAddedEventConverter implements HubEventConverter {
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public SpecificRecordBase convert(HubEventProto event) {
        ScenarioAddedEventProto hubEvent = event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(hubEvent.getName())
                .setActions(getDeviceActions(hubEvent.getActionList()))
                .setConditions(getConditions(hubEvent.getConditionList()))
                .build();
    }

    private List<ScenarioConditionAvro> getConditions(List<ScenarioConditionProto> conditionList) {
        return conditionList.stream()
                .map(c -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(c.getSensorId())
                        .setOperation(ConditionOperationAvro.valueOf(c.getOperation().toString()))
                        .setType(ConditionTypeAvro.valueOf(c.getType().toString()))
                        .setValue(c.hasIntValue() ? c.getIntValue() : c.getBoolValue())
                        .build())
                .toList();
    }

    private List<DeviceActionAvro> getDeviceActions(List<DeviceActionProto> actionList) {
        return actionList.stream()
                .map(a -> DeviceActionAvro.newBuilder()
                        .setSensorId(a.getSensorId())
                        .setType(ActionTypeAvro.valueOf(a.getType().toString()))
                        .setValue(a.getValue())
                        .build())
                .toList();
    }
}
