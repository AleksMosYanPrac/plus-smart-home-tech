package ru.practicum.telemetry.analyzer.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    Consumer<String, HubEventAvro> hubEventConsumer(ConsumerConfig hubEventConsumerConfig) {
        Consumer<String, HubEventAvro> consumer = new KafkaConsumer<>(hubEventConsumerConfig.getProperties());
        consumer.subscribe(List.of(hubEventConsumerConfig.getTopic()));
        return consumer;
    }

    @Bean
    Consumer<String, SensorsSnapshotAvro> sensorsSnapshotConsumer(ConsumerConfig snapshotConsumerConfig) {
        Consumer<String, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(snapshotConsumerConfig.getProperties());
        consumer.subscribe(List.of(snapshotConsumerConfig.getTopic()));
        return consumer;
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka.hub-event-consumer")
    public ConsumerConfig hubEventConsumerConfig() {
        return new ConsumerConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka.snapshot-consumer")
    public ConsumerConfig snapshotConsumerConfig() {
        return new ConsumerConfig();
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private String topic;
        private Properties properties;
    }
}