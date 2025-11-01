package ru.practicum.telemetry.collector.events;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.events.interfaces.EventsService;
import ru.practicum.telemetry.collector.events.interfaces.EventsMapper;
import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final Producer<String, SpecificRecordBase> producer;
    private final EventsMapper eventsMapper;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        producer.send(new ProducerRecord<>("telemetry.sensors.v1", eventsMapper.toAvro(event)));
    }

    @Override
    public void sendHubEvent(HubEvent event) {
        producer.send(new ProducerRecord<>("telemetry.hubs.v1", eventsMapper.toAvro(event)));
    }
}