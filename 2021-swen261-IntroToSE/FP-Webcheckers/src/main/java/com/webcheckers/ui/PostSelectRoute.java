package com.webcheckers.ui;

import java.util.*;
import com.webcheckers.application.*;
import spark.*;
import com.webcheckers.model.*;
import com.webcheckers.model.GamePlayer.PlayerType;

/**
 * The UI Controller to Process the Username Input.
 *
 * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class PostSelectRoute implements Route {

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSelectRoute(GameCenter gameCenter, PlayerLobby playerLobby, TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
        this.gameCenter = gameCenter;
    }

    /**
     * Processes the Game initiation
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
        String opponentName = request.queryParams("anything");
        GamePlayer opponentPlayer = playerLobby.getPlayer(opponentName);

        // Redirects if current player is in game (case if page has not reloaded fast enough)
        if (currentUser.isPlayingGame()) {
            response.redirect("/game");
            return null;
        }

        // Redirects to home if selected player is in a game
        Map<String, Object> vm = new HashMap<>();
        if (opponentPlayer.isPlayingGame() || (opponentPlayer.getType() == PlayerType.HUMAN && ((Player) opponentPlayer).getCurrentTournament() != null)) {
            response.redirect("/");
            return null;
        }

        //success case
        //initializes a new game with currentUser and selected opponentPlayer
        CheckersGame game = gameCenter.initializeGame((GamePlayer) currentUser, opponentPlayer);
        if (opponentPlayer.getType() == GamePlayer.PlayerType.ROBOT) {
            opponentPlayer = game.createAI(Color.WHITE);
        }

        BoardView board = game.getBoard();
        vm.put("message", GetHomeRoute.WELCOME_MSG);
        vm.put("title", "GAMETIME!!!!");
        vm.put("currentUser", currentUser);
        vm.put("viewMode", GameView.PLAY);
        //vm.put("modeOptionsAsJSON",)
        vm.put("redPlayer", currentUser);
        vm.put("whitePlayer", opponentPlayer);
        vm.put("activeColor", Color.RED);
        vm.put("board", board);
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}