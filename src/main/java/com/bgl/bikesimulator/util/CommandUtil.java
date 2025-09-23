package com.bgl.bikesimulator.util;

import com.bgl.bikesimulator.command.*;

import com.bgl.bikesimulator.model.Direction;
import com.bgl.bikesimulator.model.Position;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for converting string input into command objects.
 * Will return the correct CommandHandler based on the input string pattern.
 * Handles validation and error cases gracefully.
 */
public class CommandUtil {

    private static final Pattern PLACE_PATTERN =
            Pattern.compile("^PLACE\\s+(\\d+),(\\d+),(NORTH|SOUTH|EAST|WEST)$");

    public Optional<CommandHandler> getCommandHandler(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalizedInput = input.trim().toUpperCase();
        if (normalizedInput.startsWith("PLACE")) {
            return parsePlaceCommand(normalizedInput);
        }

        return parseMovementCommand(normalizedInput);
    }

    private Optional<CommandHandler> parsePlaceCommand(String input) {
        Matcher matcher = PLACE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            DebugLogger.debug(getClass(),
                String.format("Input[%s] doesnt match with the expected pattern[%s]", input, PLACE_PATTERN));
            return Optional.empty();
        }

        try {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            Direction direction = Direction.valueOf(matcher.group(3));

            return Optional.of(new PlaceCommandHandler(new Position(x, y), direction));
        } catch (IllegalArgumentException e) {
            DebugLogger.debug(getClass(), "Error Parsing the command." + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<CommandHandler> parseMovementCommand(String input) {
        return switch (input) {
            case "FORWARD" -> Optional.of(new ForwardCommandHandler());
            case "TURN_LEFT" -> Optional.of(new TurnLeftCommandHandler());
            case "TURN_RIGHT" -> Optional.of(new TurnRightCommandHandler());
            case "GPS_REPORT" -> Optional.of(new GpsReportCommandHandler());
            default -> Optional.empty();
        };
    }
}
