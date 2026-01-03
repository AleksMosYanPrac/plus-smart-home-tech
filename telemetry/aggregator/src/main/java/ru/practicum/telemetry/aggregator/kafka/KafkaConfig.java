package ru.practicum.telemetry.aggregator.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    Consumer<String, SensorEventAvro> consumer(KafkaConfiguration consumerConfig) {
        return new KafkaConsumer<>(consumerConfig.getProperties());
    }

    @Bean
    Producer<String, SensorsSnapshotAvro> producer(KafkaConfiguration producerConfig) {
        return new KafkaProducer<>(producerConfig.getProperties());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.kafka.consumer")
    public KafkaConfiguration consumerConfig() {
        return new KafkaConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.kafka.producer")
    public KafkaConfiguration producerConfig() {
        return new KafkaConfiguration();
    }

    @Getter
    @Setter
    public static class KafkaConfiguration {
        private Properties properties;
    }
}