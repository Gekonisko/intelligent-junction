package org.example.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class StepNode {
    public StepNode parent;
    public List<Vehicle> leftVehicles ;
    public List<StepNode> children;

    public StepNode(List<Vehicle> leftVehicles) {
        this.parent = null;
        this.leftVehicles = leftVehicles;
        this.children = new ArrayList<StepNode>();
    }

    public void addChild(StepNode child) {
        children.add(child);
    }

    public void setParent(StepNode parent) {
        this.parent = parent;
    }
}
