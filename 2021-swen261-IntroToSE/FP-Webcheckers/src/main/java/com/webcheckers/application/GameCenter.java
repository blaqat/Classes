/**
 * Manages the games in between players, what view that game can be viewed as, or if the game is
 * valid to play
 * author<a href='mailto:ajg7654@rit.edu'>Aiden Green</a>*/
package com.webcheckers.application;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Color;
import com.webcheckers.model.GamePlayer;

import java.util.HashMap;

/**
 * Responsible for starting games
 *
 * @author
 */
public class GameCenter {

    private final HashMap<Integer, CheckersGame> storedGames;
    private int nextUniqueId;

    public GameCenter() {
        this.storedGames = new HashMap<Integer, CheckersGame> ();
        this.nextUniqueId = 1;
    }

    /**
     *
     * @param red Any Player
     * @param white Any Player
     * @return New instance of CheckersGame
     * Will not initialize if either player is already in a game
     */
    public CheckersGame initializeGame(GamePlayer red, GamePlayer white) {
        int id = nextUniqueId;
        nextUniqueId++;
        CheckersGame newGame = new CheckersGame(id, red, white);
        red.joinGame(newGame, Color.RED);

        white.joinGame(newGame, Color.WHITE);
        return newGame;
    }

    public CheckersGame getGame(int id) {
        return storedGames.get((Integer) id);
    }

    public void storeGame(CheckersGame game) {
        storedGames.put(game.getId(), game);
    }

    public CheckersGame removeGame(int id) {
        return storedGames.remove((Integer) id);
    }
}