package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.*;

import com.webcheckers.util.Message;
import spark.*;

import com.webcheckers.model.*;

/**
 * The UI Controller to Process the Move Validation
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostCheckTurnRoute implements Route {
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostCheckTurnRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.gson = gson;
    }

    /**
     * Processes the Move Validation
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
        Player currentUser = request.session().attribute("currentUser");
        if (currentUser == null) {
            return null;
        }
        boolean isTurn;
        isTurn = currentUser.getGameSession().getActiveColor() == currentUser.getColor();
        if (isTurn) {
            return gson.toJson(Message.info("true"));
        } else {
            return gson.toJson(Message.info("false"));
        }
    }
}