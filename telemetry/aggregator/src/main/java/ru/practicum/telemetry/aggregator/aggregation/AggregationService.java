package ru.practicum.telemetry.aggregator.aggregation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class AggregationService {

    Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        // Проверяем, есть ли снапшот для event.getHubId(),Если снапшот есть, то достаём его
        // Если нет, то создаём новый
        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(event.getHubId(), hubId ->
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setSensorsState(new HashMap<>())
                        .setTimestamp(event.getTimestamp())
                        .build()
        );

        // Проверяем, есть ли в снапшоте данные для event.getId()
        // Если данные есть, то достаём их в переменную oldState
        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (Objects.nonNull(oldState)) {
            // Проверка, если oldState.getTimestamp() произошёл позже, чем event.getTimestamp() или
            // oldState.getData() равен event.getPayload(), то ничего обновлять не нужно,
            // выходим из метода вернув Optional.empty()
            if (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }

        // пришли новые данные и снапшот нужно обновить
        //Создаём экземпляр SensorStateAvro на основе данных события
        SensorStateAvro newSensorState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
        // Добавляем полученный экземпляр в снапшот
        snapshot.getSensorsState().put(event.getId(), newSensorState);
        // Обновляем таймстемп снапшота таймстемпом из события
        snapshot.setTimestamp(event.getTimestamp());
        return Optional.of(snapshot);
    }
}