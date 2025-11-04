package ru.yandex.practicum.kafka.telemetry.serialization;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorEventDeserializer extends AvroDeserializer<SensorEventAvro>{
    public SensorEventDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }
}