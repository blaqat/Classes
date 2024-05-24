package com.webcheckers.model;

import com.webcheckers.application.PlayerLobby;

import java.util.List;

public abstract class GamePlayer implements Comparable<GamePlayer> {
    public enum PlayerType {
        HUMAN,
        ROBOT
    }

    protected PlayerType type;
    protected final String name;
    protected CheckersGame gameSession;
    protected Color color;
    protected boolean inGame;
    private int gamesPlayed;
    private int gamesWon;
    protected BoardView board;
    protected List<Space> jumpedList;
    protected boolean lastMoveWasJump;
    protected boolean turnOver;
    protected Achievement achievements;

    public GamePlayer(String name) {
        this.name = name;
        this.inGame = false;
        this.gameSession = null;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.type = PlayerType.HUMAN;
        this.achievements = new Achievement();
    }

    public String getAchievementsAsString() {
        return this.achievements.toString();
    }

    public void joinGame(CheckersGame game, Color color) {
        this.gameSession = game;
        if (type == PlayerType.HUMAN) {
            this.inGame = true;
        }
        this.board = game.getBoard();
        this.color = color;
    }

    public void resignGame(PlayerLobby playerLobby) {
        gameSession.playerResign(this);
        this.exitGame(false, playerLobby);
    }

    public void exitGame(PlayerLobby playerLobby) {
        this.gameSession = null;
        this.inGame = false;
        this.gamesPlayed++;
        if (type == PlayerType.ROBOT) {
            AI parent = (AI) playerLobby.getPlayer(name);
            parent.incrementGames();
        }
    }

    public abstract void exitGame(boolean a, PlayerLobby p);

    public void incrementGamesWon() {
        this.gamesWon++;
    }
    public void incrementGames() {
        this.gamesPlayed++;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean isPlayingGame() {
        return this.inGame;
    }

    public String getName() {
        return name;
    }

    public CheckersGame getGameSession() {
        return this.gameSession;
    }

    public double getWinRate() {
        if (gamesPlayed == 0)
            return 0;
        return ((double) Math.round((((double) gamesWon) / (double) gamesPlayed * 100) * 10)) / 10;
    }

    public abstract void newTurn();

    public PlayerType getType() {
        return type;
    }

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
            if (!jumpedSpace.hasPiece() || jumpedSpace.getPiece().getColor() == this.color || jumpedList.contains(jumpedSpace)) {
                return false;
            }
        } else {
            return !lastMoveWasJump;
        }
        return true;
    }

    public String getStats() {
        return "[ WR: " + getWinRate() + "% | W: " + gamesWon + "  | L: " + (gamesPlayed - gamesWon) + " | GP: " + gamesPlayed + " ]";
    }

    @Override
    public int compareTo(GamePlayer o) {
        double[] winRates = {
            this.getWinRate(),
            o.getWinRate()
        };

        if (winRates[0] > winRates[1]) {
            return 1;
        } else if (winRates[0]<winRates[1]) {
            return -1;
        } else {
            return 0;
        }
    }
}