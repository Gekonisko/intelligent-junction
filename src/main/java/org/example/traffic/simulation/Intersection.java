package org.example.traffic.simulation;

import org.example.traffic.model.Pedestrian;
import org.example.traffic.model.StepResult;
import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;

import java.util.LinkedList;
import java.util.List;


public interface Intersection extends IntersectionCommands {
    Intersection createNewIntersection();
    void addVehicles(List<Vehicle> list);
    Vehicle removeVehicle(Vehicle v);
    List<Vehicle> removeVehicles(List<Vehicle> list);
    List<Vehicle> getVehicles();
    List<Pedestrian> getPedestrians();
    List<Vehicle> getFrontVehicles();
    List<StepResult> getBestSteps(int maxSteps);
}