package ru.practicum.telemetry.collector.events;

import com.google.protobuf.Timestamp;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.collector.events.interfaces.EventsMapper;
import ru.practicum.telemetry.collector.events.interfaces.HubEventConverter;
import ru.practicum.telemetry.collector.events.interfaces.SensorEventConverter;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventsMapperImpl implements EventsMapper {

    private final Map<SensorEventProto.PayloadCase, SensorEventConverter> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventConverter> hubEventHandlers;

    public EventsMapperImpl(Set<SensorEventConverter> sensorEventHandlers,
                            Set<HubEventConverter> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventConverter::getMessageType,
                        Function.identity()
                ));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventConverter::getMessageType,
                        Function.identity()
                ));
    }

    @Override
    public SpecificRecordBase toAvro(SensorEventProto event) {
        return SensorEventAvro.newBuilder()
                .setTimestamp(getInstant(event.getTimestamp()))
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setPayload(toAvroPayload(event))
                .build();
    }

    @Override
    public SpecificRecordBase toAvro(HubEventProto event) {
        return HubEventAvro.newBuilder()
                .setTimestamp(getInstant(event.getTimestamp()))
                .setHubId(event.getHubId())
                .setPayload(toAvroPayload(event))
                .build();
    }

    private Object toAvroPayload(SensorEventProto event) {
        if (sensorEventHandlers.containsKey(event.getPayloadCase())) {
            return sensorEventHandlers.get(event.getPayloadCase()).convert(event);
        }
        throw new IllegalArgumentException("Handler is absent for " + event.getPayloadCase());
    }

    private Object toAvroPayload(HubEventProto event) {
        if (hubEventHandlers.containsKey(event.getPayloadCase())) {
            return hubEventHandlers.get(event.getPayloadCase()).convert(event);
        }
        throw new IllegalArgumentException("Handler is absent for " + event.getPayloadCase());
    }

    private Instant getInstant(Timestamp event) {
        return Instant.ofEpochSecond(event.getSeconds(), event.getNanos());
    }
}