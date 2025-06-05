package org.example.traffic.model;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction fromString(String str) {
        return Direction.valueOf(str.toUpperCase());
    }

    public static Direction oppositeOf(Direction direction) {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public static Direction rightOf(Direction direction) {
        return switch (direction) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public static Direction leftOf(Direction direction) {
        return switch (direction) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }
}