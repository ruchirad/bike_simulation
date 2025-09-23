package com.bgl.bikesimulator.command;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.util.DebugLogger;

/**
 * Command handler to handle TURN_RIGHT move command.
 */
public class TurnRightCommandHandler extends MovementCommandHandler {
    public TurnRightCommandHandler() {
        super("TURN_RIGHT");
    }

    @Override
    public void executeCommand(Bike bike, Grid grid) {
        DebugLogger.debug(this.getHandlerName() + "Command processing...");
        bike.turnRight();
    }
}


