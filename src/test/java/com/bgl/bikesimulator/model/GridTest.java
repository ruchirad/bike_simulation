package com.bgl.bikesimulator.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private Grid defaultGrid;
    private Grid customGrid;

    @BeforeEach
    void setUp() {
        defaultGrid = new Grid(7, 7);  // Default 7x7 grid
        customGrid = new Grid(5, 10);  // Custom 5x10 grid, just in case
    }

    /**
     * Valid positions should return true for 7x7 grid
     */
    @ParameterizedTest
    @MethodSource("provideValidPositions7x7")
    void testValidPositions7x7(int x, int y, String description) {
        Position position = new Position(x, y);
        assertTrue(defaultGrid.isValidPosition(position),
                "Position " + position + " should be valid: " + description);
    }

    static Stream<Arguments> provideValidPositions7x7() {
        return Stream.of(
                // Corner positions
                Arguments.of(0, 0, "Bottom-left corner"),
                Arguments.of(6, 0, "Bottom-right corner"),
                Arguments.of(0, 6, "Top-left corner"),
                Arguments.of(6, 6, "Top-right corner"),

                // Edge positions
                Arguments.of(0, 3, "Left edge, middle"),
                Arguments.of(6, 3, "Right edge, middle"),
                Arguments.of(3, 0, "Bottom edge, middle"),
                Arguments.of(3, 6, "Top edge, middle"),

                // Center positions
                Arguments.of(3, 3, "Center of grid"),
                Arguments.of(1, 1, "Near bottom-left"),
                Arguments.of(5, 5, "Near top-right"),

                // All boundary positions
                Arguments.of(0, 1, "Left boundary"),
                Arguments.of(6, 1, "Right boundary"),
                Arguments.of(1, 0, "Bottom boundary"),
                Arguments.of(1, 6, "Top boundary")
        );
    }

    /**
     * Valid positions should work correctly for custom 5x10 grid
     */
    @ParameterizedTest
    @MethodSource("provideValidPositionsCustomGrid")
    void testValidPositionsCustomGrid(int x, int y, String description) {
        Position position = new Position(x, y);
        assertTrue(customGrid.isValidPosition(position),
                "Position " + position + " should be valid for 5x10 grid: " + description);
    }

    static Stream<Arguments> provideValidPositionsCustomGrid() {
        return Stream.of(
                // Corners for 5x10 grid (width=5, height=10, so valid range: 0-4, 0-9)
                Arguments.of(0, 0, "Bottom-left"),
                Arguments.of(4, 0, "Bottom-right"),
                Arguments.of(0, 9, "Top-left"),
                Arguments.of(4, 9, "Top-right"),

                // some middle positons
                Arguments.of(2, 5, "Center of 5x10 grid"),  //i guess?
                Arguments.of(2, 0, "Bottom edge center"),
                Arguments.of(2, 9, "Top edge center"),
                Arguments.of(0, 5, "Left edge center"),
                Arguments.of(4, 5, "Right edge center")
        );
    }

    /**
     * Grid should work correctly with bike simulator use cases
     */
    @Test
    void testBikeSimulatorIntegration() {
        assertTrue(defaultGrid.isValidPosition(new Position(0, 5)),
                "starting position should be valid");
        assertTrue(defaultGrid.isValidPosition(new Position(0, 6)),
                "forward should be valid");
    }

}