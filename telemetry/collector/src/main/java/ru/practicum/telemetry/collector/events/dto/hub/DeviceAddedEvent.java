package ru.practicum.telemetry.collector.events.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.telemetry.collector.events.dto.hub.device.DeviceType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {

    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return DeviceAddedEventAvro
                .newBuilder()
                .setId(super.getId())
                .setType(DeviceTypeAvro.valueOf(deviceType.toString()))
                .build();
    }
}