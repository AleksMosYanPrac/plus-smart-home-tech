package ru.practicum.telemetry.collector.events;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.events.interfaces.EventsMapper;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class EventsMapperImpl implements EventsMapper {

    @Override
    public SpecificRecordBase toAvro(HubEvent event) {
        return HubEventAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setHubId(event.getHubId())
                .setPayload(event.toPayload())
                .build();
    }

    @Override
    public SpecificRecordBase toAvro(SensorEvent event) {
        return SensorEventAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setPayload(event.toPayload())
                .build();
    }
}