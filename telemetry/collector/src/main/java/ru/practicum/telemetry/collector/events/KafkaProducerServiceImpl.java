package ru.practicum.telemetry.collector.events;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.collector.events.interfaces.EventsMapper;
import ru.practicum.telemetry.collector.events.interfaces.KafkaProducerService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, SpecificRecordBase> producer;
    private final EventsMapper eventMapper;

    @Value("${collector.kafka.producer.hub-topic}")
    private String hubTopic;

    @Value("${collector.kafka.producer.hub-topic}")
    private String sensorTopic;

    @Override
    public void send(HubEventProto event) {
        SpecificRecordBase hubEvent = eventMapper.toAvro(event);
        producer.send(hubTopic, hubEvent);
        log.debug("Hub event: {}", hubEvent);
    }

    @Override
    public void send(SensorEventProto event) {
        SpecificRecordBase sensorEvent = eventMapper.toAvro(event);
        producer.send(sensorTopic, sensorEvent);
        log.debug("Sensor event: {}", sensorEvent);
    }

    @PreDestroy
    public void preDestroy(){
        log.info("Pre Destroy method");
        producer.flush();
        producer.setCloseTimeout(Duration.ofSeconds(5));
        producer.destroy();
    }
}