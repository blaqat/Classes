/** This class will calculate the functionality of an AI player that will be able to go against any
 * online player. There will be three different levels, that this class will calculate
 * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * @author<a href='mailto:ajg7654@rit.edu'>Aiden Green</a>*/
package com.webcheckers.model;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import com.webcheckers.application.PlayerLobby;

public class AI extends GamePlayer {

    //Determines the ai difficulty
    public enum Difficulty {
        EASY,
        MED,
        HARD
    }
    private Difficulty difficulty;
    private Position[] moves;
    private MoveValidator moveValidator;
    private final int MAX_DEPTH;
    private final int GOOD_RATING;
    private ArrayList<Position> [] jumpPaths;
    private Move mostRecentMove;
    //This is the default ai constructor
    public AI(Difficulty level) {
        super(level.equals(Difficulty.EASY) ? "Gary" : level.equals(Difficulty.MED) ? "Qwerty" : "Sputnik");
        this.MAX_DEPTH = 4;
        this.GOOD_RATING = 25;
        this.type = PlayerType.ROBOT;
        this.difficulty = level;
        moves = new Position[3];
        jumpPaths = (ArrayList<Position> []) new ArrayList[3];
    }
    public AI(Difficulty level, MoveValidator mv) {
        this(level);
        this.moveValidator = mv;
        moves = mv.getMoves();
        jumpPaths = mv.getJumpPaths();
    }
    //initialize
    public AI(BoardView board, MoveValidator moveValidator, Color color, Difficulty difficulty) {
        this(difficulty, moveValidator);
        this.board = board;
        this.color = color;
    }

    public void newTurn() {
        boolean madeMove = false;
        //This is the make turn method
        //Only gets executed if there are pieces and it has some valid moves
        if (board.getPieces(Color.WHITE).size() != 0 && moveValidator.hasValidMoves(board, color)) {
            Move move = turnHelper(board, 0, true);
            System.out.println("AI MOVE: " + move);
            board.makeMove(move);
            mostRecentMove = move;
            madeMove = true;
        }

        if (Objects.nonNull(this.gameSession)) {
            if (!madeMove) {
                this.gameSession.playerResign(this);
            }
            this.gameSession.switchTurn();
        }

    }
    //This is the recursive helper method
    //The heart and soul of our ai
    Move turnHelper(BoardView currentBoard, int depth, boolean turn) {

        Move optimalMove = new Move();

        ArrayList<Move> validMoves = new ArrayList<Move> ();

        Color currentColor = Color.RED;

        if (!turn && color == Color.RED)
            currentColor = Color.WHITE;
        if (turn && color == Color.WHITE)
            currentColor = Color.WHITE;

        ArrayList<Piece> myPieces = currentBoard.getPieces(currentColor);

        //Creates an arrayList of all possible moves for the current player
        //At this current recursion depth
        //This loops through all the possible pieces and checks all possible moves
        for (Piece piece: myPieces) {
            for (int i = 0; i<moves.length; i++) {
                int row = piece.getPosition().row + moves[i].row;
                int column = piece.getPosition().cell + moves[i].cell;
                if (currentColor == Color.WHITE) {
                    row -= moves[i].row * 2;
                    column -= moves[i].cell * 2;
                }

                Move currentMove = new Move(piece.getPosition(), new Position(row, column), i == 2, jumpPaths[i]);

                if (MoveValidator.validateMove(currentBoard, currentMove, this.difficulty)) {

                    validMoves.add(currentMove);

                }

            }

            //If its a king piece it checks the revverse moves too
            if (piece.isKingPiece()) {
                for (int i = 0; i<moves.length; i++) {
                    int row = piece.getPosition().row + moves[i].row;
                    int column = piece.getPosition().cell + moves[i].cell;
                    if (currentColor == Color.RED) {
                        row -= moves[i].row * 2;
                        column -= moves[i].cell * 2;
                    }

                    Move currentMove = new Move(piece.getPosition(), new Position(row, column), i == 2, jumpPaths[i]);

                    if (MoveValidator.validateMove(currentBoard, currentMove)) {
                        validMoves.add(currentMove);
                    }
                }
            }
        }

        //After it decides wahat moves are valid it loops throught to find the best (or worst) one
        for (Move currentMove: validMoves) {

            BoardView currentBoardCopy = currentBoard.copy();
            currentBoardCopy.makeMove(currentMove);

            int rating = BoardRater.rateBoard(currentBoardCopy, currentColor);

            if (turn) {
                Move opponentMove = turnHelper(currentBoardCopy, depth, false);

                if (opponentMove.getMoveQuality() != Integer.MIN_VALUE) {

                    currentBoardCopy.makeMove(opponentMove);

                    rating -= opponentMove.getMoveQuality();
                } else
                    rating += 10;

                if (depth<3 || (rating > GOOD_RATING && depth<MAX_DEPTH)) {
                    Move nextMove = turnHelper(currentBoardCopy, depth + 1, true);

                    if (nextMove.getMoveQuality() != Integer.MIN_VALUE) {
                        rating += nextMove.getMoveQuality();
                    }

                }

            }

            currentMove.setMoveQuality(rating);
            optimalMove = getBetterMove(difficulty, optimalMove, currentMove);
        }

        return optimalMove;

    }

    public static Move getBetterMove(Difficulty difficulty, Move optimalMove, Move currentMove) {
        if (optimalMove.equals(currentMove)) return optimalMove;
        if (difficulty == Difficulty.HARD) {

            if (((!optimalMove.isJumpMove() && currentMove.isJumpMove()) || (currentMove.isJumpMove() == optimalMove.isJumpMove() && currentMove.getMoveQuality() > optimalMove.getMoveQuality()))) {

                optimalMove = currentMove;

            }

            if (currentMove.getMoveQuality() == optimalMove.getMoveQuality() && ((optimalMove.isJumpMove() == currentMove.isJumpMove()))) {

                Random random = new Random();

                if (random.nextBoolean())
                    optimalMove = currentMove;

            }

        } else if (difficulty == Difficulty.MED) {
            if ((!optimalMove.isJumpMove() && currentMove.isJumpMove()) || (optimalMove.getMoveQuality() == Integer.MIN_VALUE && currentMove.getMoveQuality() != Integer.MIN_VALUE)) {
                optimalMove = currentMove;
            } else if (optimalMove.isSubsetOf(currentMove) && optimalMove.count()<currentMove.count()) {
                optimalMove = currentMove;
            } else if (currentMove.getMoveQuality() != Integer.MIN_VALUE && ((optimalMove.isJumpMove() == currentMove.isJumpMove()))) {
                Random random = new Random();

                if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean())

                    optimalMove = currentMove;

            }
        } else if (difficulty == Difficulty.EASY) {
            if ((!optimalMove.isJumpMove() && currentMove.isJumpMove()) || (optimalMove.getMoveQuality() == Integer.MIN_VALUE && currentMove.getMoveQuality() != Integer.MIN_VALUE)) {
                optimalMove = currentMove;
            } else if (optimalMove.getMoveQuality() > currentMove.getMoveQuality() && currentMove.getMoveQuality() != Integer.MIN_VALUE && ((optimalMove.isJumpMove() == currentMove.isJumpMove()))) {
                optimalMove = currentMove;
            }

        }

        return optimalMove;
    }

    //This method is the same as the parent but only worked when in here.
    public boolean validateMove(Move move) {
        if (turnOver) {
            return false;
        }
        Space fromSpace = board.getSpace(move.getInitialPosition().getRow(), move.getInitialPosition().getCell());
        Piece piece = fromSpace.getPiece();
        Type pieceType = piece.getType();
        int rowDiff = move.getFinalPosition().getRow() - move.getInitialPosition().getRow();
        int cellDiff = move.getFinalPosition().getCell() - move.getInitialPosition().getCell();

        // If move was a backwards attempt
        if ((rowDiff > 0 && color == Color.RED) || (rowDiff<0 && color == Color.WHITE)) {
            if (pieceType != Type.KING) {
                return false;
            }
        }
        if (Math.abs(rowDiff) > 2 || Math.abs(cellDiff) > 2) {
            return false;
        } else if (Math.abs(rowDiff) == 2 || Math.abs(cellDiff) == 2) {
            Space jumpedSpace = board.getSpace(move.getInitialPosition().getRow() + (rowDiff / 2), move.getInitialPosition().getCell() + (cellDiff / 2));
            if (!jumpedSpace.hasPiece() || jumpedSpace.getPiece().getColor() == this.color) {
                return false;
            }
        } else {
            return !lastMoveWasJump;
        }
        return true;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
    public Move getRecentMove() {
        return mostRecentMove;
    }
    public void exitGame(boolean hasWon, PlayerLobby playerLobby) {
        if (hasWon && type == PlayerType.ROBOT) {
            AI parent = (AI) playerLobby.getPlayer(name);
            parent.incrementGamesWon();
        }

        exitGame(playerLobby);
    }

}
