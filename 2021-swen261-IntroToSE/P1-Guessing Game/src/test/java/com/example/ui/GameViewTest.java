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
public class GameViewTest {

  private static final String TITLE = "MyTitle";
  private static final String TITLE_HEAD_TAG = "<title>" + TITLE + "</title>";
  private static final String TITLE_H1_TAG = "<h1>" + TITLE + "</h1>";

  private static final String GAME_HEADING_1ST_TAG = "<h4>Make a Guess</h4>";
  private static final String GAME_HEADING_TAG = "<h4>Make Another Guess</h4>";

  private static final String BAD_GUESS_MSG = "Bad guess.";

  /**
   * Make the HTML text for a div element that holds a user message.
   */
  private static String makeMessageTag(final String text, final String type) {
    return String.format("<div class=\"message %s\">%s</div>", type, text);
  }

  /**
   * Make the message text for how many guesses are left.
   */
  private static String makeGuessesLeftMsg(final int guessesLeft) {
    if (guessesLeft > 1) {
      return String.format("You have %d guesses left.", guessesLeft);
    } else {
      return "You have 1 guess left.";
    }
  }

  private final TemplateEngine engine = new FreeMarkerEngine();

  /**
   * Test that the Game view displays when the game is brand new (no guesses made).
   */
  @Test
  public void new_game() {
    // Arrange test
    final Map<String, Object> vm = new HashMap<>();
    final ModelAndView modelAndView = new ModelAndView(vm, GetGameRoute.VIEW_NAME);
    // setup View-Model for a new player
    vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
    vm.put(GetGameRoute.GAME_BEGINS_ATTR, true);
    vm.put(GetGameRoute.GUESSES_LEFT_ATTR, 3);

    // Invoke test
    final String viewHtml = engine.render(modelAndView);

    // Analyze results
    //    * look for Title elements
    assertTrue(viewHtml.contains(TITLE_HEAD_TAG), "Title head tag exists.");
    assertTrue(viewHtml.contains(TITLE_H1_TAG), "Title heading tag exists.");
    //    * look for the Game heading
    assertTrue(viewHtml.contains(GAME_HEADING_1ST_TAG), "The Game heading tag exists.");
    //    * look for the 'Guesses left' message
    assertTrue(viewHtml.contains(makeGuessesLeftMsg(3)), "Guesses left message exists.");
  }

  /**
   * Test that the Game view displays when the guess was bad.
   */
  @Test
  public void bad_data_message() {
    // Arrange test
    final Map<String, Object> vm = new HashMap<>();
    final ModelAndView modelAndView = new ModelAndView(vm, GetGameRoute.VIEW_NAME);
    // setup View-Model for a new player
    vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
    vm.put(GetGameRoute.GAME_BEGINS_ATTR, false);
    vm.put(PostGuessRoute.MESSAGE_ATTR, BAD_GUESS_MSG);
    vm.put(PostGuessRoute.MESSAGE_TYPE_ATTR, PostGuessRoute.ERROR_TYPE);
    vm.put(GetGameRoute.GUESSES_LEFT_ATTR, 2);

    // Invoke test
    final String viewHtml = engine.render(modelAndView);

    // Analyze results
    //    * look for Title elements
    assertTrue(viewHtml.contains(TITLE_HEAD_TAG), "Title head tag exists.");
    assertTrue(viewHtml.contains(TITLE_H1_TAG), "Title heading tag exists.");
    //    * look for the Game heading
    assertTrue(viewHtml.contains(GAME_HEADING_TAG), "The Game heading tag exists.");
    //    * look for the error message
    assertTrue(viewHtml.contains(makeMessageTag(BAD_GUESS_MSG, PostGuessRoute.ERROR_TYPE)), "Error message tag exists.");
    //    * look for the 'Guesses left' message
    assertTrue(viewHtml.contains(makeGuessesLeftMsg(2)), "Guesses left message exists.");
  }

  /**
   * Test that the Game view displays when the guess was wrong.
   */
  @Test
  public void first_bad_guess() {
    // Arrange test
    final Map<String, Object> vm = new HashMap<>();
    final ModelAndView modelAndView = new ModelAndView(vm, GetGameRoute.VIEW_NAME);
    // setup View-Model for a new player
    vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
    vm.put(GetGameRoute.GAME_BEGINS_ATTR, false);
    vm.put(PostGuessRoute.MESSAGE_ATTR, PostGuessRoute.BAD_GUESS);
    vm.put(PostGuessRoute.MESSAGE_TYPE_ATTR, PostGuessRoute.ERROR_TYPE);
    vm.put(GetGameRoute.GUESSES_LEFT_ATTR, 2);

    // Invoke test
    final String viewHtml = engine.render(modelAndView);

    // Analyze results
    //    * look for Title elements
    assertTrue(viewHtml.contains(TITLE_HEAD_TAG), "Title head tag exists.");
    assertTrue(viewHtml.contains(TITLE_H1_TAG), "Title heading tag exists.");
    //    * look for the Game heading
    assertTrue(viewHtml.contains(GAME_HEADING_TAG), "The Game heading tag exists.");
    //    * look for the error message
    assertTrue(viewHtml.contains(makeMessageTag(PostGuessRoute.BAD_GUESS, PostGuessRoute.ERROR_TYPE)), "Error message tag exists.");
    //    * look for the 'Guesses left' message
    assertTrue(viewHtml.contains(makeGuessesLeftMsg(2)), "Guesses left message exists.");
  }

}
