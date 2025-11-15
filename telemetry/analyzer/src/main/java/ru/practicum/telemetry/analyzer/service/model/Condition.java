package ru.practicum.telemetry.analyzer.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Objects;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String operation;
    private Integer value;

    public boolean check(SensorStateAvro sensorStateAvro) {
        if (Objects.isNull(sensorStateAvro)) {
            return false;
        }
        int actual = chooseBySensorType(sensorStateAvro.getData());
        return switch (operation) {
            case "EQUALS" -> value == actual;
            case "GREATER_THAN" -> value < actual;
            case "LOWER_THAN" -> value > actual;
            default -> throw new IllegalStateException("Unexpected operation: " + operation);
        };
    }

    private int chooseBySensorType(Object data) {
        return switch (data) {
            case ClimateSensorAvro climateSensorAvro -> getActualValue(climateSensorAvro);
            case TemperatureSensorAvro temperatureSensorAvro -> getActualValue(temperatureSensorAvro);
            case LightSensorAvro lightSensorAvro -> getActualValue(lightSensorAvro);
            case MotionSensorAvro motionSensorAvro -> getActualValue(motionSensorAvro);
            case SwitchSensorAvro switchSensorAvro -> getActualValue(switchSensorAvro);
            default -> throw new IllegalArgumentException("Unknown SensorType: " + data.getClass().getSimpleName());
        };
    }

    private int getActualValue(SwitchSensorAvro switchSensorAvro) {
        return switchSensorAvro.getState() ? 1 : 0;
    }

    private int getActualValue(MotionSensorAvro motionSensorAvro) {
        return motionSensorAvro.getMotion() ? 1 : 0;
    }

    private int getActualValue(LightSensorAvro lightSensorAvro) {
        return lightSensorAvro.getLuminosity();
    }

    private int getActualValue(TemperatureSensorAvro temperatureSensorAvro) {
        return temperatureSensorAvro.getTemperatureC();
    }

    private int getActualValue(ClimateSensorAvro climateSensorAvro) {
        return switch (type) {
            case "TEMPERATURE" -> climateSensorAvro.getTemperatureC();
            case "HUMIDITY" -> climateSensorAvro.getHumidity();
            case "CO2LEVEL" -> climateSensorAvro.getCo2Level();
            default -> 0;
        };
    }
}