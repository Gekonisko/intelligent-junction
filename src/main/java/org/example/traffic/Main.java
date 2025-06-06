package org.example.traffic;

import org.example.traffic.io.Command;
import org.example.traffic.io.JsonLoader;
import org.example.traffic.io.JsonWriter;
import org.example.traffic.model.StepStatus;
import org.example.traffic.simulation.*;

import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar traffic-simulation.jar input.json output.json");
            return;
        }

        List<Command> commands = JsonLoader.loadCommands(args[0]);

        IntersectionConflictResolver conflictResolver = new FourWayIntersectionConflictResolver();
        Intersection intersection = new FourWayIntersection(conflictResolver);
        IntersectionDecisionTree decisionTree = new IntersectionDecisionTree(intersection, conflictResolver, 4, 4);

        SimulationEngine engine = new SimulationEngine(intersection, decisionTree);

        List<StepStatus> results = engine.runSimulation(commands);
        System.out.println(JsonWriter.toJsonString(results));
    }
}