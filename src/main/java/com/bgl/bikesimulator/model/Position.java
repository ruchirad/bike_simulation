package com.bgl.bikesimulator.model;

/**
 * Immutable value object represents a position on the Grid.
 */
public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*
     *    Y ↑
     *      |   ↑
     *      | ← * →
     *      |   ↓
     * -----|----------> X
     *      |
     */
    public Position move(Direction dir) {
        switch (dir) {
            case NORTH: return new Position(x, y + 1);
            case EAST: return new Position(x + 1, y);
            case SOUTH: return new Position(x, y - 1);
            case WEST: return new Position(x - 1, y);
            default: throw new IllegalStateException("Unexpected: " + dir);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}