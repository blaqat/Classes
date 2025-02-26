package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.*;

import spark.*;

import com.webcheckers.model.*;

/**
 * The UI Controller to Process the move backup
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostBackupRoute implements Route {
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostBackupRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.gson = gson;
    }

    /**
     * Processes the move backup
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
        // returns Ajax message describing reversed move
        return gson.toJson(currentUser.backupMove());
    }
}