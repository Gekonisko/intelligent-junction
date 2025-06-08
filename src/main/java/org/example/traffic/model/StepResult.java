package org.example.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class StepResult {
    private final List<Vehicle> leftVehicles;
    private final List<Pedestrian> leftPedestrians;

    public StepResult() {
        this.leftVehicles = new ArrayList<>();
        this.leftPedestrians = new ArrayList<>();
    }

    public StepResult(List<Vehicle> leftVehicles, List<Pedestrian> leftPedestrians) {
        this.leftVehicles = leftVehicles;
        this.leftPedestrians = leftPedestrians;
    }

    public int getFullSize() {
        return leftVehicles.size() + leftPedestrians.size();
    }

    public List<Vehicle> getLeftVehicles() {
        return leftVehicles;
    }

    public List<Pedestrian> getLeftPedestrians() {
        return leftPedestrians;
    }

    public void addVehicle(Vehicle vehicle) {
        leftVehicles.add(vehicle);
    }

    public void addPedestrians(List<Pedestrian> pedestrians) {
        leftPedestrians.addAll(pedestrians);
    }
}
