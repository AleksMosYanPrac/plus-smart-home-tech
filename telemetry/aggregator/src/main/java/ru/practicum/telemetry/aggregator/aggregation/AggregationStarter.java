package ru.practicum.telemetry.aggregator.aggregation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final AggregationService aggregationService;
    private final KafkaService kafkaService;

    public void start() {
        try {
            kafkaService.subscribeTopics();
            while (true) {
                kafkaService.poll().forEach(record -> {
                    log.debug("Receive message hubID:{}", record.value().getHubId());
                    aggregationService.updateState(record.value()).ifPresent((snapshot) -> {
                        kafkaService.send(snapshot);
                        log.debug("Send message hubID:{}", snapshot.getHubId());
                    });
                });
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Exception on processing sensor events", e);
        } finally {
            kafkaService.stop();
        }
    }
}