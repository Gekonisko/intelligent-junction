package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;

import java.util.*;

public class FourWayIntersection implements Intersection {
    private final Map<Direction, Queue<Vehicle>> queues = new EnumMap<>(Direction.class);
    private final IntersectionConflictResolver conflictResolver;

    public FourWayIntersection(IntersectionConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;

        for (Direction dir : Direction.values()) {
            queues.put(dir, new LinkedList<>());
        }
    }

    public void addVehicle(Vehicle v) {
        queues.get(v.from).add(v);
    }

    public StepStatus step() {
        List<String> left = new ArrayList<>();

        List<Vehicle> frontVehicles = getFrontVehicles();

        Set<List<Vehicle>> nonConflict = conflictResolver.nonConflictingGroup(frontVehicles);
        var maxGroup = nonConflict.stream().max(Comparator.comparingInt(List::size)).orElse(null);

        if (maxGroup != null) {
            for (Vehicle v : maxGroup) {
                if (Objects.equals(queues.get(v.from).peek(), v)) {
                    String vehicleId = Objects.requireNonNull(queues.get(v.from).poll()).vehicleId;
                    left.add(vehicleId);
                }
            }
        }
        return new StepStatus(left);
    }

    private List<Vehicle> getFrontVehicles() {
        List<Vehicle> result = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Queue<Vehicle> queue = queues.get(dir);
            if (queue != null && !queue.isEmpty()) {
                result.add(queue.peek()); // does not remove it
            }
        }
        return result;
    }
}
