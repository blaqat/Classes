package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class PositionTest {
    private static final int row = 2;
    private static final int cell = 3;

    /*private static final int toosmall = -1;
    private static final int toobig = 11;*/

    @Test
    public void testValidConstruct() {

        Position pos = new Position(row, cell);

        assertEquals(row, pos.getRow());
        assertEquals(cell, pos.getCell());
    }

    @Test //Test ToString
    public void testToString() {
        Position pos = new Position(row, cell);
        assertEquals("Position{row=2, column=3}", pos.toString());
    }

    //Test equals() as well
    @Test
    public void testEquals() {
        Position pos1 = new Position(row, cell);
        Position pos2 = new Position(row, cell);
        Position pos3 = new Position(3, 6);


        //You don't need to mock the class under test. Mock is for simulating other objects needed for a test
        //here we can just create a few Position objects with no need for a mock


        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);

//        //when (t.getClass()).thenReturn((Class)Position.class);
//        when (t.getRow()).thenReturn(2);
//        when (t.getCell()).thenReturn(3);
//        assertTrue(pos.equals(t));
//
//        Piece o = mock(Piece.class);
//
//        //when (o.getClass()).thenReturn((Class)Piece.class);
//        assertFalse(pos.equals(o));
    }


}
