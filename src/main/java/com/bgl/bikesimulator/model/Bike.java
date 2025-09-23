package com.bgl.bikesimulator.model;

import com.bgl.bikesimulator.util.DebugLogger;

/**
 * Represent the bike entity with its position and direction.
 * The entity will handle its own movements.
 */
public class Bike {
    private Position position;
    private Direction direction;
    private boolean isPlaced;

    public Bike() {
        this.isPlaced = false;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public Position getPosition(){
        return isPlaced ? position : null;
    }

    public Direction getDirection(){
        return isPlaced ? direction : null;
    }

    public void place(Position position, Direction direction){
        this.position = position;
        this.direction = direction;
        this.isPlaced = true;
        printMyLocation();
    }

    // Prerequisite - Place command has to happen before. Ignore otherwise
    public void moveForward(){
        if (isPlaced) {
            this.position = position.move(direction);
            printMyLocation();
        }else {
            printError();
        }
    }

    // Prerequisite - Place command has to happen before. Ignore otherwise
    public void turnLeft(){
        if (isPlaced) {
            this.direction = direction.turnLeft();
            printMyLocation();
        }else {
            printError();
        }
    }

    // Prerequisite - Place command has to happen before. Ignore otherwise
    public void turnRight(){
        if (isPlaced) {
            this.direction = direction.turnRight();
            printMyLocation();
        }else {
            printError();
        }
    }

    // Prerequisite - Place command has to happen before. Ignore otherwise
    public String getStatusReport(){
        if (isPlaced) {
            return String.format("%s,%s", position, direction);
        }
        return "Bike has not been placed yet";
    }

    private void printError(){
        DebugLogger.debug(getClass(), "Bike is not placed yet");
    }

    private void printMyLocation(){
        DebugLogger.debug(getClass(), String.format("Bike is placed[%s] at the position[%s] with direction[%s]",
                        isPlaced, position.toString(), direction.toString()));
    }
}
