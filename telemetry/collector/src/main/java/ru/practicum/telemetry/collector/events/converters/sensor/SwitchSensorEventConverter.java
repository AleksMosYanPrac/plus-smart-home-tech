package ru.practicum.telemetry.collector.events.converters.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.interfaces.SensorEventConverter;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventConverter implements SensorEventConverter {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public SpecificRecordBase convert(SensorEventProto event) {
        SwitchSensorProto sensor = event.getSwitchSensor();
        return SwitchSensorAvro.newBuilder()
                .setState(sensor.getState())
                .build();
    }
}