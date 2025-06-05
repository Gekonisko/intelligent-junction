package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.StepStatus;
import org.example.traffic.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FourWayIntersectionTest {
    private final IntersectionConflictResolver resolver = new FourWayIntersectionConflictResolver();
    private FourWayIntersection intersection;

    @BeforeEach
    public void setUp() {
        intersection = new FourWayIntersection(resolver);
    }

    @Test
    public void testVehiclesFromNonConflictingDirectionsProceed() {
        Vehicle v1 = new Vehicle("V1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("V2", Direction.WEST, Direction.EAST);
        Vehicle v3 = new Vehicle("V3", Direction.SOUTH, Direction.EAST);

        intersection.addVehicle(v1);
        intersection.addVehicle(v2);
        intersection.addVehicle(v3);

        StepStatus status = intersection.step();

        List<String> actual = status.leftVehicles;

        assertEquals(2, actual.size());
        assert(actual.contains("V1") && actual.contains("V3"));
    }

    @Test
    public void testSingleVehicleProceeds() {
        Vehicle v1 = new Vehicle("V1", Direction.NORTH, Direction.SOUTH);

        intersection.addVehicle(v1);

        StepStatus status = intersection.step();

        assertEquals(List.of("V1"), status.leftVehicles);
    }

    @Test
    public void testNoVehicles() {
        StepStatus status = intersection.step();
        assertEquals(List.of(), status.leftVehicles);
    }
}
