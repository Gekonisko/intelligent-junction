package org.example.traffic.io;

public class AddPedestrianCommand extends Command{
    public String road;

    public AddPedestrianCommand() {
        super("addPedestrian");
    }

    public AddPedestrianCommand(String road) {
        super("addPedestrian");
        this.road = road;
    }
}
