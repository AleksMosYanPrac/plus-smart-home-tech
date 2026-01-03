package ru.practicum.telemetry.analyzer.service.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.telemetry.analyzer.service.model.Sensor;

import java.util.Collection;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);
}