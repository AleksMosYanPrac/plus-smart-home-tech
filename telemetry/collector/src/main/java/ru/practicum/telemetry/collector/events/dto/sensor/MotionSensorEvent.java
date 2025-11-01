package ru.practicum.telemetry.collector.events.dto.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    private int linkQuality;
    private boolean motion;
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return MotionSensorAvro
                .newBuilder()
                .setLinkQuality(linkQuality)
                .setMotion(motion)
                .setVoltage(voltage)
                .build();
    }
}