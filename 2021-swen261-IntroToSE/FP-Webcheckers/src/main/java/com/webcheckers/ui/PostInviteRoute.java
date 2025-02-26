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
 * The UI Controller invite player to tournament
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostInviteRoute implements Route {
    private final PlayerLobby playerLobby;
    /*
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostInviteRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, final TemplateEngine templateEngine, Gson gson) {
        this.playerLobby = playerLobby;
    }

    /**
     * processes the tournament invitation
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
        String inviteeName = request.queryParams("invitee");
        Player invitee = (Player) playerLobby.getPlayer(inviteeName);
        invitee.invite(tournament);
        response.redirect("/tournament");
        return null;
    }

}