package ru.practicum.telemetry.aggregator.aggregation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final AggregationService aggregationService;
    private final KafkaService kafkaService;
    private boolean isStarted;

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        if (!isStarted) {
            isStarted = true;
            try {
                kafkaService.subscribeTopics();
                while (true) {
                    kafkaService.poll().forEach(record -> {
                        log.info("Receive hubID:{} payload:{}", record.value().getHubId(), record.value().getPayload());
                        aggregationService.updateState(record.value()).ifPresent((snapshot) -> {
                            kafkaService.send(snapshot);
                            log.debug("Send snapshot:{}", snapshot);
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
}