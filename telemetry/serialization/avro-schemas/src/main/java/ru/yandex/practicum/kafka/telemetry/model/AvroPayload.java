package ru.yandex.practicum.kafka.telemetry.model;

import org.apache.avro.specific.SpecificRecordBase;

public interface AvroPayload {
    SpecificRecordBase toPayload();
}