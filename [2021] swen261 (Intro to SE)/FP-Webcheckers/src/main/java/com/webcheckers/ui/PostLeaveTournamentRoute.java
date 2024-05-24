package com.webcheckers.ui;

import com.webcheckers.application.*;

import com.webcheckers.model.Player;
import spark.*;

/**
 * UI Post for leaving tournament
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostLeaveTournamentRoute implements Route {
    private final TournamentCenter tournamentCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostLeaveTournamentRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, final TemplateEngine templateEngine) {
        this.tournamentCenter = tournamentCenter;
    }

    /**
     * Processes leaving a tournament
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     */
    @Override
    public Object handle(Request request, Response response) {
        Player currentPlayer = request.session().attribute("currentUser");
        tournamentCenter.leaveTournament(currentPlayer);
        response.redirect("/");
        return null;
    }
}