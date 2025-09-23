package com.bgl.bikesimulator.model;

/**
 * Enumerator represents the four directions.
 * Uses a mathematical trick to avoid negatives by using % operator.
 */
public enum Direction {
    NORTH(0, 1),  // ordinal() = 0
    EAST(1, 0),   // ordinal() = 1
    SOUTH(0, -1), // ordinal() = 2
    WEST(-1, 0);  // ordinal() = 3

    private final int deltaX;
    private final int deltaY;

//    Direction() {
//    }

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public Direction turnRight(){
        int newOrdinal = (ordinal() + 1) % 4;
        return values()[newOrdinal];
    }

    public Direction turnLeft(){
        return values()[(ordinal() + 3) % 4];
    }


}
