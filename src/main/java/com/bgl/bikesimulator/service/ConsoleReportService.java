package com.bgl.bikesimulator.service;

import java.io.PrintStream;

/**
 * Console implementation of the output service.
 */
public class ConsoleReportService implements ReportService {
    private final PrintStream outputStream;
    private final PrintStream errorStream;

    public ConsoleReportService() {
        this(System.out, System.err);
    }

    public ConsoleReportService(PrintStream outputStream, PrintStream errorStream) {
        this.outputStream = outputStream;
        this.errorStream = errorStream;
    }

    @Override
    public void printReport(String report) {
        outputStream.println("------------------\n" + "GPS_REPORT:");
        outputStream.println("\t" + report + "\n------------------");
    }

    @Override
    public void printError(String error) {
        errorStream.println("Error: " + error);
    }
}
