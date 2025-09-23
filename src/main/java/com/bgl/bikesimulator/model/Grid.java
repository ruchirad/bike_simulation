package com.bgl.bikesimulator.model;

/**
 * Represents the Grid and its boundaries.
 */
public class Grid {
    private final int width;
    private final int height;

    public Grid(int width, int height) {
        if(width <= 0 || height <= 0){
            throw new IllegalArgumentException("Grid dimension must be positive");
        }
        this.width = width;
        this.height = height;
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < width
                && position.getY() >= 0 && position.getY() < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("Grid[%dx%d]", width, height);
    }
}