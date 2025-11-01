package ru.practicum.telemetry.collector.events.dto.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {

    private int temperatureC;
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return TemperatureSensorAvro
                .newBuilder()
                .setTemperatureC(temperatureC)
                .setTemperatureF(temperatureF)
                .build();
    }
}