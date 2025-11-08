package ru.practicum.telemetry.analyzer.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.telemetry.analyzer.service.model.Scenario;

import java.util.List;
import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findByHubId(String hubId);

    Optional<Scenario> findByHubIdAndName(String hubId, String name);
}