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
 * The UI Controller to GET a tournament page.
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostCreateTournamentRoute implements Route {
    private final TournamentCenter tournamentCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostCreateTournamentRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, final TemplateEngine templateEngine, Gson gson) {
        this.tournamentCenter = tournamentCenter;
    }

    /**
     * processes the tournament creation
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
        if (currentUser.isPlayingGame()) {
            response.redirect("/game");
            return null;
        }
        tournamentCenter.createTournament(currentUser);
        response.redirect("/tournament");
        return null;
    }
}