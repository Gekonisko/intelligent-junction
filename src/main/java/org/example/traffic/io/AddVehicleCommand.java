package org.example.traffic.io;

public class AddVehicleCommand extends Command {
    public String vehicleId;
    public String startRoad;
    public String endRoad;

    public AddVehicleCommand() {
        super("addVehicle");
    }

    public AddVehicleCommand(String vehicleId, String startRoad, String endRoad) {
        super("addVehicle");
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
    }

    @Override
    public String toString() {
        return "AddVehicleCommand{" +
                "vehicleId='" + vehicleId + '\'' +
                ", startRoad='" + startRoad + '\'' +
                ", endRoad='" + endRoad + '\'' +
                '}';
    }
}