package com.webcheckers.ui;

import java.util.*;
import com.google.gson.Gson;
import com.webcheckers.application.*;

import spark.*;

import com.webcheckers.model.*;

/**
 * Post route for board preset
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Maxwell Shenk</a>
 */
public class GetPresetRoute implements Route {
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetPresetRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    /**
     * Processes the preset
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
        currentUser.newTurn();
        currentUser.getGameSession().getBoard().preset(Integer.parseInt(request.splat()[0]));
        response.redirect("/game");
        return null;
    }
}