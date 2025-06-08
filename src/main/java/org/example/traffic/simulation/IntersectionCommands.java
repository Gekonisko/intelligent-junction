package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;

public interface IntersectionCommands {
    void addVehicle(Vehicle v);
    StepStatus step();
    StepStatus step(DecisionTree decisionTree);
    void setFailureMode(boolean value);
    void setRoadPriority(Direction direction, int priority);
    void addPedestrian(Direction direction);
}
