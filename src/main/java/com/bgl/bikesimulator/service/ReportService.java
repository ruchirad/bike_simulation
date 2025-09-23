package com.bgl.bikesimulator.service;

/**
 * Service interface for handling output operations.
 * Different implementation can handle different outputs (console, file, json, etc.).
 */
public interface ReportService {

    void printReport(String report);
    void printError(String error);
}
