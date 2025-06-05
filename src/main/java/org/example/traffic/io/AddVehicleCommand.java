package org.example.traffic.io;

public class AddVehicleCommand extends Command {
    public String vehicleId;
    public String startRoad;
    public String endRoad;

    public AddVehicleCommand() {}

    public AddVehicleCommand(String vehicleId, String startRoad, String endRoad) {
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