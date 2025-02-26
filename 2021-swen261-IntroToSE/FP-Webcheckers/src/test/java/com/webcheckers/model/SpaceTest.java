package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier")
public class SpaceTest {
  @Test
  public void spaceInitialize(){
    Space space = new Space(3,4);

    assertEquals(3,space.getCellIdx());
    assertEquals(null,space.getPiece());
    assertEquals(false,space.hasPiece());
  }

  @Test
  public void spaceAddPiece(){
    Space space = new Space(3,4);
    Piece piece = new Piece(Color.RED,Type.SINGLE);
    space.setPiece(piece);

    assertEquals(true,space.hasPiece());
    assertEquals(false,space.isValid());
  }

  @Test
  public void spaceInitializeWWhite(){
    Space space = new Space(4,4);

    assertEquals(4,space.getCellIdx());
    assertEquals(null,space.getPiece());
    assertEquals(false,space.hasPiece());
  }

  @Test
  public void spaceAddPieceWWhite(){
    Space space = new Space(4,4);
    Piece piece = new Piece(Color.WHITE,Type.SINGLE);
    space.setPiece(piece);

    assertEquals(true,space.hasPiece());
    assertEquals(false,space.isValid());
  }
}
