/**
 * A single space that may contain a piece and the cell id number
 * @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 */
package com.webcheckers.model;

public class Space {
  private int cellIdx;
  private Piece piece;
  private boolean isWhite;

  public Space(int cellIdx, int rowIdx) {
    this.cellIdx = cellIdx;
    piece = null;

    if ((rowIdx + cellIdx) % 2 == 0)
      isWhite = true;
  }

  public int getCellIdx() {
    return this.cellIdx;
  }

  /**
   * Will return true if the space is a valid location to
   * place a piece; this is a dark square and has no other
   * piece on it
   * @return boolean: space can be placed on
   * */
  public boolean isValid() {
    return !isWhite && piece == null;
  }

  /**
   * returns the piece that my be occupying the space
   * @return Piece
   **/
  public int setPiece(Piece p) {
    this.piece = p;
    return 0;
  }

  public Piece removePiece() {
    Piece result = piece;
    piece = null;

    return result;
  }
  public Piece getPiece() {
    return this.piece;
  }

  public boolean hasPiece() {
    return this.piece != null;
  }

  public Color getColor() {
    return isWhite ? Color.WHITE : Color.BLACK;
  }

}