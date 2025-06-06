package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FourWayIntersectionConflictResolverTest {
    private final IntersectionConflictResolver resolver = new FourWayIntersectionConflictResolver();

    @Test
    void testIsConflict_LeftMovement_NoConflict() {
        Vehicle v1 = new Vehicle("A", Direction.SOUTH, Direction.WEST);

        List<Vehicle> v2 = List.of(
                new Vehicle("A", Direction.NORTH, Direction.EAST),
                new Vehicle("A", Direction.WEST, Direction.SOUTH),
                new Vehicle("A", Direction.EAST, Direction.NORTH)
        );

        for (Vehicle v : v2) {
            var conflict = resolver.isConflict(v, v1);
            if (conflict) {
                System.out.println(v + " - " + v1);
            }
            assertFalse(conflict);
        }
    }

    @Test
    void testIsConflict_LeftMovement_Conflict() {
        Vehicle v1 = new Vehicle("A", Direction.SOUTH, Direction.WEST);

        List<Vehicle> v2 = List.of(
                new Vehicle("A", Direction.WEST, Direction.EAST),
                new Vehicle("B", Direction.WEST, Direction.NORTH),
                new Vehicle("C", Direction.EAST, Direction.SOUTH),
                new Vehicle("C", Direction.EAST, Direction.WEST),
                new Vehicle("C", Direction.NORTH, Direction.WEST),
                new Vehicle("C", Direction.NORTH, Direction.SOUTH),
                new Vehicle("C", Direction.SOUTH, Direction.NORTH)
        );

        for (Vehicle v : v2) {
            var conflict = resolver.isConflict(v, v1);
            if (!conflict) {
                System.out.println(v + " - " + v1);
            }
            assertTrue(conflict);
        }
    }

    @Test
    void testIsConflict_RightMovement_NoConflict() {
        Vehicle v1 = new Vehicle("A", Direction.EAST, Direction.NORTH);

        List<Vehicle> v2 = List.of(
                new Vehicle("A", Direction.NORTH, Direction.EAST),
                new Vehicle("A", Direction.NORTH, Direction.SOUTH),
                new Vehicle("A", Direction.NORTH, Direction.WEST),
                new Vehicle("A", Direction.SOUTH, Direction.EAST),
                new Vehicle("A", Direction.SOUTH, Direction.WEST),
                new Vehicle("A", Direction.WEST, Direction.EAST),
                new Vehicle("A", Direction.WEST, Direction.SOUTH)
        );

        for (Vehicle v : v2) {
            var conflict = resolver.isConflict(v, v1);
            if (conflict) {
                System.out.println(v + " - " + v1);
            }
            assertFalse(conflict);
        }
    }

    @Test
    void testIsConflict_RightMovement_Conflict() {
        Vehicle v1 = new Vehicle("A", Direction.EAST, Direction.NORTH);

        List<Vehicle> v2 = List.of(
                new Vehicle("A", Direction.EAST, Direction.NORTH),
                new Vehicle("A", Direction.SOUTH, Direction.NORTH),
                new Vehicle("A", Direction.WEST, Direction.NORTH)
        );

        for (Vehicle v : v2) {
            var conflict = resolver.isConflict(v, v1);
            if (!conflict) {
                System.out.println(v + " - " + v1);
            }
            assertTrue(conflict);
        }
    }

    @Test
    void testIsConflict_StraightMovement_NoConflict() {
        Vehicle v1 = new Vehicle("A", Direction.NORTH, Direction.SOUTH);

        List<Vehicle> v2 = List.of(
                new Vehicle("A", Direction.SOUTH, Direction.NORTH),
                new Vehicle("A", Direction.SOUTH, Direction.EAST),
                new Vehicle("A", Direction.EAST, Direction.NORTH)
        );

        for (Vehicle v : v2) {
            var conflict = resolver.isConflict(v, v1);
            if (conflict) {
                System.out.println(v + " - " + v1);
            }
            assertFalse(conflict);
        }
    }

    @Test
    void testIsConflict_StraightMovement_Conflict() {
        Vehicle v1 = new Vehicle("A", Direction.NORTH, Direction.SOUTH);

        List<Vehicle> v2 = List.of(
                new Vehicle("A", Direction.NORTH, Direction.SOUTH),
                new Vehicle("A", Direction.EAST, Direction.WEST),
                new Vehicle("A", Direction.EAST, Direction.SOUTH),
                new Vehicle("A", Direction.SOUTH, Direction.WEST),
                new Vehicle("A", Direction.WEST, Direction.SOUTH),
                new Vehicle("A", Direction.WEST, Direction.NORTH),
                new Vehicle("A", Direction.WEST, Direction.EAST)
        );

        for (Vehicle v : v2) {
            var conflict = resolver.isConflict(v, v1);
            if (!conflict) {
                System.out.println(v + " - " + v1);
            }
            assertTrue(conflict);
        }
    }

    @Test
    void testNonConflictingGroup_returnsValidSet() {
        Vehicle v1 = new Vehicle("A", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("B", Direction.SOUTH, Direction.NORTH);
        Vehicle v3 = new Vehicle("C", Direction.EAST, Direction.WEST);
        Vehicle v4 = new Vehicle("D", Direction.WEST, Direction.EAST);

        List<Vehicle> vehicles = List.of(v1, v2, v3, v4);

        LinkedList<List<Vehicle>> groups = resolver.nonConflictingGroup(vehicles);

        assertNotNull(groups);
        assertFalse(groups.isEmpty());

        for (List<Vehicle> group : groups) {
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    assertFalse(resolver.isConflict(group.get(i), group.get(j)),
                            "Found conflict between " + group.get(i).vehicleId + " and " + group.get(j).vehicleId);
                }
            }
        }
    }

    @Test
    void testNonConflictingGroup_returnsMaxGroup() {
        Vehicle v1 = new Vehicle("A", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("B", Direction.EAST, Direction.WEST);
        Vehicle v3 = new Vehicle("C", Direction.SOUTH, Direction.EAST);
        Vehicle v4 = new Vehicle("D", Direction.WEST, Direction.SOUTH);

        List<Vehicle> vehicles = List.of(v1, v2, v3, v4);
        var groups = resolver.nonConflictingGroup(vehicles);

        var maxGroup = groups.stream().max(Comparator.comparingInt(List::size)).orElse(null);

        assertNotNull(maxGroup);
        assertEquals(3, maxGroup.size());
        for (int i = 0; i < maxGroup.size(); i++) {
            for (int j = i + 1; j < maxGroup.size(); j++) {
                assertFalse(resolver.isConflict(maxGroup.get(i), maxGroup.get(j)));
            }
        }
    }
}
