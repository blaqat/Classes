package com.webcheckers.ui;

import java.util.*;
import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.model.CheckersGame.GameState;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The UI Controller to GET the Home page.
 *
 * @author<a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class GetGameViewRoute implements Route {
  static final String PLAYERSERVICES_KEY = "playerServices";
  static final String VIEW_NAME = "game.ftl";

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  private final Gson gson;
  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameViewRoute(PlayerLobby playerLobby, final TemplateEngine templateEngine, Gson gson) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = playerLobby;
    this.gson = gson;
  }

  /**
   * Render the WebCheckers Game Page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Game View
   */
  @Override
  public Object handle(Request request, Response response) {

    Player currentPlayer = request.session().attribute("currentUser");
    CheckersGame currentGame = currentPlayer.getGameSession();
    currentPlayer.clearInvites();

    // Redirects to home if there is no game being played
    if (!currentPlayer.isPlayingGame()) {
      response.redirect("/");
      return null;
    }

    GamePlayer redPlayer = currentGame.getRedPlayer();
    GamePlayer whitePlayer = currentGame.getWhitePlayer();
    BoardView board = currentGame.getBoard();
    GamePlayer opponent = whitePlayer;

    //mirror board
    if (currentPlayer == whitePlayer) {
      board = board.getFlippedBoard();
      opponent = redPlayer;
    }
    final Map<String, Object> modeOptions = new HashMap<>(2);

    //RENDER DATA

    Map<String, Object> vm = new HashMap<>();
    //Checks the two game over conditions at the beginning of every turn.
    
    Color loser = currentGame.endGame();
    Color crippled = currentGame.playerCrippled();
    //The game state changes in end game or player crippled or if a player has resigned.
    switch (currentGame.getGameState()) {
      case ACTIVE:
        break;
        //If a player was crippled then gets the player that was and displays the proper message.
      case OVER_CRIPPLE:
        System.out.println("Player has been crippled");
        currentPlayer.exitGame(!currentPlayer.getColor().equals(crippled), playerLobby);
        currentGame.switchTurn();
        if (opponent.getType() == GamePlayer.PlayerType.ROBOT) {
          opponent.exitGame(currentPlayer.getColor().equals(crippled), playerLobby);
        }

        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", ((crippled == Color.WHITE) ? "White" : "Red") + " has no valid moves.");
        break;
        //
      case OVER_ENDED:
        currentPlayer.exitGame(!currentPlayer.getColor().equals(loser), playerLobby);
        currentGame.switchTurn();
        if (opponent.getType() == GamePlayer.PlayerType.ROBOT) {
          opponent.exitGame(currentPlayer.getColor().equals(loser), playerLobby);
        }
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", ((loser == Color.WHITE) ? "Red" : "White") + " has captured all the pieces.");
        break;

      case OVER_RESIGN:
        currentPlayer.exitGame(true, playerLobby);

        if (opponent.getType() == GamePlayer.PlayerType.ROBOT) {
          opponent.exitGame(false, playerLobby);
        }
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", ((currentPlayer.getColor() == Color.WHITE) ? "Red" : "White") + " has resigned.");
        break;
    }

    //Set winning message for winner!
    if (Objects.nonNull(currentGame.getWinner()) && currentGame.getWinner().equals((GamePlayer) currentPlayer)) {
      modeOptions.put("gameOverMessage", (modeOptions.get("gameOverMessage") + "\t CONGRATULATIONS! YOU HAVE WON!"));
    }
    modeOptions.put("gameOverMessage", (modeOptions.get("gameOverMessage") + "").replaceAll(currentPlayer.getColor() == Color.RED ? "Red" : "White", "You"));
    vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));

    vm.put("title", "GAMETIME!!!!");
    vm.put("message", GetHomeRoute.WELCOME_MSG);
    vm.put("currentUser", currentPlayer);
    vm.put("viewMode", GameView.PLAY);
    //vm.put("modeOptionsAsJSON",)
    vm.put("redPlayer", redPlayer);
    vm.put("whitePlayer", whitePlayer);
    vm.put("activeColor", currentGame.getActiveColor());
    vm.put("board", board);
    return templateEngine.render(new ModelAndView(vm, "game.ftl"));
  }
}