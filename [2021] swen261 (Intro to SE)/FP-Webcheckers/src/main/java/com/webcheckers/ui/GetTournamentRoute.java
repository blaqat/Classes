package com.webcheckers.ui;

import java.util.*;
import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.model.GamePlayer.PlayerType;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The UI Controller to GET a tournament page.
 *
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class GetTournamentRoute implements Route {
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetTournamentRoute(PlayerLobby playerLobby, final TemplateEngine templateEngine, Gson gson) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
    }

    /**
     * Render the WebCheckers tournament page
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Tournament Bracket View
     */
    @Override
    public Object handle(Request request, Response response) {
        Player currentPlayer = request.session().attribute("currentUser");
        Tournament tournament = currentPlayer.getCurrentTournament();
        currentPlayer.clearInvites();

        if (currentPlayer.isPlayingGame()) {
            response.redirect("/game");
            return null;
        }
        if (tournament == null) {
            String leaderName = request.queryParams("leader");
            GamePlayer leader = playerLobby.getPlayer(leaderName);
            if (leader == null) {
                response.redirect("/");
                return null;
            }
            tournament = ((Player) leader).getCurrentTournament();
            if (tournament != null && !tournament.isInProgress()) {
                tournament.join(currentPlayer);
            } else {
                response.redirect("/");
                return null;
            }
        }

        List<GamePlayer> onlinePlayers = playerLobby.getOnlinePlayers();
        onlinePlayers.remove(currentPlayer);
        List<GamePlayer> availiablePlayers = new ArrayList<>();
        for (GamePlayer player: onlinePlayers) {
            if (!player.isPlayingGame() && (player.getType() != PlayerType.ROBOT && ((Player) player).getCurrentTournament() == null)) {
                availiablePlayers.add(player);
            }
        }
        boolean isLeader = tournament.getLeader() == currentPlayer;
        boolean leaderIsPlaying = tournament.getParticipants().contains(tournament.getLeader());
        boolean ready = tournament.getParticipants().size() == tournament.getMaxPlayers();

        //RENDER DATA
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Tournament");
        vm.put("isOver", tournament.isOver());
        vm.put("maxPlayers", tournament.getMaxPlayers());
        vm.put("participants", tournament.getParticipants());
        vm.put("currentUser", currentPlayer);
        vm.put("ready", ready);
        vm.put("winner", tournament.getWinner());
        vm.put("inProgress", tournament.isInProgress());
        vm.put("isLeader", isLeader);
        vm.put("areGames", tournament.getGamesInProgress().size() > 0);
        vm.put("bracket", tournament.getBracket());
        vm.put("playerLobby", availiablePlayers);
        vm.put("lobbySize", availiablePlayers.size());
        vm.put("leaderIsPlaying", leaderIsPlaying);
        vm.put("gamesInProgress", tournament.getGamesInProgress());
        vm.put("lobbyCount", tournament.getParticipants().size());
        if (currentPlayer == tournament.getWinner()) {
            currentPlayer.leaveTournament();
        }
        return templateEngine.render(new ModelAndView(vm, "tournament.ftl"));
    }
}