package ru.practicum.telemetry.collector.events.dto.hub.device;

import lombok.Data;

@Data
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private int value;
}