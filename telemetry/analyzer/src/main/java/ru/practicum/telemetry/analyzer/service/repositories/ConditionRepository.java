package ru.practicum.telemetry.analyzer.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.telemetry.analyzer.service.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}