package org.example.traffic.model;

import java.util.List;
import java.util.stream.Collectors;

public class StepStatus {
    public List<String> leftVehicles;

    public StepStatus(List<String> leftVehicles) {
        this.leftVehicles = leftVehicles.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "StepStatus{" +
                "leftVehicles=" + leftVehicles +
                '}';
    }
}
