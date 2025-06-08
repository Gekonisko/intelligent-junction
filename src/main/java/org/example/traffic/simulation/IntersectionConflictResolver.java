package org.example.traffic.simulation;

import org.example.traffic.model.Pedestrian;
import org.example.traffic.model.StepResult;
import org.example.traffic.model.Vehicle;

import java.util.LinkedList;
import java.util.List;

public interface IntersectionConflictResolver {
    boolean isConflict(Vehicle a, Vehicle b);
    List<StepResult> nonConflictingGroups(List<Vehicle> vehicles, List<Pedestrian> pedestrians);
}
