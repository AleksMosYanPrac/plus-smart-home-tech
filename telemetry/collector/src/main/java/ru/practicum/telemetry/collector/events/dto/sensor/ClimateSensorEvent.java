package ru.practicum.telemetry.collector.events.dto.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {

    private int temperatureC;
    private int humidity;
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return ClimateSensorAvro
                .newBuilder()
                .setTemperatureC(temperatureC)
                .setHumidity(humidity)
                .setCo2Level(co2Level)
                .build();
    }
}