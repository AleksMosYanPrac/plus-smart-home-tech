package ru.practicum.telemetry.aggregator.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Objects;
import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    KafkaClient getKafkaClient() {
        return new KafkaClient() {

            @Value("${spring.kafka.consumer.group-id}")
            private String consumerGroup;

            @Value("${spring.kafka.consumer.value-deserializer}")
            private String consumerValueDeserializer;

            @Value("${spring.kafka.consumer.key-deserializer}")
            private String consumerKeyDeserializer;

            @Value("${spring.kafka.consumer.bootstrap-servers}")
            private String consumerBootstrap;

            @Value("${spring.kafka.producer.value-serializer}")
            private String producerValueSerializer;

            @Value("${spring.kafka.producer.key-serializer}")
            private String producerKeySerializer;

            @Value("${spring.kafka.producer.bootstrap-servers}")
            private String producerBootstrap;

            private Consumer<String, SensorEventAvro> consumer;
            private Producer<String, SensorsSnapshotAvro> producer;

            @Override
            public Consumer<String, SensorEventAvro> getConsumer() {
                return Objects.isNull(consumer) ? initConsumer() : consumer;
            }

            @Override
            public Producer<String, SensorsSnapshotAvro> getProducer() {
                return Objects.isNull(producer) ? initProducer() : producer;
            }

            private Consumer<String, SensorEventAvro> initConsumer() {
                return new KafkaConsumer<>(getConsumerConfig());
            }

            private Producer<String, SensorsSnapshotAvro> initProducer() {
                return new KafkaProducer<>(getProducerConfig());
            }

            private Properties getProducerConfig() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrap);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerKeySerializer);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerValueSerializer);
                return config;
            }

            private Properties getConsumerConfig() {
                Properties config = new Properties();
                config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrap);
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerKeyDeserializer);
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerValueDeserializer);
                config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
                return config;
            }
        };
    }
}