package ru.practicum.telemetry.collector.events.interfaces;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventConverter {
    HubEventProto.PayloadCase getMessageType();

    SpecificRecordBase convert(HubEventProto event);
}