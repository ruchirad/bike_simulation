package com.bgl.bikesimulator.command;

import com.bgl.bikesimulator.model.Bike;
import com.bgl.bikesimulator.model.Grid;
import com.bgl.bikesimulator.service.ConsoleReportService;
import com.bgl.bikesimulator.service.ReportService;
import com.bgl.bikesimulator.util.DebugLogger;

/**
 * Command handler to handle GPS_REPORT command.
 * If the Bike is not placed properly, the command will be ignored.
 */
public class GpsReportCommandHandler extends MovementCommandHandler {

    ReportService reportService = new ConsoleReportService();

    public GpsReportCommandHandler() {
        super("GPS_REPORT");
    }

    @Override
    public void executeCommand(Bike bike, Grid grid) {
        generateReport(bike);
    }

    public void generateReport(Bike bike) {
        if (bike.isPlaced()) {
            reportService.printReport(bike.getStatusReport());
        }else {
            DebugLogger.debug(getClass(), "Bike is not placed yet");
        }
        // Ignore if not placed yet
    }
}

