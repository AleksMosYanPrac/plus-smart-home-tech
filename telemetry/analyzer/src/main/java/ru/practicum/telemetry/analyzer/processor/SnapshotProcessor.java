package ru.practicum.telemetry.analyzer.processor;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.telemetry.analyzer.service.AnalyzerService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;

import static ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {

    private final AnalyzerService analyzerService;
    private final HubRouterControllerBlockingStub hubRouterClient;
    private final Consumer<String, SensorsSnapshotAvro> sensorsSnapshotAvroConsumer;

    @Override
    public void run() {
        try {
            while (true) {
                sensorsSnapshotAvroConsumer.poll(Duration.ofSeconds(3)).forEach(record -> {
                    log.info("Receive Snapshot HUB_ID: {}", record.value().getHubId());
                    analyzerService.analyze(record.value()).ifPresent(l -> {
                        l.forEach(actionRequest -> {
                            try {
                                hubRouterClient.handleDeviceAction(actionRequest);
                            } catch (StatusRuntimeException e) {
                                log.error("gRPC send request to HUB:{} scenario:{}  error:{}.",
                                        actionRequest.getHubId(),
                                        actionRequest.getScenarioName(),
                                        e.getStatus().getDescription(), e);
                            }
                        });
                    });
                });
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Exception on processing snapshot", e);
        } finally {
            sensorsSnapshotAvroConsumer.commitSync();
            sensorsSnapshotAvroConsumer.close();
        }
    }
}