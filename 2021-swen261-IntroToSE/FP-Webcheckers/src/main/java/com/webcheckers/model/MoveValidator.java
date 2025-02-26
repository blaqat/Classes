/* * Will validate that a player will make
 *@author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * @author<a href='mailto:ajg7654@rit.edu'>Aiden Green</a>*/
package com.webcheckers.model;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Objects;

import com.webcheckers.model.AI.Difficulty;

public class MoveValidator {
    Dictionary<Integer, Position[] > spacesJumped;
    private ArrayList<Position> [] jumpPaths;
    private Position[] moves;

    public MoveValidator() {

        moves = new Position[3];
        jumpPaths = (ArrayList<Position> []) new ArrayList[3];

        //Same as the ai it initializes the list of possible moves
        moves[0] = new Position(-1, -1);

        moves[1] = new Position(-1, 1);

        moves[2] = new Position(0, 0);
        jumpPaths[2] = new ArrayList<Position> ();
    }
    //Checks if there are any valid moves.
    public boolean hasValidMoves(BoardView board, Color color) {

        ArrayList<Piece> pieces = board.getPieces(color);
        //This is bassically the same as the ai move
        for (Piece piece: pieces) {
            for (int i = 0; i<moves.length; i++) {
                int row = piece.getPosition().row + moves[i].row;
                int column = piece.getPosition().cell + moves[i].cell;
                if (color == Color.WHITE) {
                    row -= moves[i].row * 2;
                    column -= moves[i].cell * 2;
                }

                Move currentMove = new Move(piece.getPosition(), new Position(row, column), i == 2, jumpPaths[i]);

                if ((currentMove.isJumpMove() && MoveValidator.hasJumpMove(board, currentMove)) || MoveValidator.validateMove(board, currentMove)) {
                    return true;
                }

            }
            if (piece.isKingPiece()) {
                for (int i = 0; i<moves.length; i++) {
                    int row = piece.getPosition().row + moves[i].row;
                    int column = piece.getPosition().cell + moves[i].cell;
                    if (color == Color.RED) {
                        row -= moves[i].row * 2;
                        column -= moves[i].cell * 2;
                    }

                    Move currentMove = new Move(piece.getPosition(), new Position(row, column), i == 2, jumpPaths[i]);

                    if (MoveValidator.validateMove(board, currentMove)) {
                        return true;

                    }

                }
            }
        }
        return false;

    }

    public static boolean validateMove(BoardView board, Move move, Difficulty difficulty) {
        Position initalPosition = move.getInitialPosition();

        Piece piece = board.getSpace(initalPosition).getPiece();

        Color color = (piece.getColor());

        if (!move.isInBounds()) {
            return false;
        }

        ArrayList<Move> newMoves = MoveValidator.getJumpMoves(board, move);
        if (move.isJumpMove() && newMoves.size() > 0) {
            Move oldMove = new Move();
            oldMove.set(move);
            Move newMove = null;
            for (Move nm: newMoves) {
                BoardView c = board.copy();

                if (validateMove(c, nm, difficulty))
                    c.makeMove(nm);

                nm.setMoveQuality(BoardRater.rateBoard(c, color));

                if (Objects.isNull(newMove))
                    newMove = nm;
                else
                    newMove = AI.getBetterMove(difficulty, newMove, nm);
            }

            move.set(newMove);

            return true;
        }

        return validateMove(board, move);
    }
    //Will validate a single move
    public static boolean validateMove(BoardView board, Move move) {
        Position finalPosition = move.getFinalPosition();
        Position initalPosition = move.getInitialPosition();

        Piece piece = board.getSpace(initalPosition).getPiece();

        Color color = (piece.getColor());
        //moving to empty space -> false
        /*
            not jump move -> true

            loop through jump spaces
                if not space color isnt move color or empty -> false
                if space color is move color and not empty

             -> true
         */
        if (!move.isInBounds()) {
            return false;
        }

        if (move.isJumpMove() && MoveValidator.hasJumpMove(board, move) && move.getJumpPaths().size() != 0)
            return true;

        if (board.getSpace(finalPosition).hasPiece() || !move.isJumpMove() && MoveValidator.needsJumpMove(board, color)) {
            return false;
        }

        return true;
    }

    public static boolean needsJumpMove(BoardView b, Color c) {
        for (Piece p: b.getPieces(c)) {
            if (hasJumpMove(b, new Move(p.getPosition(), p.getPosition())))
                return true;
        }
        return false;
    }

    public static ArrayList<Move> getJumpMoves(BoardView board, Move move) {

        ArrayList<Move> jumpMoves = new ArrayList<Move> ();
        Position init = move.getInitialPosition();
        Position fin = move.getFinalPosition();
        Space space = board.getSpace(init);
        if (!space.hasPiece()) return jumpMoves;

        Piece piece = space.getPiece();
        boolean isKing = piece.isKingPiece();
        Position[] neighbors = fin.getNeighbors();
        ArrayList<Position> jumpsMade = move.getJumpPaths();
        boolean madeJumps = Objects.nonNull(jumpsMade);

        int start = (isKing || piece.getColor() == Color.WHITE) ? 0 : 2;
        for (int i = start; i<(isKing ? 4 : (start + 2)); i++) {
            Position neighbor = fin.add(neighbors[i]);
            Position nextDestination = neighbor.add(neighbors[i]);

            Position toNeigh = neighbor.subtract(init);
            Position toNext = nextDestination.subtract(init);
            if (
                !neighbor.isOutOfBounds() && !nextDestination.isOutOfBounds() &&
                (board.getSpace(neighbor).hasPiece() &&
                    (!madeJumps || madeJumps && jumpsMade.indexOf(toNeigh) == -1)) &&
                (!board.getSpace(nextDestination).hasPiece() ||
                    madeJumps && (jumpsMade.indexOf(toNext) != -1 || nextDestination.equals(init))) &&
                board.getSpace(neighbor).getPiece().getColor() != piece.getColor()
            ) {
                ArrayList<Position> newJumpPath = madeJumps ? new ArrayList<Position> (move.getJumpPaths()) : new ArrayList<Position> ();
                newJumpPath.add(toNeigh);
                jumpMoves.add(new Move(init, nextDestination, true, newJumpPath));
            }
        }
        return jumpMoves;
    }

    public static boolean hasJumpMove(BoardView b, Move m) {
        return getJumpMoves(b, m).size() > 0;
    }

    public Position[] getMoves() {
        return moves;
    }

    public ArrayList<Position> [] getJumpPaths() {
        return jumpPaths;
    }
}