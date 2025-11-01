package ru.practicum.telemetry.collector.events.interfaces;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;

public interface EventsMapper {
    SpecificRecordBase toAvro(HubEvent event);

    SpecificRecordBase toAvro(SensorEvent event);
}