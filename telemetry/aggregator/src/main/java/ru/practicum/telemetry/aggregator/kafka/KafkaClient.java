package ru.practicum.telemetry.aggregator.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface KafkaClient {
    Consumer<String, SensorEventAvro> getConsumer();

    Producer<String, SensorsSnapshotAvro> getProducer();
}