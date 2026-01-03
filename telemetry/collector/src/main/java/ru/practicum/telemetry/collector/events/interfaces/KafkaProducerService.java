package ru.practicum.telemetry.collector.events.interfaces;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface KafkaProducerService {

    void send(HubEventProto message);

    void send(SensorEventProto message);
}