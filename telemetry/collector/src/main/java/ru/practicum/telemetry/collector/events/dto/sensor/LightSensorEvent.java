package ru.practicum.telemetry.collector.events.dto.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {

    private int linkQuality;
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return LightSensorAvro
                .newBuilder()
                .setLinkQuality(linkQuality)
                .setLuminosity(luminosity)
                .build();
    }
}