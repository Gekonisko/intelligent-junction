package org.example.traffic.model;

import java.util.List;
import java.util.stream.Collectors;

public class StepStatus {
    private final List<String> leftVehicles;

    public StepStatus(List<String> leftVehicles) {
        this.leftVehicles = leftVehicles.stream().sorted().collect(Collectors.toList());
    }

    public List<String> getLeftVehicles() {
        return leftVehicles;
    }

    @Override
    public String toString() {
        return "StepStatus{" +
                "leftVehicles=" + leftVehicles +
                '}';
    }
}
