package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.*;
import spark.*;
import com.webcheckers.model.*;

/**
 * The UI Controller to Process the Move Validation
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostValidateRoute implements Route {
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostValidateRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
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
        //Gets the Move json from the Ajax call and converts it into a Move object
        String moveString = request.queryParams("actionData");
        Move move = gson.fromJson(moveString, Move.class);
        Player currentUser = request.session().attribute("currentUser");

        System.out.println("Player Move: " + move);
        //flips move coordinates if player is WHITE
        if (currentUser.getColor() == Color.WHITE) {
            move = move.getFlipped();
        }
        //attempts the Ajax move through the move method and immediately returns a Message result
        return gson.toJson(currentUser.move(move));
    }
}