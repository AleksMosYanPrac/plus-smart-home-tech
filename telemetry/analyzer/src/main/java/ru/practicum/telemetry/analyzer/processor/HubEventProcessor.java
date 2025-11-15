package ru.practicum.telemetry.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.analyzer.service.AnalyzerService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final AnalyzerService analyzerService;
    private final Consumer<String, HubEventAvro> hubEventConsumer;

    @Override
    public void run() {
        try {
            while (true) {
                hubEventConsumer.poll(Duration.ofSeconds(5)).forEach(record -> {
                    log.info("Receive Hub Event hubID:{}", record.value().getHubId());
                    analyzerService.analyze(record.value());
                    hubEventConsumer.commitSync();
                });
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Exception on processing hub event", e);
        } finally {
            hubEventConsumer.commitSync();
            hubEventConsumer.close();
        }
    }
}