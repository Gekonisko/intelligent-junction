package org.example.traffic.simulation;

import org.example.traffic.model.StepNode;
import org.example.traffic.model.Vehicle;

import java.util.List;

public interface DecisionTree {
    void build(StepNode stepNode, List<Vehicle> vehicles);
    StepNode getBestStepNode(List<StepNode> stepNodes, List<Vehicle> vehicles);
}
