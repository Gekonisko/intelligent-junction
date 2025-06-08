package org.example.traffic.simulation;

import org.example.traffic.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class FourWayIntersection implements Intersection {
    private final Map<Direction, Queue<Vehicle>> queues = new EnumMap<>(Direction.class);
    private final Map<Direction, Integer> roadPriorities = new EnumMap<>(Direction.class);
    private final Map<Direction, Boolean> pedestrians = new EnumMap<>(Direction.class);
    private final IntersectionConflictResolver conflictResolver;
    private boolean isFailureMode = false;
    private Direction lastClockwiseDirection = Direction.NORTH;

    public FourWayIntersection(IntersectionConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;

        for (Direction dir : Direction.values()) {
            queues.put(dir, new LinkedList<>());
        }
        for (Direction dir : Direction.values()) {
            roadPriorities.put(dir, 0);
        }
        for (Direction dir : Direction.values()) {
            pedestrians.put(dir, false);
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
        if(isFailureMode) return clockwiseAlternatingTraffic();

        List<String> left = new ArrayList<>();

        var bestStep = getBestSteps(1).poll();
        if (bestStep != null) {
            List<Vehicle> removedVehicles = removeVehicles(bestStep.getLeftVehicles());
            removedVehicles.forEach(v -> left.add(v.vehicleId));
        }
        return new StepStatus(left);
    }

    @Override
    public StepStatus step(DecisionTree decisionTree) {
        if(isFailureMode) return clockwiseAlternatingTraffic();

        List<String> left = new ArrayList<>();

        var bestSteps = getBestSteps(decisionTree.getSimultaneousDecisions());
        var decisions = bestSteps.stream().map(s -> new StepNode(s.getLeftVehicles(), s.getLeftPedestrians())).toList();

        var best = decisionTree.getBestStepNode(decisions, getVehicles());
        if (best != null) {
            List<Vehicle> removedVehicles = removeVehicles(best.getLeftVehicles());
            removedVehicles.forEach(v -> left.add(v.vehicleId));
        }
        return new StepStatus(left);
    }

    @Override
    public void setFailureMode(boolean value) {
        isFailureMode = value;
    }

    @Override
    public void setRoadPriority(Direction direction, int priority) {
        roadPriorities.put(direction, priority);
    }

    @Override
    public void addPedestrian(Direction direction) {
        pedestrians.put(direction, true);
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

    @Override
    public LinkedList<StepResult> getBestSteps(int maxSteps) {
        LinkedList<StepResult> bestSteps = new LinkedList<>();
        List<Vehicle> frontVehicles = getFrontVehicles();

        var nonConflict = conflictResolver.nonConflictingGroup(frontVehicles, getPedestrians());
        var sortedNonConflict = sortStepResults(nonConflict);

        for (int i = 0; i < maxSteps; i++) {
            if(sortedNonConflict.isEmpty()) break;
            bestSteps.add(sortedNonConflict.poll());
        }
        return bestSteps;
    }

    @Override
    public List<Pedestrian> getPedestrians() {
        List<Pedestrian> result = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            if (pedestrians.get(dir) != null && pedestrians.get(dir)) {
                result.add(new Pedestrian(dir));
            }
        }
        return result;
    }

    private LinkedList<StepResult> sortStepResults(List<StepResult> stepResults) {
        return stepResults.stream()
                .sorted(Comparator.comparingInt(StepResult::getFullSize).thenComparing(this::getGroupPriority).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private int getGroupPriority(StepResult step) {
        int sum = 0;
        for (Vehicle v : step.getLeftVehicles()) {
            sum += roadPriorities.get(v.from);
        }
        return sum;
    }

    private StepStatus clockwiseAlternatingTraffic()
    {
        var newDirection = Direction.leftOf(lastClockwiseDirection);
        while (lastClockwiseDirection != newDirection) {
            if(!queues.get(newDirection).isEmpty()) {
                lastClockwiseDirection = newDirection;
                var vehicle = queues.get(newDirection).poll();
                return new StepStatus(List.of(vehicle.vehicleId));
            }
            newDirection = Direction.leftOf(lastClockwiseDirection);
        }
        return new StepStatus(List.of());
    }
}
