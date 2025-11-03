package ru.practicum.telemetry.collector.events.dto.hub.scenario;

import lombok.Data;

@Data
public class ScenarioCondition {
    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private int value;
}