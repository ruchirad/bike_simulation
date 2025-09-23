package com.bgl.bikesimulator.command;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Grid;

/**
 * Command handler pattern interface for all simulator commands.
 */

@FunctionalInterface
public interface CommandHandler {

    void executeCommand(Bike bike, Grid grid);
}
