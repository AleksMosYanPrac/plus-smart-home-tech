package ru.practicum.telemetry.collector.events.converters.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.interfaces.SensorEventConverter;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventConverter implements SensorEventConverter {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public SpecificRecordBase convert(SensorEventProto event) {
        TemperatureSensorProto sensor = event.getTemperatureSensor();
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureF(sensor.getTemperatureF())
                .setTemperatureC(sensor.getTemperatureC())
                .build();
    }
}