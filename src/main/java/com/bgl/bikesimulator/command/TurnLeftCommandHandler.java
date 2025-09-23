package com.bgl.bikesimulator.command;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.util.DebugLogger;

/**
 * Command handler to handle TURN_LEFT move command.
 */
public class TurnLeftCommandHandler extends MovementCommandHandler {
    public TurnLeftCommandHandler() {
        super("TURN_LEFT");
    }

    @Override
    public void executeCommand(Bike bike, Grid grid) {
        DebugLogger.debug(this.getHandlerName() + "Command processing...");
        bike.turnLeft();
    }
}


