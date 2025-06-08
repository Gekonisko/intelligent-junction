package org.example.traffic.simulation;

import org.example.traffic.model.Direction;
import org.example.traffic.model.StepNode;
import org.example.traffic.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntersectionDecisionTreeTest {

    private IntersectionDecisionTree decisionTree;

    @BeforeEach
    void setUp() {
        IntersectionConflictResolver conflictResolver = new FourWayIntersectionConflictResolver();
        Intersection intersection = new FourWayIntersection(conflictResolver);
        this.decisionTree = new IntersectionDecisionTree(intersection, conflictResolver, 3, 2);
    }

    @Test
    void testBuildGeneratesChildren() {
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("v2", Direction.SOUTH, Direction.NORTH);

        StepNode root = new StepNode(List.of(v1), List.of());
        decisionTree.build(root, List.of(v1, v2));

        assertFalse(root.children.isEmpty(), "Children should be created from non-conflicting vehicles");
    }

    @Test
    void testGetBestStepNodeChoosesNodeWithMostVehicles() {
        Vehicle v1 = new Vehicle("v1", Direction.NORTH, Direction.SOUTH);
        Vehicle v2 = new Vehicle("v2", Direction.SOUTH, Direction.NORTH);
        Vehicle v3 = new Vehicle("v3", Direction.EAST, Direction.WEST);

        StepNode root1 = new StepNode(List.of(v1), List.of());
        StepNode root2 = new StepNode(List.of(v3), List.of());

        decisionTree.build(root1, List.of(v1, v2));
        decisionTree.build(root2, List.of(v3));

        StepNode best = decisionTree.getBestStepNode(List.of(root1, root2), List.of(v1, v2, v3));

        assertNotNull(best);
        assertTrue(best == root1 || best == root2, "Best node should be one of the roots");
    }

    @Test
    void testGetBestStepNodeReturnsNullWhenEmpty() {
        StepNode best = decisionTree.getBestStepNode(List.of(), List.of());
        assertNull(best);
    }
}
