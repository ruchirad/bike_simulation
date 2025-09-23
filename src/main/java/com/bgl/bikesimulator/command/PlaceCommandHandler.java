package com.bgl.bikesimulator.command;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Direction;
import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.model.Position;
import com.bgl.bikesimulator.util.DebugLogger;

/**
 * Command handler to place the bike at a
 * specific position and direction in the grid.
 */
public class PlaceCommandHandler implements CommandHandler {
    private final Position position;
    private final Direction direction;

    public PlaceCommandHandler(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    @Override
    public void executeCommand(Bike bike, Grid grid) {
        if (grid.isValidPosition(position)) {
            DebugLogger.debug("PlaceCommandHandler Command processing...");
            bike.place(position, direction);
        }else {
            DebugLogger.debug("PlaceCommand got an invalid position - " + position.toString());
        }
    }


    @Override
    public String toString() {
        return String.format("PLACE %d,%d,%s",
                position.getX(), position.getY(), direction);
    }
}