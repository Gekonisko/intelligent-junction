package org.example.traffic.simulation;

import org.example.traffic.model.StepNode;
import org.example.traffic.model.Vehicle;

import java.util.*;

public class IntersectionDecisionTree implements DecisionTree {
    private final Intersection intersection;
    private final IntersectionConflictResolver conflictResolver;
    private final int maxDepth;
    private final int simultaneousDecisions;

    public IntersectionDecisionTree(Intersection intersection, IntersectionConflictResolver conflictResolver, int maxDepth, int simultaneousDecisions) {
        this.intersection = intersection;
        this.conflictResolver = conflictResolver;
        this.maxDepth = maxDepth;
        this.simultaneousDecisions = simultaneousDecisions;
    }

    @Override
    public void build(StepNode stepNode, List<Vehicle> vehicles) {
        Intersection intersection = this.intersection.createNewIntersection();
        intersection.addVehicles(vehicles);
        intersection.removeVehicles(stepNode.leftVehicles);

        var frontVehicles = intersection.getFrontVehicles();
        var nonConflict = conflictResolver.nonConflictingGroup(frontVehicles);

        for (int i = 0 ; i < simultaneousDecisions ; i++) {
            if(nonConflict.isEmpty()) break;

            var targetGroup = nonConflict.poll();
            StepNode childNode = new StepNode(targetGroup);
            childNode.parent = stepNode;
            stepNode.children.add(childNode);

            build(childNode, intersection.getVehicles(), 1);
        }
    }

    private void build(StepNode stepNode, List<Vehicle> vehicles, int depth) {
        if(depth >= maxDepth) return;

        Intersection intersection = this.intersection.createNewIntersection();
        intersection.addVehicles(vehicles);
        intersection.removeVehicles(stepNode.leftVehicles);

        var frontVehicles = intersection.getFrontVehicles();
        var nonConflict = conflictResolver.nonConflictingGroup(frontVehicles);

        for (int i = 0 ; i < simultaneousDecisions ; i++) {
            if(nonConflict.isEmpty()) break;

            var targetGroup = nonConflict.poll();
            StepNode childNode = new StepNode(targetGroup);
            childNode.parent = stepNode;
            stepNode.children.add(childNode);

            build(childNode, intersection.getVehicles(), depth + 1);
        }
    }

    @Override
    public StepNode getBestStepNode(List<StepNode> stepNodes, List<Vehicle> vehicles) {
        if (stepNodes.isEmpty()) return null;

        for (StepNode node : stepNodes) {
            build(node, vehicles);
        }

        StepNode best = null;
        int maxPassed = Integer.MIN_VALUE;
        int bestDepth = Integer.MAX_VALUE;

        for (StepNode node : stepNodes) {
            int[] maxVehicles = new int[1];
            int[] minDepth = new int[] { Integer.MAX_VALUE };
            dfsCount(node, maxVehicles, minDepth);

            if (maxVehicles[0] > maxPassed ||
                    (maxVehicles[0] == maxPassed && minDepth[0] < bestDepth)) {
                maxPassed = maxVehicles[0];
                bestDepth = minDepth[0];
                best = node;
            }
        }

        return best;
    }

    @Override
    public int getSimultaneousDecisions() {
        return simultaneousDecisions;
    }

    private void dfsCount(StepNode node, int[] maxVehicles, int[] minDepth) {
        int totalFromRoot = 0;
        int currentDepth = 0;

        StepNode temp = node;
        while (temp != null) {
            totalFromRoot += (temp.leftVehicles != null) ? temp.leftVehicles.size() : 0;
            currentDepth++;
            temp = temp.parent;
        }

        if (totalFromRoot > maxVehicles[0] ||
                (totalFromRoot == maxVehicles[0] && currentDepth < minDepth[0])) {
            maxVehicles[0] = totalFromRoot;
            minDepth[0] = currentDepth;
        }

        for (StepNode child : node.children) {
            dfsCount(child, maxVehicles, minDepth);
        }
    }
}
