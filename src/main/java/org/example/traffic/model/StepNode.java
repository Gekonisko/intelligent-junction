package org.example.traffic.model;

import java.util.ArrayList;
import java.util.List;

public class StepNode {
    private StepNode parent;
    private List<Vehicle> leftVehicles ;
    private List<Pedestrian> leftPedestrians ;
    private List<StepNode> children;

    public StepNode(List<Vehicle> leftVehicles, List<Pedestrian> leftPedestrians) {
        this.parent = null;
        this.leftVehicles = leftVehicles;
        this.leftPedestrians = leftPedestrians;
        this.children = new ArrayList<StepNode>();
    }

    public int getFullSize() {
        return leftVehicles.size() + leftPedestrians.size();
    }

    public void addChild(StepNode child) {
        children.add(child);
    }

    public void setParent(StepNode parent) {
        this.parent = parent;
    }

    public StepNode getParent() {
        return parent;
    }

    public List<Vehicle> getLeftVehicles() {
        return leftVehicles;
    }

    public List<Pedestrian> getLeftPedestrians() {
        return leftPedestrians;
    }

    public List<StepNode> getChildren() {
        return children;
    }
}
