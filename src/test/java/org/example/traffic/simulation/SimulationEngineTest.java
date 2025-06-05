package org.example.traffic.simulation;

import org.example.traffic.io.AddVehicleCommand;
import org.example.traffic.io.Command;
import org.example.traffic.io.JsonWriter;
import org.example.traffic.io.StepCommand;
import org.example.traffic.model.StepStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationEngineTest {

    private SimulationEngine engine;

    @BeforeEach
    public void setUp() {
        IntersectionConflictResolver resolver = new FourWayIntersectionConflictResolver();
        Intersection intersection = new FourWayIntersection(resolver);
        engine = new SimulationEngine(intersection);
    }

    @Test
    public void testSimulationSequence() throws Exception {

        List<Command> commands = List.of(
                new AddVehicleCommand("vehicle1", "south", "north"),
                new AddVehicleCommand("vehicle2", "north", "south"),
                new StepCommand(),
                new StepCommand(),
                new AddVehicleCommand("vehicle3", "west", "south"),
                new AddVehicleCommand("vehicle4", "west", "south"),
                new StepCommand(),
                new StepCommand()
        );
        List<StepStatus> results = engine.runSimulation(commands);

        List<StepStatus> expectedResults = List.of(
                new StepStatus(List.of("vehicle1", "vehicle2")),
                new StepStatus(List.of()),
                new StepStatus(List.of("vehicle3")),
                new StepStatus(List.of("vehicle4"))
        );

        assertEquals(JsonWriter.toJsonString(results),
                JsonWriter.toJsonString(expectedResults));
    }
}
