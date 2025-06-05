package org.example.traffic.simulation;

import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;


public interface Intersection {
    void addVehicle(Vehicle v);
    StepStatus step();
}