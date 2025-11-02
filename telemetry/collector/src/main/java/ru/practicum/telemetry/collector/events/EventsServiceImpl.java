package ru.practicum.telemetry.collector.events;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.events.interfaces.EventsService;
import ru.practicum.telemetry.collector.events.interfaces.EventsMapper;
import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final KafkaTemplate<String, SpecificRecordBase> producer;
    private final EventsMapper eventsMapper;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        SpecificRecordBase sensorEvent = eventsMapper.toAvro(event);
        producer.send(new ProducerRecord<>("telemetry.sensors.v1", sensorEvent));
        log.debug("Sensor event: {}", sensorEvent);
    }

    @Override
    public void sendHubEvent(HubEvent event) {
        SpecificRecordBase hubEvent = eventsMapper.toAvro(event);
        producer.send(new ProducerRecord<>("telemetry.hubs.v1", hubEvent));
        log.debug("Hub event: {}", hubEvent);
    }

    @PreDestroy
    public void preDestroy(){
        log.info("Pre Destroy method");
        producer.flush();
        producer.setCloseTimeout(Duration.ofSeconds(5));
        producer.destroy();
    }
}