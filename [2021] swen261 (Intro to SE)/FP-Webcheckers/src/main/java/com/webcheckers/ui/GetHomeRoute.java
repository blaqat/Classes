package com.webcheckers.ui;

import java.util.*;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.TournamentCenter;
import com.webcheckers.model.GamePlayer;
import com.webcheckers.model.Player;
import com.webcheckers.model.Tournament;
import com.webcheckers.model.GamePlayer.PlayerType;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author<a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class GetHomeRoute implements Route {

  public static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(PlayerLobby playerLobby, TournamentCenter tournamentCenter, TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = playerLobby;
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {

    Player currentUser = request.session().attribute("currentUser");

    //Redirects if the current player is in a game already
    if (currentUser != null && currentUser.isPlayingGame()) {
      response.redirect("/game");
      return null;
    }

    if (currentUser != null && currentUser.getCurrentTournament() != null) {
      response.redirect("/tournament");
      return null;
    }

    //RENDER DATA
    Map<String, Object> vm = new HashMap<String, Object> ();
    List<GamePlayer> onlinePlayers = playerLobby.getOnlinePlayers();
    onlinePlayers.remove(currentUser);
    List<GamePlayer> availiablePlayers = new ArrayList<>();
    for (GamePlayer player: onlinePlayers) {
      if (!player.isPlayingGame() && (player.getType() == PlayerType.ROBOT || ((Player) player).getCurrentTournament() == null)) {
        availiablePlayers.add(player);
      }
    }
    Collections.sort(availiablePlayers);
    Collections.reverse(availiablePlayers);
    Set<Tournament> invitations = new HashSet<>();
    if (currentUser != null) {
      invitations = currentUser.getInvitations();
    }
    vm.put("title", "Welcome!");
    vm.put("message", WELCOME_MSG);
    vm.put("currentUser", currentUser);
    vm.put("totalPlayers", onlinePlayers.size());
    vm.put("playerLobby", availiablePlayers);
    vm.put("inviteList", invitations);
    vm.put("lobbySize", availiablePlayers.size());
    vm.put("inviteCount", invitations.size());
    return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}