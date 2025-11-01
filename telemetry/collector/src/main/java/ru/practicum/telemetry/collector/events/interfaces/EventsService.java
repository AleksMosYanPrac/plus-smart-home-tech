package ru.practicum.telemetry.collector.events.interfaces;

import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;

public interface EventsService {
    void sendSensorEvent(SensorEvent event);

    void sendHubEvent(HubEvent event);
}
