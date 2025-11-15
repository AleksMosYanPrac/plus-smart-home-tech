package ru.practicum.telemetry.analyzer.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

import java.util.Map;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@NoArgsConstructor
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hubId;
    private String name;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @MapKeyColumn(
            table = "scenario_conditions",
            name = "sensor_id")
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id"))
    private Map<String, Condition> conditions;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @MapKeyColumn(
            table = "scenario_actions",
            name = "sensor_id")
    @JoinTable(
            name = "scenario_actions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    private Map<String, Action> actions;


    public boolean checkConditions(Map<String, SensorStateAvro> sensorsState) {
        return conditions.entrySet().stream()
                .allMatch(entry -> entry.getValue().check(sensorsState.get(entry.getKey())));
    }

}