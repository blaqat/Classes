package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.*;

import spark.*;

import com.webcheckers.model.*;

/**
 * The UI Controller to Process the turn submission
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostSubmitTurnRoute implements Route {
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSubmitTurnRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.gson = gson;
    }

    /**
     * Processes the turn submission
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
        //attempts the Ajax move through the move method and immediately returns a Message result
        return gson.toJson(currentUser.validateTurn());
    }
}