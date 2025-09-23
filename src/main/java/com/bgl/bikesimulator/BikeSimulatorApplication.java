package com.bgl.bikesimulator;

import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.service.BikeSimulatorService;
import com.bgl.bikesimulator.util.DebugLogger;

import java.io.IOException;

public class BikeSimulatorApplication {

    public static final int GRID_WIDTH = 7;
    public static final int GRID_HEIGHT = 7;
    public static final boolean DEBUG_MODE = true;

    public static void main(String[] args) {
        Grid grid = new Grid(GRID_WIDTH,GRID_HEIGHT);
        BikeSimulatorService simulatorService = new BikeSimulatorService(grid);

        DebugLogger.setDebugMode(DEBUG_MODE);  // Toggle debugging

        try {
            if (args.length > 0) {
                // Process file input
                simulatorService.processFile(args[0]);
            } else {
                // Process standard input
                System.out.println("Bike Simulator - Enter commands:");
                System.out.println("Valid commands: \n\tPLACE X,Y,DIRECTION, " +
                        "\n\tFORWARD, \n\tTURN_LEFT, \n\tTURN_RIGHT, \n\tGPS_REPORT");
                System.out.println();
                simulatorService.processInputStream(System.in);
            }
        } catch (IOException e) {
            System.err.println("Error processing input: " + e.getMessage());
            System.exit(1);
        }
    }
}
