package ru.practicum.telemetry.collector.events.interfaces;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventConverter {
    SensorEventProto.PayloadCase getMessageType();

    SpecificRecordBase convert(SensorEventProto event);
}