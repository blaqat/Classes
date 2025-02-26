package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier")
public class MoveTest {
    @Test
    public void testCreateMove(){
        Move move = new Move(new Position(1, 2), new Position(2, 3));
        assertEquals(1, move.getInitialPosition().getRow());
        assertEquals(2, move.getInitialPosition().getCell());
        assertEquals(2, move.getFinalPosition().getRow());
        assertEquals(3, move.getFinalPosition().getCell());
    }

    @Test
    public void testGetFlippedMove(){
        Move move = new Move(new Position(1, 2), new Position(2, 3));
        Move expected = new Move(new Position(6, 5), new Position(5, 4));
        Move actual = move.getFlipped();
        assertEquals(expected, actual);
    }

}
