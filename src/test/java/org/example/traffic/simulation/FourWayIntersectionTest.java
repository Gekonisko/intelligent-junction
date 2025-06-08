package org.example.traffic.simulation;

import org.example.traffic.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        List<String> actual = status.getLeftVehicles();

        assertEquals(2, actual.size());
        assert(actual.contains("V1") && actual.contains("V3"));
    }

    @Test
    public void testSingleVehicleProceeds() {
        Vehicle v1 = new Vehicle("V1", Direction.NORTH, Direction.SOUTH);

        intersection.addVehicle(v1);

        StepStatus status = intersection.step();

        assertEquals(List.of("V1"), status.getLeftVehicles());
    }

    @Test
    public void testNoVehicles() {
        StepStatus status = intersection.step();
        assertEquals(List.of(), status.getLeftVehicles());
    }

    @Test
    void testAddAndGetVehicles() {
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("v2", Direction.WEST, Direction.EAST);

        intersection.addVehicle(v1);
        intersection.addVehicle(v2);

        List<Vehicle> vehicles = intersection.getVehicles();
        assertEquals(2, vehicles.size());
        assertTrue(vehicles.contains(v1));
        assertTrue(vehicles.contains(v2));
    }

    @Test
    void testStepRemovesVehicle() {
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        intersection.addVehicle(v1);

        StepStatus status = intersection.step();
        assertEquals(List.of("v1"), status.getLeftVehicles());
        assertTrue(intersection.getVehicles().isEmpty());
    }

    @Test
    void testStepDoesNothingIfNoVehicles() {
        StepStatus status = intersection.step();
        assertTrue(status.getLeftVehicles().isEmpty());
    }

    @Test
    void testFailureModeEnablesClockwiseFlow() {
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("v2", Direction.WEST, Direction.EAST);
        Vehicle v3 = new Vehicle("v3", Direction.SOUTH, Direction.NORTH);

        intersection.addVehicles(List.of(v1, v2, v3));
        intersection.setFailureMode(true);

        Set<String> exited = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            StepStatus status = intersection.step();
            exited.addAll(status.getLeftVehicles());
        }

        assertEquals(Set.of("v1", "v2", "v3"), exited);
    }

    @Test
    void testRoadPriorityAffectsSorting() {
        IntersectionConflictResolver resolver = new FourWayIntersectionConflictResolver();

        FourWayIntersection customIntersection = new FourWayIntersection(resolver);
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("v2", Direction.EAST, Direction.WEST);

        customIntersection.addVehicles(List.of(v1, v2));
        customIntersection.setRoadPriority(Direction.EAST, 10);

        StepStatus status = customIntersection.step();
        assertEquals(List.of("v2"), status.getLeftVehicles());
    }

    @Test
    void testAddPedestrianReflectsInState() {
        assertTrue(intersection.getPedestrians().isEmpty());

        intersection.addPedestrian(Direction.WEST);
        List<Pedestrian> peds = intersection.getPedestrians();

        assertEquals(1, peds.size());
        assertEquals(Direction.WEST, peds.get(0).getDirection());
    }

    @Test
    void testRemoveSpecificVehicle() {
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("v2", Direction.NORTH, Direction.EAST);

        intersection.addVehicles(List.of(v1, v2));

        Vehicle removed = intersection.removeVehicle(v1);
        assertEquals(v1, removed);

        assertEquals(v2, intersection.getFrontVehicles().get(0));
    }
}
