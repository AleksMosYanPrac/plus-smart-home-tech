package ru.practicum.telemetry.collector.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.events.interfaces.EventsService;

@RestController
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;

    @PostMapping("/events/sensors")
    public void postSensorEvent(@Valid @RequestBody SensorEvent event) {
        eventsService.sendSensorEvent(event);
    }

    @PostMapping("/events/hubs")
    public void postHubEvent(@Valid @RequestBody HubEvent event) {
        eventsService.sendHubEvent(event);
    }
}