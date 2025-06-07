package org.example.traffic.cli;

import org.example.traffic.api.TrafficApplication;
import org.example.traffic.model.EngineType;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "traffic-sim", mixinStandardHelpOptions = true)
public class TrafficCli implements Runnable {
    @CommandLine.Option(names = "--serve", description = "Start backend (Spring Boot)")
    boolean serve;

    @CommandLine.Option(names = "--input", description = "Input JSON file")
    File input;

    @CommandLine.Option(names = "--output", description = "Output JSON file")
    File output;

    @CommandLine.Option(names = "--engine", defaultValue = "DECISION_TREE",
            description = "Simulation engine: ${COMPLETION-CANDIDATES}")
    EngineType engineType;

    @CommandLine.Option(names = "--max-depth", defaultValue = "4",
            description = "Max depth for decision tree (only for DECISION_TREE)")
    int maxDepth;

    @CommandLine.Option(names = "--simultaneous-decisions", defaultValue = "4",
            description = "Max children per node (only for DECISION_TREE)")
    int simultaneousDecisions;

    @Override
    public void run() {
        if (serve) {
            SpringApplication app = new SpringApplication(TrafficApplication.class);
            ConfigurableApplicationContext context = app.run();

            return;
        }

        if (input == null || output == null) {
            System.err.println("Missing --input or --output.");
            return;
        }

        SimulationCli.runSimulation(input, output, engineType, maxDepth, simultaneousDecisions);
    }
}
