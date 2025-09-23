package com.bgl.bikesimulator.util;

/**
 * Simple static debug logger that can be toggled on/off globally.
 * Easy to use throughout the application.
 */
public class DebugLogger {
    private static boolean debugMode = false;

    public static void setDebugMode(boolean enabled) {
        debugMode = enabled;
        if (enabled) {
            System.out.println("[DEBUG] Debug mode ENABLED");
        }
    }

    public static boolean isDebugEnabled() {
        return debugMode;
    }

    public static void debug(String message) {
        if (debugMode) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public static void debug(Class<?> clazz, String message) {
        if (debugMode) {
            System.out.println("[DEBUG] " + clazz.getSimpleName() + ": " + message);
        }
    }
}