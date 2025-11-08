package ru.practicum.telemetry.aggregator.aggregation;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.telemetry.aggregator.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class KafkaService {

    private Consumer<String, SensorEventAvro> consumer;
    private Producer<String, SensorsSnapshotAvro> producer;

    @Value("${aggregator.kafka.producer.snapshot-topic}")
    private String snapshotTopic;

    @Value("${aggregator.kafka.consumer.sensor-topic}")
    private String sensorTopic;

    public KafkaService(KafkaClient kafkaClient) {
        this.producer = kafkaClient.getProducer();
        this.consumer = kafkaClient.getConsumer();
    }

    public ConsumerRecords<String, SensorEventAvro> poll() {
        return consumer.poll(Duration.ofSeconds(5));
    }

    public void send(SensorsSnapshotAvro snapshot) {
        producer.send(new ProducerRecord<>(snapshotTopic, snapshot));
    }

    public void subscribeTopics(){
        consumer.subscribe(List.of(sensorTopic));
    }

    @PreDestroy
    public void stop() {
        try {
            producer.flush();
            consumer.commitSync();
        } finally {
            log.info("Close producer");
            producer.close();
            log.info("Close consumer");
            consumer.close();
        }
    }
}