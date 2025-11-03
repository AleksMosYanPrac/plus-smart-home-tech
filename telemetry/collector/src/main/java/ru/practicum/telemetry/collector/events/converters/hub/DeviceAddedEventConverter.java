package ru.practicum.telemetry.collector.events.converters.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.interfaces.HubEventConverter;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventConverter implements HubEventConverter {
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public SpecificRecordBase convert(HubEventProto event) {
        DeviceAddedEventProto hubEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(hubEvent.getId())
                .setType(DeviceTypeAvro.valueOf(hubEvent.getType().toString()))
                .build();
    }
}