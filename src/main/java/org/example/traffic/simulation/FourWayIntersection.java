package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.StepNode;
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

    @Override
    public Intersection createNewIntersection() {
        return new FourWayIntersection(this.conflictResolver);
    }

    @Override
    public void addVehicle(Vehicle v) {
        queues.get(v.from).add(v);
    }

    @Override
    public void addVehicles(List<Vehicle> list) {
        for (var v : list) {
            addVehicle(v);
        }
    }

    @Override
    public Vehicle removeVehicle(Vehicle v) {
        var queueVehicle = Objects.requireNonNull(queues.get(v.from).peek());
        if (Objects.equals(queueVehicle.vehicleId, v.vehicleId)) {
            return  Objects.requireNonNull(queues.get(v.from).poll());
        }
        return null;
    }

    @Override
    public List<Vehicle> removeVehicles(List<Vehicle> list) {
        List<Vehicle> removedVehicles = new ArrayList<>();
        for (var v : list) {
            if (removeVehicle(v) != null) {
                removedVehicles.add(v);
            }
        }
        return removedVehicles;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> allVehicles = new ArrayList<>();
        for (Queue<Vehicle> queue : queues.values()) {
            allVehicles.addAll(queue);
        }
        return allVehicles;
    }

    @Override
    public StepStatus step() {
        List<String> left = new ArrayList<>();

        List<Vehicle> frontVehicles = getFrontVehicles();

        var nonConflict = conflictResolver.nonConflictingGroup(frontVehicles);
        var maxGroup = nonConflict.poll();

        if (maxGroup != null) {
            List<Vehicle> removedVehicles = removeVehicles(maxGroup);
            removedVehicles.forEach(v -> left.add(v.vehicleId));
        }
        return new StepStatus(left);
    }

    @Override
    public StepStatus step(DecisionTree decisionTree) {
        List<String> left = new ArrayList<>();

        List<Vehicle> frontVehicles = getFrontVehicles();
        var nonConflict = conflictResolver.nonConflictingGroup(frontVehicles);

        List<StepNode> decisions = new ArrayList<>();
        for (int i = 0; i < decisionTree.getSimultaneousDecisions(); i++) {
            if (nonConflict.isEmpty()) break;
            decisions.add(new StepNode(nonConflict.poll()));
        }

        var best = decisionTree.getBestStepNode(decisions, getVehicles());
        if (best != null) {
            List<Vehicle> removedVehicles = removeVehicles(best.leftVehicles);
            removedVehicles.forEach(v -> left.add(v.vehicleId));
        }
        return new StepStatus(left);
    }

    @Override
    public List<Vehicle> getFrontVehicles() {
        List<Vehicle> result = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Queue<Vehicle> queue = queues.get(dir);
            if (queue != null && !queue.isEmpty()) {
                result.add(queue.peek());
            }
        }
        return result;
    }

}
