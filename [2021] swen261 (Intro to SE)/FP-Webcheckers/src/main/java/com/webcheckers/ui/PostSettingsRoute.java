package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.TournamentCenter;
import com.webcheckers.model.*;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The UI Controller for changing max players
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostSettingsRoute implements Route {
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSettingsRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, final TemplateEngine templateEngine, Gson gson) {}

    /**
     * processes the player max change
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   null
     */
    @Override
    public Object handle(Request request, Response response) {
        Player currentUser = request.session().attribute("currentUser");
        Tournament tournament = currentUser.getCurrentTournament();
        String action = request.queryParams("change");
        if (action.equals("seed")) {
            tournament.seed();
        } else {
            double value = 2;
            if (action.equals("decrease")) {
                value = 0.5;
            }
            tournament.setMaxPlayers((int)((double) tournament.getMaxPlayers() * value));
        }
        response.redirect("/tournament");
        return null;
    }
}