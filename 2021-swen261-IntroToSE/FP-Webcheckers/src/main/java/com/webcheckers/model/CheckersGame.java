/**
 * Instance of the game, keeps track of the board and players playing
 * author<a href='mailto:ajg7654@rit.edu'>Aiden Green</a>
 */
package com.webcheckers.model;

import com.webcheckers.model.GamePlayer.PlayerType;
import java.util.Iterator;

public class CheckersGame {
    public enum GameState {
        ACTIVE,
        OVER_RESIGN,
        OVER_CRIPPLE,
        OVER_ENDED
    }

    private final int id;
    private BoardView board;
    private GamePlayer[] players;
    private Color activeColor;
    private GameState state;
    private MoveValidator moveValidator;
    private GamePlayer winner;

    public CheckersGame(int id, GamePlayer red, GamePlayer white) {
        this.moveValidator = new MoveValidator();
        this.board = new BoardView(moveValidator);
        System.out.println("constructing game");
        this.id = id;
        this.players = new GamePlayer[2];
        red.setColor(Color.RED);
        white.setColor(Color.WHITE);
        this.players[0] = red;
        this.players[1] = white;
        this.activeColor = Color.RED;
        this.state = GameState.ACTIVE;
        this.winner = null;
    }

    public AI createAI(Color color) {
        AI ai = new AI(((AI) players[1]).getDifficulty(), moveValidator);
        ai.joinGame(this, color);
        this.players[1] = ai;
        return ai;
    }

    public BoardView getBoard() {
        return board;
    }

    public GamePlayer getRedPlayer() {
        return players[0];
    }

    public GamePlayer getWhitePlayer() {
        return players[1];
    }

    public GamePlayer getWinner() {
        return winner;
    }

    /**
     * Helper function to switch whose turn it is
     */
    public void switchTurn() {

        if (this.activeColor == Color.RED) {
            if (state == GameState.ACTIVE || state != GameState.ACTIVE && players[1].getType() == PlayerType.HUMAN) {
                this.activeColor = Color.WHITE;
                players[1].newTurn();
            }
        } else if (this.activeColor == Color.WHITE) {
            this.activeColor = Color.RED;
            players[0].newTurn();
        }
    }

    //Checks a single position against every other posiiton on the board.
    public boolean hasValidMoves(int r, int c, GamePlayer player) {
        Position base = new Position(r, c);
        Iterator<Row> rows = board.iterator();

        //Loops through all possible spaces on the board.
        for (int i = 0; i<8; i++) {
            Row currentRow = rows.next();
            for (int j = 0; j<8; j++) {
                Space curSpace = currentRow.getSpace(j);
                Position currentCheck = new Position(i, j);
                Move move = new Move(base, currentCheck);

                //If the space is valid it will the check if the move was valid and if it was it will return trueystem.out.println(curSpace.isValid());
                if (curSpace.isValid() && player.validateMove(move)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Checks if any player has been crippled and returns the color of the player crippled.
    public Color playerCrippled() {
        if(state != GameState.ACTIVE){
            return winner.getColor()==Color.RED?Color.WHITE:Color.RED;
        }

        boolean redCrip = true;
        boolean whiteCrip = true;
        Iterator<Row> rows = board.iterator();
        //Loops through all possible spaces on the board

        for (int i = 0; i<8; i++) {
            Row currentRow = rows.next();
            for (int j = 0; j<8; j++) {
                Space currentSpace = currentRow.getSpace(j);

                //If the space has a piece it will check all the valid moves and change
                //the boolean depending on wether or not it can move
                if (currentSpace.hasPiece())
                    if (currentSpace.getPiece().getColor() == Color.RED) {
                        if (hasValidMoves(i, j, (Player) getRedPlayer()))
                            redCrip = false;
                    }
                else if (currentSpace.getPiece().getColor() == Color.WHITE) {

                    if (hasValidMoves(i, j, getWhitePlayer()))
                        whiteCrip = false;
                }
            }
        }
        //if the red or the white cannot move at all it will return that color
        if (redCrip && board.getPieces(Color.RED).size() != 0) {
            state = GameState.OVER_CRIPPLE;
            winner = getWhitePlayer();
            return Color.RED;
        }
        if (whiteCrip && board.getPieces(Color.WHITE).size() != 0) {
            state = GameState.OVER_CRIPPLE;
            winner = getRedPlayer();
            return Color.WHITE;
        }
        return null;
    }

    public Color endGame() {
        //if player resigns || player loses all pieces, end game and declare winner
        /*for(Player p : players){
            if (p.hasLost()){
                loser = p;
                return true;
            }
        }*/

        //check each row for all pieces of each color and keep a tally with two ints that are ++ every time the search finds a piece of that color
        //if a counter is zero after the end of the search, declare that color player the loser and end the game
        int redCount = 0;
        int whiteCount = 0;
        for (Row r: board) {
            for (Space s: r) {
                if (s.getPiece() != null && s.getPiece().getColor() == Color.RED) {
                    redCount++;
                }
                if (s.getPiece() != null && s.getPiece().getColor() == Color.WHITE) {
                    whiteCount++;
                }
            }
        }
        if (redCount == 0) {
            state = GameState.OVER_ENDED;
            this.winner = getWhitePlayer();
            GamePlayer redPlayer = getRedPlayer();
            if (redPlayer.type != PlayerType.ROBOT)
                ((Player) redPlayer).leaveTournament();
            return Color.RED;
        }
        if (whiteCount == 0) {
            state = GameState.OVER_ENDED;
            this.winner = getRedPlayer();
            GamePlayer whitePlayer = getWhitePlayer();
            if (whitePlayer.type != PlayerType.ROBOT)
                ((Player) whitePlayer).leaveTournament();
            return Color.WHITE;
        }
        return null;
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    public void playerResign(GamePlayer player) {
        this.state = GameState.OVER_RESIGN;
        if (player.getColor() == Color.RED) {
            this.winner = getWhitePlayer();
        } else {
            this.winner = getRedPlayer();
        }
    }

    public GameState getGameState() {
        return this.state;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    public int getId() {
        return id;
    }
}