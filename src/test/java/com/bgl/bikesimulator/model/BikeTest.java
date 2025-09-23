package com.bgl.bikesimulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BikeTest {
    private Bike bike;

    @BeforeEach
    void setUp() {
        bike = new Bike();
    }

    @Test
    @DisplayName("New bike should not be placed initially")
    void testNewBikeNotPlaced() {
        assertFalse(bike.isPlaced());
    }

    @Test
    @DisplayName("Once bike is placed correctly")
    void testPlaceBike() {
        Position position = new Position(2, 3);
        Direction direction = Direction.NORTH;

        bike.place(position, direction);

        assertTrue(bike.isPlaced());
        assertEquals(position, bike.getPosition());
        assertEquals(direction, bike.getDirection());
    }

    @Test
    @DisplayName("Bike >> move forward correctly, after positioning")
    void testMoveForward() {
        bike.place(new Position(2, 2), Direction.NORTH);

        bike.moveForward();

        assertEquals(new Position(2, 3), bike.getPosition());
        assertEquals(Direction.NORTH, bike.getDirection());
    }

    @Test
    @DisplayName("Bike >> turn left correctly, after positioning")
    void testTurnLeft() {
        bike.place(new Position(2, 2), Direction.NORTH);

        bike.turnLeft();

        assertEquals(new Position(2, 2), bike.getPosition()); // Position unchanged
        assertEquals(Direction.WEST, bike.getDirection());
    }

    @Test
    @DisplayName("Bike >> turn right correctly, after positioning")
    void testTurnRight() {
        bike.place(new Position(2, 2), Direction.NORTH);

        bike.turnRight();

        assertEquals(new Position(2, 2), bike.getPosition()); // Position unchanged
        assertEquals(Direction.EAST, bike.getDirection());
    }

    @Test
    @DisplayName("Status report should be formatted correctly")
    void testStatusReport() {
        bike.place(new Position(3, 4), Direction.SOUTH);

        String report = bike.getStatusReport();

        assertEquals("(3, 4),SOUTH", report);
    }

}
