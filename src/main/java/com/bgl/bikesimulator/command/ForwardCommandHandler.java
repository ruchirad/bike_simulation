package com.bgl.bikesimulator.command;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Direction;
import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.model.Position;
import com.bgl.bikesimulator.util.DebugLogger;

/**
 * Command handler to handle FORWARD move command.
 */
public class ForwardCommandHandler extends MovementCommandHandler {

    public ForwardCommandHandler() {
        super("FORWARD");
    }

    @Override
    public void executeCommand(Bike bike, Grid grid) {
        DebugLogger.debug(this.getHandlerName() + "Command processing...");

        Position currentPosition = bike.getPosition();
        Direction currentDirection = bike.getDirection();

        if(currentPosition != null) {
            Position newPosition = currentPosition.move(currentDirection);

            if (grid.isValidPosition(newPosition)) {
                bike.moveForward();
            }else {
                DebugLogger.debug(getClass(), "Invalid position in the Grid. Hence ignoring...");
            }
        }else {
            DebugLogger.debug(getClass(), "Currently not in a valid position. Hence ignoring...");
        }
        // Invalid moves are silently ignored per requirements
    }
}


