package org.example.traffic.io;

public class RoadPriorityCommand extends Command {
    public String road;
    public int priority;

    public RoadPriorityCommand() {
        super("roadPriority");
    }

    public RoadPriorityCommand(String road, int priority) {
        super("roadPriority");
        this.road = road;
        this.priority = priority;
    }
}
