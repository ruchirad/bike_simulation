package com.bgl.bikesimulator.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testDirectionProperties(Direction direction) {
        assertNotNull(direction.getDeltaX());
        assertNotNull(direction.getDeltaY());
    }

    @Test
    void testTurnOperations() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());

        // Test full rotation
        Direction current = Direction.NORTH;
        for (int i = 0; i < 4; i++) {
            current = current.turnRight();
        }
        assertEquals(Direction.NORTH, current);
    }

    /**
     * Turn left should follow correct mathematical sequence
     * Looking for mathematical correction of >> values()[(ordinal() + 3) % 4]
     *
     *      NORTH(0, 1),  // ordinal() = 0
     *      EAST(1, 0),   // ordinal() = 1
     *      SOUTH(0, -1), // ordinal() = 2
     *      WEST(-1, 0);  // ordinal() = 3
     */
    @Test
    void testTurnLeftMathematicalSequence() {
        // NORTH (ordinal=0): (0 + 3) % 4 = 3 >> WEST
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());

        // EAST (ordinal=1): (1 + 3) % 4 = 0 >> NORTH
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());

        // SOUTH(ordinal= 2): (2 + 3) % 4 = 1 >> EAST
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());

        // WEST (ordinal=3) : (3 + 3) % 4 = 2 >> SOUTH
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
    }

    @Test
    void testWhyPlus3ForLeftTurn() {
        // Why adding 3 is equivalent to subtracting 1 in circular arithmetic (avoid -ves)
        for (int i = 0; i < 4; i++) {
            int leftTurnResult = (i + 3) % 4;
            int alternativeLeftTurn = (i - 1 + 4) % 4;

            assertEquals(alternativeLeftTurn, leftTurnResult);
        }
    }

    @Test
    void testModuloMathematics() {
        assertEquals(3, (0 + 3) % 4, "NORTH turnLeft calculation");
        assertEquals(0, (1 + 3) % 4, "EAST turnLeft calculation");
    }

    /**
     * Turn right should follow correct mathematical sequence
     * Looking for mathematical correction of >> values()[(ordinal() + 1) % 4]
     */
    @Test
    void testTurnRightMathematicalSequence() {
        // NORTH (ordinal=0) : (0 + 1) % 4 = 1 >> EAST
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());

        // EAST (ordinal=1) : (1 + 1) % 4 = 2 >> SOUTH
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());

        // SOUTH (ordinal=2) : (2 + 1) % 4 = 3 >> WEST
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());

        // WEST (ordinal=3) : (3 + 1) % 4 = 0 >> NORTH
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }


}