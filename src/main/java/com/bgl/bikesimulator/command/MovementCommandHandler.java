package com.bgl.bikesimulator.command;

/**
 * Abstract base class for movement-related commands.
 * Mainly to get teh command handler name for logging.
 */
public abstract class MovementCommandHandler implements CommandHandler {
    protected final String commandName;

    protected MovementCommandHandler(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public String toString() {
        return commandName;
    }

    protected String getHandlerName() {
        return commandName;
    }
}