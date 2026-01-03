package ru.practicum.telemetry.collector.events.converters.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.interfaces.SensorEventConverter;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventConverter implements SensorEventConverter {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public SpecificRecordBase convert(SensorEventProto event) {
        MotionSensorProto sensor = event.getMotionSensor();
        return MotionSensorAvro.newBuilder()
                .setVoltage(sensor.getVoltage())
                .setMotion(sensor.getMotion())
                .setLinkQuality(sensor.getLinkQuality())
                .build();
    }
}