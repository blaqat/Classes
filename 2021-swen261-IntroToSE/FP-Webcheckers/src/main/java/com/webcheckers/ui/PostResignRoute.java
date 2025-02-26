package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.*;

import com.webcheckers.util.Message;
import spark.*;

import com.webcheckers.model.*;
import com.webcheckers.model.CheckersGame.GameState;
import com.webcheckers.model.GamePlayer.PlayerType;

/**
 * This is the ui controller for the post resign button
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Maxwell Shenk</a>
 */
public class PostResignRoute implements Route {
    private final PlayerLobby playerLobby;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostResignRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.playerLobby = playerLobby;
        this.gson = gson;
    }

    /**
     * Processes the resign
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Game view
     */
    @Override
    public Object handle(Request request, Response response) {
        //Gets the Move json from the Ajax call and converts it into a Move object
        Player currentUser = request.session().attribute("currentUser");
        CheckersGame game = currentUser.getGameSession();
        GamePlayer potentialAI = game.getWhitePlayer();
        if (potentialAI.getType() != GamePlayer.PlayerType.HUMAN)
            potentialAI.exitGame(true, playerLobby);

        if (game.getGameState().equals(GameState.ACTIVE)) {
            Color color = currentUser.getColor();
            //If the player that resigned is currently the one whose turn it is they will switch turns
            if (game.getActiveColor() == color && potentialAI.getType() == PlayerType.HUMAN) {
                game.switchTurn();
            }

            //Changes the game state and as the player leave the game.
            currentUser.resignGame(playerLobby);
        }
        //automatically returns true because if we have gotten to this piint then it is supposed to be true
        return gson.toJson(Message.info("true"));
    }
}