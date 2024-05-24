/**
 * This class will represent how the player will be able to see the board, with the
 * pieces and spaces that occupy the board.
 * @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 */
package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardView implements Iterable<Row> {
  private Row[] rows;
  private final int size = 8;
  private ArrayList<Piece> redPieces;
  private ArrayList<Piece> whitePieces;
  private MoveValidator moveValidator;

  public BoardView() {
    //Initialize rows parameter with a capacity of 8
    this.rows = new Row[size];
    redPieces = new ArrayList<Piece> ();
    whitePieces = new ArrayList<Piece> ();

    //Inserts 8 rows into Board
    for (int i = 0; i<size; i++) {
      rows[i] = new Row(i);
    }
    initializeBoard();
  }

  public BoardView(MoveValidator moveValidator) {
    this();
    this.moveValidator = moveValidator;
  }

  public BoardView(Row[] boardCopy, ArrayList<Piece> redPieces, ArrayList<Piece> whitePieces, MoveValidator moveValidator) {
    this.rows = boardCopy;
    this.redPieces = redPieces;
    this.whitePieces = whitePieces;
    this.moveValidator = moveValidator;
  }

  private void initializeBoard() {

    for (int r = 0; r<8; r++) {
      Row currentRow = rows[r];
      for (int c = 0; c<8; c++) {
        Space currentSpace = currentRow.getSpace(c);

        if (r<3 || r > 4) {

          Color pieceColor = (r<4) ? Color.WHITE : Color.RED;

          if ((r % 2 == 0 && c % 2 == 1) || (r % 2 == 1 && c % 2 == 0)) {

            Piece piece = new Piece(pieceColor, new Position(r, c));

            if (pieceColor == Color.WHITE)
              whitePieces.add(piece);
            else
              redPieces.add(piece);

            currentSpace.setPiece(piece);

          }

        }

      }

    }

  }
  public void preset(int value) {
    ArrayList<Position> nr = new ArrayList<Position> ();
    ArrayList<Position> nw = new ArrayList<Position> ();
    ArrayList<Position> nk = new ArrayList<Position> ();

    for (Row row: rows) {
      for (Space space: row) {
        space.setPiece(null);
      }
    }
    switch (value) {
      //Test Force Red Jump
      case 0:
        nr.add(new Position(5, 4));
        nw.add(new Position(4, 3));
        nw.add(new Position(0, 1));
        nr.add(new Position(7, 6));
        break;
        //Test Force Red Double Jump
      case 1:
        nr.add(new Position(5, 4));
        nw.add(new Position(4, 3));
        nw.add(new Position(2, 1));
        nw.add(new Position(0, 1));
        nr.add(new Position(7, 6));
        break;
        //Test Red Turn Into King
      case 2:
        nr.add(new Position(1, 4));
        nw.add(new Position(1, 2));
        break;
        //Test White Cripple
      case 3:
        nw.add(new Position(0, 1));
        nr.add(new Position(1, 0));
        nr.add(new Position(1, 2));
        nr.add(new Position(3, 4));
        break;
        //Test White Lose
      case 4:
        nr.add(new Position(5, 4));
        nw.add(new Position(4, 3));
        break;
        //Test Bot White Win
      case 5:
        nr.add(new Position(5, 4));
        nw.add(new Position(3, 2));
        break;
        //Test Multi Jump / Circular Jump Bot
      case 6:
        nr.add(new Position(6, 3));
        nr.add(new Position(4, 3));
        nr.add(new Position(3, 4));
        nw.add(new Position(7, 2));
        nk.add(new Position(7, 2));
        break;
        //Test Bot Jump Choise
      case 7:
        nr.add(new Position(6, 3));
        nr.add(new Position(4, 3));
        nr.add(new Position(3, 4));
        nr.add(new Position(6, 5));
        nw.add(new Position(7, 4));
        nk.add(new Position(7, 4));
        break;
        //Altrnate Losing Board
      case 8:
        nr.add(new Position(1, 0));
        nr.add(new Position(7, 6));
        nr.add(new Position(3, 4));

        nw.add(new Position(0, 1));
        nw.add(new Position(6, 1));
        nw.add(new Position(0, 3));
        nw.add(new Position(1, 4));
        nw.add(new Position(3, 6));
        nw.add(new Position(2, 7));
        nw.add(new Position(4, 7));

        nk.add(new Position(2, 7));

        break;
        //Test Circular King Piece
      case 9:
        nr.add(new Position(4, 3));
        nr.add(new Position(3, 4));
        nr.add(new Position(5, 4));
        nr.add(new Position(5, 2));
        nw.add(new Position(4, 1));
        nk.add(new Position(4, 1));
        break;
      default:
        nw.add(new Position(0, 1));
        nr.add(new Position(7, 6));
        break;
    }
    for (Position p: nr) {
      getSpace(p).setPiece(new Piece(Color.RED, p));
    }
    for (Position p: nw) {
      getSpace(p).setPiece(new Piece(Color.WHITE, p));
    }
    for (Position p: nk) {
      if (getSpace(p).hasPiece())
        getSpace(p).getPiece().king();
    }
  }

  /**
   * Will return a flipped board so that the board is presented appropriately to the opposing player
   *
   * @return BoardView how the board will be viewed
   */
  public BoardView getFlippedBoard() {
    BoardView result = new BoardView();
    for (int i = 0; i<size; i++) {
      Row resultRow = result.rows[7 - i];
      Row currentRow = rows[i];
      for (int j = 0; j<size; j++) {
        Space currentSpace = currentRow.getSpace(j);
        Space resultSpace = resultRow.getSpace(7 - j);
        resultSpace.setPiece(currentSpace.getPiece());
      }
    }
    return result;
  }

  public Space getSpace(int row, int col) {
    return rows[row].getSpace(col);
  }

  public Space getSpace(Position p) {
    return getSpace(p.row, p.cell);
  }

  /**Creates a java iterator of the rows on the checkers board
   * @return iterator<row> allows the boardview to be iterable*/
  @Override
  public Iterator<Row> iterator() {
    return new Iterator<Row> () {
      int k = 0;
      @Override
      public boolean hasNext() {
        boolean has = k<size;
        return has;
      }

      @Override
      public Row next() {
        Row next = rows[k];
        k++;
        return next;
      }
    };
  }

  /**
   * checks final row on each side for opposite color piece
   * if player's piece reaches furthest row, kings the piece
   */
  public void kingCheck() {
    for (Space space: rows[0]) {
      Piece piece = space.getPiece();
      if (piece != null && piece.getColor() == Color.RED) {
        piece.king();
      }
    }
    for (Space space: rows[7]) {
      Piece piece = space.getPiece();
      if (piece != null && piece.getColor() == Color.WHITE) {
        piece.king();
      }
    }
  }

  public ArrayList<Piece> getPieces(Color pieceColor) {

    ArrayList<Piece> pieces = new ArrayList<Piece> ();

    for (int r = 0; r<8; r++) {

      for (int c = 0; c<8; c++) {

        if (getSpace(r, c).hasPiece()) {

          Piece piece = getSpace(r, c).getPiece();

          if (piece.getColor() == pieceColor) {

            pieces.add(piece);

          }

        }

      }

    }

    return pieces;

  }

  public BoardView copy() {

    ArrayList<Piece> whitePiecesCopy = new ArrayList<Piece> ();

    ArrayList<Piece> redPiecesCopy = new ArrayList<Piece> ();

    Row[] boardCopy = new Row[size];
    for (int i = 0; i<size; i++) {
      boardCopy[i] = new Row(i);
    }

    for (int r = 0; r<8; r++) {
      Row currentRow = boardCopy[r];

      for (int c = 0; c<8; c++) {

        Space copyCurrentSpace = currentRow.getSpace(c);

        Space currentSpace = getSpace(r, c);

        if (currentSpace.hasPiece()) {

          Piece oldPiece = currentSpace.getPiece();

          Color pieceColor = (oldPiece.getColor() == Color.WHITE) ? Color.WHITE : Color.RED;

          Piece piece = new Piece(pieceColor, new Position(r, c));
          if (oldPiece.isKingPiece())
            piece.king();
          if (pieceColor == Color.WHITE)
            whitePiecesCopy.add(piece);
          else
            redPiecesCopy.add(piece);

          copyCurrentSpace.setPiece(piece);

        }

      }

    }

    BoardView copy = new BoardView(boardCopy, redPiecesCopy, whitePiecesCopy, moveValidator);

    return copy;

  }

  public void makeMove(Move move) {

    Position finalPosition = move.getFinalPosition();
    Position initalPosition = move.getInitialPosition();

    Space initalSpace = getSpace(initalPosition);

    Space finalSpace = getSpace(finalPosition);

    Piece piece = initalSpace.removePiece();

    if (move.isJumpMove()) {
      ArrayList<Position> path = move.getJumpPaths();

      for (Position p: path) {
        Position jumpPosition = initalPosition.add(p);

        if (jumpPosition.isOutOfBounds()) {
          if (((piece.getColor() == Color.WHITE && initalPosition.row<finalPosition.row) || (piece.getColor() == Color.RED && initalPosition.row<finalPosition.row))) {
            jumpPosition = initalPosition.add(p);
          } else
            jumpPosition = initalPosition.add(p.flip());
        }

        Space jumpedSpace = getSpace(jumpPosition);

        Piece removed = jumpedSpace.removePiece();

        if (piece.getColor() == Color.RED)
          redPieces.remove(removed);
        else
          whitePieces.remove(removed);

      }
    }
    finalSpace.setPiece(piece);

    piece.setPosition(finalPosition);

    if (finalPosition.row == 0 && piece.getColor() == Color.RED)
      piece.king();
    if (finalPosition.row == 7 && piece.getColor() == Color.WHITE)
      piece.king();

  }

}