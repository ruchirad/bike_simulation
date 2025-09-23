package com.bgl.bikesimulator.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void testPositionCreation() {
        Position position = new Position(3, 4);
        assertEquals(3, position.getX());
        assertEquals(4, position.getY());
    }

    @Test
    void testPositionMovement() {
        Position position = new Position(2, 2);
        Position moved = position.move(Direction.NORTH);

        assertEquals(new Position(2, 3), moved);
        assertEquals(new Position(2, 2), position); // Original unchanged
    }

}
