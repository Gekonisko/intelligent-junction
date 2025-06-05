package org.example.traffic.simulation;

import org.example.traffic.model.Vehicle;

import java.util.List;
import java.util.Set;

public interface IntersectionConflictResolver {
    boolean isConflict(Vehicle a, Vehicle b);
    Set<List<Vehicle>> nonConflictingGroup(List<Vehicle> vehicles);
}
