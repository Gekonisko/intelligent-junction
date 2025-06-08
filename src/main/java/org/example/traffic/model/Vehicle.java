package org.example.traffic.model;

public class Vehicle {
    private final String vehicleId;
    private final Direction from;
    private final Direction to;

    public Vehicle(String vehicleId, Direction from, Direction to) {
        this.vehicleId = vehicleId;
        this.from = from;
        this.to = to;
    }

    public Movement getMovement() {
        return switch (from) {
            case NORTH -> switch (to) {
                case EAST -> Movement.LEFT;
                case SOUTH -> Movement.STRAIGHT;
                case WEST -> Movement.RIGHT;
                default -> null;
            };
            case SOUTH -> switch (to) {
                case WEST -> Movement.LEFT;
                case NORTH -> Movement.STRAIGHT;
                case EAST -> Movement.RIGHT;
                default -> null;
            };
            case EAST -> switch (to) {
                case SOUTH -> Movement.LEFT;
                case WEST -> Movement.STRAIGHT;
                case NORTH -> Movement.RIGHT;
                default -> null;
            };
            case WEST -> switch (to) {
                case NORTH -> Movement.LEFT;
                case EAST -> Movement.STRAIGHT;
                case SOUTH -> Movement.RIGHT;
                default -> null;
            };
        };
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public Direction getFrom() {
        return from;
    }

    public Direction getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}