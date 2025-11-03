package ru.practicum.telemetry.collector.events.dto.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {

    private boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return SwitchSensorAvro
                .newBuilder()
                .setState(state)
                .build();
    }
}