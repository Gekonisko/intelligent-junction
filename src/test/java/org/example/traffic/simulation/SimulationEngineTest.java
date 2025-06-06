package org.example.traffic.simulation;

import org.example.traffic.io.AddVehicleCommand;
import org.example.traffic.io.Command;
import org.example.traffic.io.JsonWriter;
import org.example.traffic.io.StepCommand;
import org.example.traffic.model.StepStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

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
    public void testSimulation_baseExample() throws Exception {

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

    @Test
    public void testSimulation_twentyVehicles() throws Exception {

        List<Command> commands = List.of(
                new AddVehicleCommand("vehicle1", "west", "east"),
                new AddVehicleCommand("vehicle2", "south", "north"),
                new AddVehicleCommand("vehicle3", "east", "north"),
                new AddVehicleCommand("vehicle4", "north", "east"),
                new StepCommand(),
                new AddVehicleCommand("vehicle5", "west", "north"),
                new AddVehicleCommand("vehicle6", "south", "east"),
                new AddVehicleCommand("vehicle7", "east", "west"),
                new AddVehicleCommand("vehicle8", "north", "south"),
                new StepCommand(),
                new StepCommand(),
                new AddVehicleCommand("vehicle9", "west", "east"),
                new AddVehicleCommand("vehicle10", "south", "east"),
                new AddVehicleCommand("vehicle11", "east", "south"),
                new AddVehicleCommand("vehicle12", "north", "west"),
                new StepCommand(),
                new StepCommand(),
                new StepCommand(),
                new StepCommand()
        );
        List<StepStatus> results = engine.runSimulation(commands);

        int vehicles = results.stream().map(l -> l.leftVehicles.size()).reduce(0, Integer::sum);

        assertEquals(12, vehicles);
    }
}
