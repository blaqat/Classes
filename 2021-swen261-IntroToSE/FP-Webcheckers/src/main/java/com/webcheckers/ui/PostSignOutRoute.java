package com.webcheckers.ui;

import com.webcheckers.application.*;
import com.webcheckers.model.Player;
import spark.*;

/**
 * UI Post for signout
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostSignOutRoute implements Route {
    private final PlayerLobby playerLobby;
    private final TournamentCenter tournamentCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignOutRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, final TemplateEngine templateEngine) {
        this.playerLobby = playerLobby;
        this.tournamentCenter = tournamentCenter;
    }

    /**
     * Processes the User Sign Out
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     */
    @Override
    public Object handle(Request request, Response response) {

        //Unbinds the session from the object
        //redirects to homepage as new user
        Player currentUser = request.session().attribute("currentUser");
        tournamentCenter.leaveTournament(currentUser);
        playerLobby.signOut(request.session());
        response.redirect("/");
        return null;
    }
}