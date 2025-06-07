package org.example.traffic.cli;

import org.example.traffic.io.Command;
import org.example.traffic.io.JsonLoader;
import org.example.traffic.io.JsonWriter;
import org.example.traffic.model.EngineType;
import org.example.traffic.model.StepStatus;
import org.example.traffic.simulation.*;

import java.io.File;
import java.util.List;

public class SimulationCli {
    public static void runSimulation(File input, File output, EngineType engineType,
                                     int maxDepth, int simultaneousDecisions) {
        try {
            List<Command> commands = JsonLoader.loadCommands(input.getPath());

            IntersectionConflictResolver resolver = new FourWayIntersectionConflictResolver();
            Intersection intersection = new FourWayIntersection(resolver);

            DecisionTree decisionTree = null;
            if (engineType == EngineType.DECISION_TREE) {
                decisionTree = new IntersectionDecisionTree(intersection, resolver, maxDepth, simultaneousDecisions);
            }

            SimulationEngine engine = new SimulationEngine(
                    intersection,
                    engineType,
                    decisionTree
            );

            List<StepStatus> results = engine.runSimulation(commands);
            JsonWriter.writeToJsonFile(output.getPath(), results);
        } catch (Exception e) {
            System.err.println("Error running simulation: " + e.getMessage());
        }
    }
}
