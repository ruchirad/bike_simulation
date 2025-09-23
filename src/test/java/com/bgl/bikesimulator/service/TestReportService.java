package com.bgl.bikesimulator.service;

public class TestReportService implements ReportService {
    private String lastReport;
    private String lastError;

    @Override
    public void printReport(String report) {
        this.lastReport = "----" + report + "----";
    }

    @Override
    public void printError(String error) {
        this.lastError = "Error: " + error;
    }

    public String getLastReport() {
        return lastReport;
    }

    public String getLastError() {
        return lastError;
    }
}
