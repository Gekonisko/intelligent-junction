package org.example.traffic.simulation;

import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;

import java.util.List;


public interface Intersection {
    Intersection createNewIntersection();
    void addVehicle(Vehicle v);
    void addVehicles(List<Vehicle> list);
    Vehicle removeVehicle(Vehicle v);
    List<Vehicle> removeVehicles(List<Vehicle> list);
    List<Vehicle> getVehicles();
    StepStatus step(DecisionTree decisionTree, int simultaneousDecisions);
    StepStatus step();
    List<Vehicle> getFrontVehicles();
}