package ru.practicum.telemetry.collector.events.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return DeviceRemovedEventAvro
                .newBuilder()
                .setId(super.getId())
                .build();
    }
}