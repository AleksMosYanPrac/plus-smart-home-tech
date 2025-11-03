package ru.practicum.telemetry.collector.events.dto.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {

    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public SpecificRecordBase toPayload() {
        return ScenarioRemovedEventAvro
                .newBuilder()
                .setName(name)
                .build();
    }
}