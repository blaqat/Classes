/**
 * This class will represent a piece that is able to be moved in a checker board
 * @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 */
package com.webcheckers.model;

public class Piece {
  private Type type;
  private Color color;
  private Position position;

  public Piece(Color color, Type type) {
    this.type = type;
    this.color = color;
  }

  public Piece(Color color, Position position) {
    this(color, Type.SINGLE);
    this.position = position;
  }

  public void king() {
    this.type = Type.KING;
  }

  public Type getType() {
    return type;
  }

  public Color getColor() {
    return color;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public boolean isKingPiece() {
    return this.type == Type.KING;
  }
}