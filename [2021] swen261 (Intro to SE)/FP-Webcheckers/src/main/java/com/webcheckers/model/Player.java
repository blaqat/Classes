package com.webcheckers.model;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.util.Message;

import spark.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class will represent a player that is able to play Web Checkers
 * Contains name and html session
 *  @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 *  @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class Player extends GamePlayer {
    private Session currentSession;
    private boolean hadJump;
    private boolean hasMultiJump;
    private List<Move> moveList;
    private Set<Tournament> invitations;
    private Position currentPiecePosition;
    private Tournament currentTournament;
    private int tournamentRound;
    private int consecutiveWins;

    public Player(String name, Session session) {
        super(name);
        tournamentRound = 0;
        this.invitations = new HashSet<>();
        this.currentPiecePosition = null;
        this.hasMultiJump = false;
        this.moveList = new ArrayList<>();
        this.jumpedList = new ArrayList<>();
        this.currentSession = session;
        this.lastMoveWasJump = false;
        this.turnOver = false;
        this.hadJump = false;
        this.currentTournament = null;
        this.consecutiveWins = 0;
    }

    /**
     * attempts to make a move on the board when given any Move
     * @param move Move class that contains from space and to space
     * @return INFO type message on success - ERROR type message on failure(respective)
     */
    public Message move(Move move) {
        if (turnOver) {
            return Message.error("Your turn is over. Backup move to attempt a different move.");
        }
        Space fromSpace = board.getSpace(move.getInitialPosition().getRow(), move.getInitialPosition().getCell());
        Space toSpace = board.getSpace(move.getFinalPosition().getRow(), move.getFinalPosition().getCell());
        Piece piece = fromSpace.getPiece();
        Type pieceType = piece.getType();
        int rowDiff = move.getFinalPosition().getRow() - move.getInitialPosition().getRow();
        int cellDiff = move.getFinalPosition().getCell() - move.getInitialPosition().getCell();

        // If move was a backwards attempt
        if ((rowDiff > 0 && color == Color.RED) || (rowDiff<0 && color == Color.WHITE)) {
            if (pieceType != Type.KING) {
                return Message.error("Invalid move. Singles cannot move backwards.");
            }
        }

        if (Math.abs(rowDiff) > 2 || Math.abs(cellDiff) > 2) {
            return Message.error("Invalid move. You can only move one diagonal space for a simple move, or two diagonal spaces for a jump.");
        } else if (Math.abs(rowDiff) == 2 || Math.abs(cellDiff) == 2) {
            Space jumpedSpace = board.getSpace(move.getInitialPosition().getRow() + (rowDiff / 2), move.getInitialPosition().getCell() + (cellDiff / 2));
            if (!jumpedSpace.hasPiece() || jumpedSpace.getPiece().getColor() == this.color || jumpedList.contains(jumpedSpace)) {
                return Message.error("Invalid jump. You can only jump if there is an opponent piece in the middle space");
            }
            lastMoveWasJump = true;
            currentPiecePosition = move.getFinalPosition();
            jumpedList.add(jumpedSpace);
        } else {
            if (lastMoveWasJump) {
                return Message.error("You can't make a simple move now. You may only jump if available.");
            }
            turnOver = true;
        }

        fromSpace.setPiece(null);
        toSpace.setPiece(piece);

        moveList.add(move.getInverse());
        if (lastMoveWasJump) {
            scanMultiJump();
        }
        return Message.info("Valid move.");
    }

    //checks if any given move is valid
    public boolean validateMove(Move move) {
        if (turnOver) {
            return false;
        }
        Space fromSpace = board.getSpace(move.getInitialPosition().getRow(), move.getInitialPosition().getCell());
        Space toSpace = board.getSpace(move.getFinalPosition().getRow(), move.getFinalPosition().getCell());
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
            if (!jumpedSpace.hasPiece() || jumpedSpace.getPiece().getColor() == this.color || jumpedList.contains(jumpedSpace) || toSpace.hasPiece()) {
                return false;
            }
        } else {
            return !lastMoveWasJump;
        }
        return true;
    }

    /**
     * called to make sure the submitted turn is valid
     *
     * @return INFO "true" message on valid move - ERROR "false" message otherwise
     */
    public Message validateTurn() {
        boolean tookFirstJump = (!hadJump || lastMoveWasJump);
        boolean tookMultiJump = !hasMultiJump;
        if (tookFirstJump && tookMultiJump) {
            for (Space space: jumpedList) {
                space.setPiece(null);
            }
            jumpedList.clear();
            moveList.clear();
            gameSession.switchTurn();
            board.kingCheck();
            newTurn();
            GamePlayer opponent = null;
            if (this.color == Color.RED) {
                opponent = gameSession.getWhitePlayer();
            } else {
                opponent = gameSession.getRedPlayer();
            }
            if (opponent.type != PlayerType.ROBOT) {
                ((Player) opponent).scanForFirstJump();
            } else {
                return Message.info("AI Moved");
            }

            return Message.info("Valid turn");
        } else if (!tookMultiJump) {
            return Message.error("Invalid turn. You have another possible jump. Take it, or backup.");
        } else {
            return Message.error("Invalid turn. When at least one jump is available, you must perform a jump.");
        }
    }

    /**
     * private helper to change the hasMultiJump boolean when the user
     * has a multijump option that they need to take
     */
    private void scanMultiJump() {
        hasMultiJump = false;
        Position startPos = currentPiecePosition;
        boolean outOfRange = false;
        int rowEnd = startPos.getRow() + 2;
        int cellEnd = startPos.getCell() + 2;
        if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
            outOfRange = true;
        }
        if (!outOfRange) {
            Position endPos = new Position(rowEnd, cellEnd);
            Move potentialJump = new Move(startPos, endPos);
            if (validateMove(potentialJump)) {
                hasMultiJump = true;
            }
        }
        outOfRange = false;
        rowEnd = startPos.getRow() - 2;
        cellEnd = startPos.getCell() + 2;
        if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
            outOfRange = true;
        }
        if (!outOfRange) {
            Position endPos = new Position(rowEnd, cellEnd);
            Move potentialJump = new Move(startPos, endPos);
            if (validateMove(potentialJump)) {
                hasMultiJump = true;
            }
        }
        outOfRange = false;
        rowEnd = startPos.getRow() - 2;
        cellEnd = startPos.getCell() - 2;
        if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
            outOfRange = true;
        }
        if (!outOfRange) {
            Position endPos = new Position(rowEnd, cellEnd);
            Move potentialJump = new Move(startPos, endPos);
            if (validateMove(potentialJump)) {
                hasMultiJump = true;
            }
        }
        outOfRange = false;
        rowEnd = startPos.getRow() + 2;
        cellEnd = startPos.getCell() - 2;
        if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
            outOfRange = true;
        }
        if (!outOfRange) {
            Position endPos = new Position(rowEnd, cellEnd);
            Move potentialJump = new Move(startPos, endPos);
            if (validateMove(potentialJump)) {
                hasMultiJump = true;
            }
        }

    }

    /**
     * private helper for checking if the user has a jump at the start of their turn
     * that they must take
     */
    private void scanForFirstJump() {
        List<Position> positions = new ArrayList<>();
        for (Row row: board) {
            for (Space space: row) {
                Piece piece = space.getPiece();
                if (piece != null && (piece.getColor() == this.color)) {
                    positions.add(new Position(row.getIndex(), space.getCellIdx()));
                }
            }
        }
        for (Position startPos: positions) {
            boolean outOfRange = false;
            int rowEnd = startPos.getRow() + 2;
            int cellEnd = startPos.getCell() + 2;
            if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
                outOfRange = true;
            }
            if (!outOfRange) {
                Position endPos = new Position(rowEnd, cellEnd);
                Move potentialJump = new Move(startPos, endPos);
                if (validateMove(potentialJump)) {
                    hadJump = true;
                }
            }
            outOfRange = false;
            rowEnd = startPos.getRow() - 2;
            cellEnd = startPos.getCell() + 2;
            if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
                outOfRange = true;
            }
            if (!outOfRange) {
                Position endPos = new Position(rowEnd, cellEnd);
                Move potentialJump = new Move(startPos, endPos);
                if (validateMove(potentialJump)) {
                    hadJump = true;
                }
            }
            outOfRange = false;
            rowEnd = startPos.getRow() - 2;
            cellEnd = startPos.getCell() - 2;
            if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
                outOfRange = true;
            }
            if (!outOfRange) {
                Position endPos = new Position(rowEnd, cellEnd);
                Move potentialJump = new Move(startPos, endPos);
                if (validateMove(potentialJump)) {
                    hadJump = true;
                }
            }
            outOfRange = false;
            rowEnd = startPos.getRow() + 2;
            cellEnd = startPos.getCell() - 2;
            if (rowEnd > 7 || rowEnd<0 || cellEnd > 7 || cellEnd<0) {
                outOfRange = true;
            }
            if (!outOfRange) {
                Position endPos = new Position(rowEnd, cellEnd);
                Move potentialJump = new Move(startPos, endPos);
                if (validateMove(potentialJump)) {
                    hadJump = true;
                }
            }
        }
    }

    /**
     * helper to let player move when their turn comes
     */
    public void newTurn() {
        turnOver = false;
        lastMoveWasJump = false;
        hadJump = false;
        currentPiecePosition = null;
        hasMultiJump = false;
        scanForFirstJump();
    }

    //Undo a move
    public Message backupMove() {
        Move move = moveList.remove(moveList.size() - 1);
        Space fromSpace = this.board.getSpace(move.getInitialPosition().getRow(), move.getInitialPosition().getCell());
        Space toSpace = this.board.getSpace(move.getFinalPosition().getRow(), move.getFinalPosition().getCell());
        Piece piece = fromSpace.getPiece();
        fromSpace.setPiece(null);
        toSpace.setPiece(piece);
        currentPiecePosition = move.getFinalPosition();
        hasMultiJump = false;
        int rowDiff = move.getFinalPosition().getRow() - move.getInitialPosition().getRow();
        int cellDiff = move.getFinalPosition().getCell() - move.getInitialPosition().getCell();
        if (Math.abs(rowDiff) == 2 || Math.abs(cellDiff) == 2) {
            jumpedList.remove(jumpedList.size() - 1);
        }
        if (moveList.size() == 0) {
            newTurn();
        } else {
            scanMultiJump();
        }
        return Message.info("Moved piece from " + move.getInitialPosition().getRow() + ", " + move.getInitialPosition().getCell() + " back to " + move.getFinalPosition().getRow() + ", " + move.getFinalPosition().getCell());
    }

    public void joinTournament(Tournament tournament) {
        this.currentTournament = tournament;
        this.tournamentRound = 1;
    }

    public int getTournamentRound() {
        return tournamentRound;
    }

    public void invite(Tournament tournament) {
        this.invitations.add(tournament);
    }

    public void clearInvites() {
        this.invitations.clear();
    }

    public Set<Tournament> getInvitations() {
        return this.invitations;
    }

    public void leaveTournament() {
        this.currentTournament = null;
    }

    public Tournament getCurrentTournament() {
        return this.currentTournament;
    }

    public Session getSession() {
        return currentSession;
    }

    public void exitGame(boolean hasWon, PlayerLobby lobby) {
        this.inGame = false;

        this.incrementGames();
        newTurn();
        this.hadJump = false;

        if (hasWon) {
            GamePlayer opp = this.gameSession.getWhitePlayer();
            if (opp.type == PlayerType.ROBOT) {
                switch (((AI) opp).getDifficulty()) {
                    case EASY:
                        this.achievements.add("Beat Easy Bot");
                        break;
                    case HARD:
                        this.achievements.add("Beat Hard Bot");
                        break;
                    case MED:
                        this.achievements.add("Beat Medium Bot");
                        break;
                    default:
                        break;
                }
            }
            if (getCurrentTournament() != null) {
                getCurrentTournament().reportWinner(this);
            } else {
                this.gameSession = null;
            }
            this.incrementGamesWon();
            this.consecutiveWins++;
        } else {
            leaveTournament();
            tournamentRound = 0;
            this.gameSession = null;
            this.consecutiveWins = 0;
        }
        this.achievements.toggle("Zero Deaths", this.getWinRate() == 100 && this.consecutiveWins > 1);
        this.achievements.toggle("Hot Streak", this.consecutiveWins >= 3);

    }
}