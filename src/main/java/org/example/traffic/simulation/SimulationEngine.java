package org.example.traffic.simulation;

import org.example.traffic.io.AddVehicleCommand;
import org.example.traffic.io.Command;
import org.example.traffic.io.StepCommand;
import org.example.traffic.model.Direction;
import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final Intersection intersection;
    private final DecisionTree decisionTree;
    private final List<StepStatus> results = new ArrayList<>();

    public SimulationEngine(Intersection intersection, DecisionTree decisionTree) {
        this.intersection = intersection;
        this.decisionTree = decisionTree;
    }

    public SimulationEngine(Intersection intersection) {
        this.intersection = intersection;
        this.decisionTree = null;
    }

    public List<StepStatus> runSimulation(List<Command> commands) {
        for (Command cmd : commands) {
            if (cmd instanceof AddVehicleCommand c) {
                Vehicle v = new Vehicle(c.vehicleId, Direction.fromString(c.startRoad), Direction.fromString(c.endRoad));
                intersection.addVehicle(v);
            } else if (cmd instanceof StepCommand) {
                if (decisionTree != null) {
                    results.add(intersection.step(decisionTree, 4));
                }
                else {
                    results.add(intersection.step());
                }
            }
        }
        return results;
    }
}