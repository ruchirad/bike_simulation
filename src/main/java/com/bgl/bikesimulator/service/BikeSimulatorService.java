package com.bgl.bikesimulator.service;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.util.CommandUtil;
import com.bgl.bikesimulator.util.DebugLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * This works as a facade to the client, and orchestrate command handling and
 * coordinate between multiple components.
 * Further, this layer handles the business logic as well.
 */
public class BikeSimulatorService {

    private final CommandUtil parser;
    private final Bike bike;
    private final Grid grid;

    public BikeSimulatorService() {
        // We use a 7*7 Grid as the default grid
        this(new Grid(7,7));
    }

    public BikeSimulatorService(Grid grid) {
        this.grid = grid;
        this.bike = new Bike();
        this.parser = new CommandUtil();
    }

    public void processInputStream(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                executeCommand(command);
            }
        }
    }

    public void processFile(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            processInputStream(fileInputStream);
        }
    }

    public void executeCommand(String commandString) {
        DebugLogger.debug(getClass(), String.format("Processing command [%s] in BikeSimulatorService", commandString));
        parser.getCommandHandler(commandString)
                .ifPresent(command -> command.executeCommand(bike, grid));
        // Invalid commands are silently ignored per requirements
    }

    public Bike getBike() {
        return bike;
    }

    public Grid getGrid() {
        return grid;
    }

}
