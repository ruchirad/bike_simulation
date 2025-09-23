package com.bgl.bikesimulator.util;

import com.bgl.bikesimulator.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CommandUtilTest {
    private CommandUtil util;

    @BeforeEach
    void setUp() {
        util = new CommandUtil();
    }

    @Test
    @DisplayName("Valid PLACE command should be parsed correctly")
    void testValidPlaceCommand() {
        Optional<CommandHandler> result = util.getCommandHandler("PLACE 2,3,NORTH");

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof PlaceCommandHandler);
    }

    @ParameterizedTest
    @DisplayName("Valid simple commands should be parsed correctly")
    @ValueSource(strings = {"FORWARD", "TURN_LEFT", "TURN_RIGHT", "GPS_REPORT"})
    void testValidSimpleCommands(String commandString) {
        Optional<CommandHandler> result = util.getCommandHandler(commandString);

        assertTrue(result.isPresent());

        CommandHandler command = result.get();
        switch (commandString) {
            case "FORWARD":
                assertTrue(command instanceof ForwardCommandHandler);
                break;
            case "TURN_LEFT":
                assertTrue(command instanceof TurnLeftCommandHandler);
                break;
            case "TURN_RIGHT":
                assertTrue(command instanceof TurnRightCommandHandler);
                break;
            case "GPS_REPORT":
                assertTrue(command instanceof GpsReportCommandHandler);
                break;
        }
    }

    @Test
    @DisplayName("Case insensitive commands should work")
    void testCaseInsensitiveCommands() {
        assertTrue(util.getCommandHandler("forward").isPresent());
        assertTrue(util.getCommandHandler("FORWARD").isPresent());
        assertTrue(util.getCommandHandler("Forward").isPresent());
        assertTrue(util.getCommandHandler("place 1,2,north").isPresent());
        assertTrue(util.getCommandHandler("PLACE 1,2,NORTH").isPresent());
    }

    @ParameterizedTest
    @DisplayName("Invalid commands should return empty Optional")
    @ValueSource(strings = {
            "INVALID_COMMAND",
            "MOVE",
            "ROTATE",
            "PLACE",
            "PLACE 1,2",
            "PLACE 1,2,INVALID_DIRECTION",
            "PLACE abc,2,NORTH",
            "PLACE 1,abc,NORTH",
            "",
            "   "
    })
    void testInvalidCommands(String invalidCommand) {
        Optional<CommandHandler> result = util.getCommandHandler(invalidCommand);

        assertFalse(result.isPresent(),
                "Command '" + invalidCommand + "' should be invalid");
    }

}
