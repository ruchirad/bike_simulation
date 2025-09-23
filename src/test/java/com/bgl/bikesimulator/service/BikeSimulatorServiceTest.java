package com.bgl.bikesimulator.service;

import com.bgl.bikesimulator.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BikeSimulatorServiceTest {

    private BikeSimulatorService service;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        service = new BikeSimulatorService();

        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // ---------- HAPPY CASES ----------

    @Test
    @DisplayName("EX_1: PLACE 0,5,NORTH -> FORWARD -> GPS_REPORT should result in (0,6), NORTH")
    void testExample1() {
        service.executeCommand("PLACE 0,5,NORTH");
        service.executeCommand("FORWARD");

        assertTrue(service.getBike().isPlaced());
        assertEquals(new Position(0, 6), service.getBike().getPosition());
        assertEquals(Direction.NORTH, service.getBike().getDirection());
    }

    @Test
    @DisplayName("EX_2: Complex movement sequence should result in (3,3), NORTH")
    void testExample3() {
        // Given
        service.executeCommand("PLACE 1,2,EAST");
        service.executeCommand("FORWARD");     // (2,2), EAST
        service.executeCommand("FORWARD");     // (3,2), EAST
        service.executeCommand("TURN_LEFT");   // (3,2), NORTH
        service.executeCommand("FORWARD");     // (3,3), NORTH

        // When & Then
        assertTrue(service.getBike().isPlaced());
        assertEquals(new Position(3, 3), service.getBike().getPosition());
        assertEquals(Direction.NORTH, service.getBike().getDirection());
    }

    // ---------- PLACE COMMAND TESTS --------------------

    @ParameterizedTest
    @DisplayName("PLACE: Valid PLACE commands should work correctly")
    @MethodSource("provideValidPlaceCommands")
    void testValidPlaceCommands(String command, Position expectedPosition, Direction expectedDirection) {
        service.executeCommand(command);

        assertTrue(service.getBike().isPlaced());
        assertEquals(expectedPosition, service.getBike().getPosition());
        assertEquals(expectedDirection, service.getBike().getDirection());
    }

    static Stream<Arguments> provideValidPlaceCommands() {
        return Stream.of(
                Arguments.of("PLACE 0,0,NORTH", new Position(0, 0), Direction.NORTH),
                Arguments.of("PLACE 6,6,SOUTH", new Position(6, 6), Direction.SOUTH),
                Arguments.of("PLACE 3,3,EAST", new Position(3, 3), Direction.EAST),
                Arguments.of("PLACE 2,4,WEST", new Position(2, 4), Direction.WEST),
                Arguments.of("place 1,1,north", new Position(1, 1), Direction.NORTH) // Case insensitive
        );
    }

    @ParameterizedTest
    @DisplayName("PLACE: Invalid PLACE commands should be ignored")
    @ValueSource(strings = {
            "PLACE 7,0,NORTH",     // Outside grid (x)
            "PLACE 0,7,NORTH",     // Outside grid (y)
            "PLACE -1,0,NORTH",    // Negative x
            "PLACE 0,-1,NORTH",    // Negative y
            "PLACE 0,0,INVALID",   // Invalid direction
            "PLACE 0,0",           // Missing direction
            "PLACE",               // Missing all params
            "PLACE abc,0,NORTH",   // Invalid x
            "PLACE 0,abc,NORTH"    // Invalid y
    })
    void testInvalidPlaceCommands(String command) {
        service.executeCommand(command);

        assertFalse(service.getBike().isPlaced());
    }

    @Test
    @DisplayName("PLACE: Multiple PLACE commands should work")
    void testMultiplePlaceCommands() {
        // First placement
        service.executeCommand("PLACE 0,0,NORTH");
        assertEquals(new Position(0, 0), service.getBike().getPosition());
        assertEquals(Direction.NORTH, service.getBike().getDirection());

        // Second placement should overwrite
        service.executeCommand("PLACE 5,5,SOUTH");
        assertEquals(new Position(5, 5), service.getBike().getPosition());
        assertEquals(Direction.SOUTH, service.getBike().getDirection());
    }

    @Test
    @DisplayName("PLACE: Commands before PLACE should be ignored")
    void testCommandsBeforePlaceIgnored() {
        // Execute various commands before PLACE
        service.executeCommand("FORWARD");
        service.executeCommand("TURN_LEFT");
        service.executeCommand("TURN_RIGHT");
        service.executeCommand("GPS_REPORT");

        // Bike should still not be placed
        assertFalse(service.getBike().isPlaced());

        // Now place the bike
        service.executeCommand("PLACE 2,2,NORTH");

        // Ths should work now
        assertTrue(service.getBike().isPlaced());
        assertEquals(new Position(2, 2), service.getBike().getPosition());
        assertEquals(Direction.NORTH, service.getBike().getDirection());
    }

    // ---------- MOVEMENT TESTS --------------------

    @Test
    @DisplayName("FORWARD: FORWARD command should move bike in current direction")
    void testForwardMovement() {
        service.executeCommand("PLACE 3,3,NORTH");
        service.executeCommand("FORWARD");

        assertEquals(new Position(3, 4), service.getBike().getPosition());
        assertEquals(Direction.NORTH, service.getBike().getDirection());
    }

    @ParameterizedTest
    @DisplayName("FORWARD: FORWARD should be blocked at grid boundaries")
    @MethodSource("provideBoundaryTestCases")
    void testForwardBlockedAtBoundaries(String placeCommand, Position expectedFinalPosition) {
        service.executeCommand(placeCommand);
        Position initialPosition = service.getBike().getPosition();

        service.executeCommand("FORWARD"); // Should be blocked

        assertEquals(expectedFinalPosition, service.getBike().getPosition());
        assertEquals(initialPosition.toString(), expectedFinalPosition.toString()); // Should not have moved
    }

    static Stream<Arguments> provideBoundaryTestCases() {
        return Stream.of(
                Arguments.of("PLACE 0,6,NORTH", new Position(0, 6)),   // Top boundary
                Arguments.of("PLACE 6,0,EAST", new Position(6, 0)),    // Right boundary
                Arguments.of("PLACE 0,0,SOUTH", new Position(0, 0)),   // Bottom boundary
                Arguments.of("PLACE 0,0,WEST", new Position(0, 0))     // Left boundary
        );
    }

    // ---------- ROTATION TESTS --------------------

    @ParameterizedTest
    @DisplayName("ROTATION: TURN_LEFT should rotate direction correctly")
    @MethodSource("provideTurnLeftTestCases")
    void testTurnLeft(Direction initial, Direction expected) {
        service.executeCommand(String.format("PLACE 3,3,%s", initial));
        service.executeCommand("TURN_LEFT");

        assertEquals(expected, service.getBike().getDirection());
        assertEquals(new Position(3, 3), service.getBike().getPosition()); // Position unchanged
    }

    static Stream<Arguments> provideTurnLeftTestCases() {
        return Stream.of(
                Arguments.of(Direction.NORTH, Direction.WEST),
                Arguments.of(Direction.WEST, Direction.SOUTH),
                Arguments.of(Direction.SOUTH, Direction.EAST),
                Arguments.of(Direction.EAST, Direction.NORTH)
        );
    }

    @ParameterizedTest
    @DisplayName("ROTATION: TURN_RIGHT should rotate direction correctly")
    @MethodSource("provideTurnRightTestCases")
    void testTurnRight(Direction initial, Direction expected) {
        service.executeCommand(String.format("PLACE 3,3,%s", initial));
        service.executeCommand("TURN_RIGHT");

        assertEquals(expected, service.getBike().getDirection());
        assertEquals(new Position(3, 3), service.getBike().getPosition()); // Position unchanged
    }

    static Stream<Arguments> provideTurnRightTestCases() {
        return Stream.of(
                Arguments.of(Direction.NORTH, Direction.EAST),
                Arguments.of(Direction.EAST, Direction.SOUTH),
                Arguments.of(Direction.SOUTH, Direction.WEST),
                Arguments.of(Direction.WEST, Direction.NORTH)
        );
    }

    // ---------- INVALID COMMANDS TESTS --------------------

    @ParameterizedTest
    @DisplayName("INVALID_CMD: Invalid commands should be silently ignored")
    @ValueSource(strings = {
            "INVALID_COMMAND",
            "MOVE",
            "ROTATE",
            "",
            "   ",
            "PLACE INVALID FORMAT",
            "123456"
    })
    void testInvalidCommandsIgnored(String invalidCommand) {
        // Place bike first
        service.executeCommand("PLACE 3,3,NORTH");
        Position originalPosition = service.getBike().getPosition();
        Direction originalDirection = service.getBike().getDirection();

        // Execute invalid command
        service.executeCommand(invalidCommand);

        // State should be unchanged
        assertEquals(originalPosition, service.getBike().getPosition());
        assertEquals(originalDirection, service.getBike().getDirection());
    }

    // ---------- INPUT STREAM TESTS --------------------

    @Test
    @DisplayName("STREAM: processInputStream should handle multiple commands")
    void testProcessInputStream() {
        String commands = "PLACE 0,0,NORTH\nFORWARD\nTURN_RIGHT\nFORWARD\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(commands.getBytes());

        service.processInputStream(inputStream);

        assertEquals(new Position(1, 1), service.getBike().getPosition());
        assertEquals(Direction.EAST, service.getBike().getDirection());
    }

    @Test
    @DisplayName("STREAM: processInputStream should handle empty input gracefully")
    void testProcessInputStreamEmpty() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());

        assertDoesNotThrow(() -> service.processInputStream(inputStream));
        assertFalse(service.getBike().isPlaced());
    }

    @Test
    @DisplayName("STREAM: processInputStream should handle mixed valid and invalid commands")
    void testProcessInputStreamMixedCommands() {
        String commands = "INVALID\nPLACE 1,1,NORTH\nINVALID_MOVE\nFORWARD\nBAD_COMMAND\nTURN_LEFT\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(commands.getBytes());

        service.processInputStream(inputStream);

        assertEquals(new Position(1, 2), service.getBike().getPosition());
        assertEquals(Direction.WEST, service.getBike().getDirection());
    }

    // ---------- FILE PROCESSING TESTS --------------------

    @Test
    @DisplayName("FILE: processFile should handle valid file")
    void testProcessFileValid() throws IOException {
        // Create temporary file
        Path tempFile = Files.createTempFile("bike_commands", ".txt");
        String commands = "PLACE 2,2,EAST\nFORWARD\nTURN_LEFT\nFORWARD\n";
        Files.write(tempFile, commands.getBytes());

        try {
            service.processFile(tempFile.toString());

            assertEquals(new Position(3, 3), service.getBike().getPosition());
            assertEquals(Direction.NORTH, service.getBike().getDirection());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @DisplayName("FILE: processFile should throw IOException for non-existent file")
    void testProcessFileNonExistent() {
        assertThrows(IOException.class, () -> {
            service.processFile("non_existent_file.txt");
        });
    }

    // ---------- EDGE CASES --------------------

    @Test
    @DisplayName("EDGE: Null command should be handled gracefully")
    void testNullCommand() {
        assertDoesNotThrow(() -> service.executeCommand(null));
        assertFalse(service.getBike().isPlaced());
    }

    @Test
    @DisplayName("EDGE: Complex sequence with boundaries and invalid moves")
    void testComplexSequenceWithBoundaries() {
        service.executeCommand("PLACE 6,6,NORTH");  // Top-right corner
        service.executeCommand("FORWARD");          // Should be ignored (would exit grid)
        service.executeCommand("TURN_RIGHT");       // Now facing EAST
        service.executeCommand("FORWARD");          // Should be ignored (would exit grid)
        service.executeCommand("TURN_RIGHT");       // Now facing SOUTH
        service.executeCommand("FORWARD");          // Should work (6,5)

        assertEquals(new Position(6, 5).toString(), service.getBike().getPosition().toString());
        assertEquals(Direction.SOUTH, service.getBike().getDirection());
    }

    @Test
    @DisplayName("EDGE: Case insensitive commands should work")
    void testCaseInsensitiveCommands() {
        service.executeCommand("place 1,1,north");
        service.executeCommand("forward");
        service.executeCommand("turn_left");
        service.executeCommand("turn_right");

        assertEquals(new Position(1, 2), service.getBike().getPosition());
        assertEquals(Direction.NORTH, service.getBike().getDirection());
    }

}