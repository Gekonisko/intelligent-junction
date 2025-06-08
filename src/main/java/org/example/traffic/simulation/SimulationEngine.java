package org.example.traffic.simulation;

import org.example.traffic.io.*;
import org.example.traffic.model.Direction;
import org.example.traffic.model.EngineType;
import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final IntersectionCommands intersection;
    private final DecisionTree decisionTree;
    private final EngineType engineType;
    private final List<StepStatus> results = new ArrayList<>();

    public SimulationEngine(Intersection intersection, EngineType engineType, DecisionTree decisionTree) {
        this.intersection = intersection;
        this.engineType = engineType;
        this.decisionTree = decisionTree;
    }

    public SimulationEngine(Intersection intersection, EngineType engineType) {
        this.intersection = intersection;
        this.engineType = engineType;
        this.decisionTree = null;
    }

    public List<StepStatus> runSimulation(List<Command> commands) {
        for (Command cmd : commands) {
            if (cmd instanceof AddVehicleCommand c) {
                Vehicle v = new Vehicle(c.vehicleId, Direction.fromString(c.startRoad), Direction.fromString(c.endRoad));
                intersection.addVehicle(v);
            }
            else if (cmd instanceof FailureModeCommand c) {
                intersection.setFailureMode(c.state);
            }
            else if (cmd instanceof RoadPriorityCommand c) {
                intersection.setRoadPriority(Direction.fromString(c.road), c.priority);
            }
            else if (cmd instanceof AddPedestrianCommand c) {
                intersection.addPedestrian(Direction.fromString(c.road));
            }
            else if (cmd instanceof StepCommand) {
                if (engineType == EngineType.DECISION_TREE) {
                    results.add(intersection.step(decisionTree));
                }
                else if(engineType == EngineType.MAX_GROUP) {
                    results.add(intersection.step());
                }
            }
        }
        return results;
    }
}