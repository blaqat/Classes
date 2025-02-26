package com.example.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The unit test suite for the {@code home.ftl} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("UI-tier")
public class HomeViewTest {

  private static final String TITLE = "MyTitle";
  private static final String TITLE_HEAD_TAG = "<title>" + TITLE + "</title>";
  private static final String TITLE_H1_TAG = "<h1>" + TITLE + "</h1>";

  private static final String GAME_STATS_MSG = "no message";

  private static final String NEW_GAME_LINK_TAG = "<a href=\"/game\">Want to play a game?!?</a>";
  private static final String YOU_WIN_TEXT = "Congratulations!";
  private static final String YOU_WIN_GAME_LINK_TAG = "<a href=\"/game\">Do it again</a>";
  private static final String YOU_LOSE_TEXT = "Aww, too bad.";
  private static final String YOU_LOSE_GAME_LINK_TAG = "<a href=\"/game\">How about it?</a>";

  private final TemplateEngine engine = new FreeMarkerEngine();

  /**
   * Test that the Home view displays when the player is new.
   */
  @Test
  public void new_player() {
    // Arrange test
    final Map<String, Object> vm = new HashMap<>();
    final ModelAndView modelAndView = new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    // setup View-Model for a new player
    vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
    vm.put(GetHomeRoute.NEW_PLAYER_ATTR, true);
    vm.put(GetHomeRoute.GAME_STATS_MSG_ATTR, GAME_STATS_MSG);

    // Invoke test
    final String viewHtml = engine.render(modelAndView);

    // Analyze results
    //    * look for Title elements
    assertTrue(viewHtml.contains(TITLE_HEAD_TAG), "Title head tag exists.");
    assertTrue(viewHtml.contains(TITLE_H1_TAG), "Title heading tag exists.");
    //    * look for the Game stats message
    assertTrue(viewHtml.contains(GAME_STATS_MSG), "The Game stats msg text exists.");
    //    * look for the Game link
    assertTrue(viewHtml.contains(NEW_GAME_LINK_TAG), "Game link exists.");
  }

  /**
   * Test that the Home view displays when the player has won a game.
   */
  @Test
  public void player_wins() {
    // Arrange test
    final Map<String, Object> vm = new HashMap<>();
    final ModelAndView modelAndView = new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    // setup View-Model for a new player
    vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
    vm.put(GetHomeRoute.NEW_PLAYER_ATTR, false);
    vm.put(PostGuessRoute.YOU_WON_ATTR, true);
    vm.put(GetHomeRoute.GAME_STATS_MSG_ATTR, GAME_STATS_MSG);

    // Invoke test
    final String viewHtml = engine.render(modelAndView);

    // Analyze results
    //    * look for Title elements
    assertTrue(viewHtml.contains(TITLE_HEAD_TAG), "Title head tag exists.");
    assertTrue(viewHtml.contains(TITLE_H1_TAG), "Title heading tag exists.");
    //    * look for the Game stats message
    assertTrue(viewHtml.contains(GAME_STATS_MSG), "The Game stats msg text exists.");
    //    * look for the "kudos"
    assertTrue(viewHtml.contains(YOU_WIN_TEXT), "Player wins text exists.");
    //    * look for the Game link
    assertTrue(viewHtml.contains(YOU_WIN_GAME_LINK_TAG), "Game link exists.");
  }

  /**
   * Test that the Home view displays when the player has lost a game.
   */
  @Test
  public void player_loses() {
    // Arrange test
    final Map<String, Object> vm = new HashMap<>();
    final ModelAndView modelAndView = new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
    // setup View-Model for a new player
    vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
    vm.put(GetHomeRoute.NEW_PLAYER_ATTR, false);
    vm.put(PostGuessRoute.YOU_WON_ATTR, false);
    vm.put(GetHomeRoute.GAME_STATS_MSG_ATTR, GAME_STATS_MSG);

    // Invoke test
    final String viewHtml = engine.render(modelAndView);

    // Analyze results
    //    * look for Title elements
    assertTrue(viewHtml.contains(TITLE_HEAD_TAG), "Title head tag exists.");
    assertTrue(viewHtml.contains(TITLE_H1_TAG), "Title heading tag exists.");
    //    * look for the Game stats message
    assertTrue(viewHtml.contains(GAME_STATS_MSG), "The Game stats msg text exists.");
    //    * look for the "kudos"
    assertTrue(viewHtml.contains(YOU_LOSE_TEXT), "Player loses text exists.");
    //    * look for the Game link
    assertTrue(viewHtml.contains(YOU_LOSE_GAME_LINK_TAG), "Game link exists.");
  }

}
