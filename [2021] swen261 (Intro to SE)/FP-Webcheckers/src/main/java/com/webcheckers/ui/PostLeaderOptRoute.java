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
 * The UI Controller to opt out leader from tournament.
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostLeaderOptRoute implements Route {
    /*
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostLeaderOptRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, final TemplateEngine templateEngine, Gson gson) {}

    /**
     * processes the leader sit out option
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
        if (tournament.getParticipants().contains(currentUser)) {
            tournament.leave(currentUser);
        } else {
            tournament.join(currentUser);
        }
        response.redirect("/tournament");
        return null;
    }
}