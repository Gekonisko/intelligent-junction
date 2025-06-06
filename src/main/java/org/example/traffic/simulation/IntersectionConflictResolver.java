package org.example.traffic.simulation;

import org.example.traffic.model.Vehicle;

import java.util.LinkedList;
import java.util.List;

public interface IntersectionConflictResolver {
    boolean isConflict(Vehicle a, Vehicle b);
    LinkedList<List<Vehicle>> nonConflictingGroup(List<Vehicle> vehicles);
}
