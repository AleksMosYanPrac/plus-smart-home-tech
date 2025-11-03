package ru.practicum.telemetry.collector.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.telemetry.collector.events.dto.hub.HubEvent;
import ru.practicum.telemetry.collector.events.dto.sensor.SensorEvent;
import ru.practicum.telemetry.collector.events.interfaces.EventsService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;

    @PostMapping("/events/sensors")
    public void postSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Processing sensor event id:{} hubId:{} type:{}", event.getId(), event.getHubId(), event.getType());
        eventsService.sendSensorEvent(event);
    }

    @PostMapping("/events/hubs")
    public void postHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Processing hub event id:{} hubId:{} type:{}", event.getId(), event.getHubId(), event.getType());
        eventsService.sendHubEvent(event);
    }
}