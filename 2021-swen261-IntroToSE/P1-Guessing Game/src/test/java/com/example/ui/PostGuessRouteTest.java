package com.example.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.example.appl.GameCenter;
import com.example.appl.PlayerServices;
import com.example.model.GuessGame;
import com.example.model.GuessGame.GuessResult;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("UI-tier")
public class PostGuessRouteTest {

  private static final int NUMBER = 7;
  private static final int WRONG_GUESS = 3;
  private static final String WRONG_GUESS_STR = Integer.toString(WRONG_GUESS);
  private static final String NOT_A_NUMBER = "asdf";
  private static final int INVALID_GUESS = 47;
  private static final String INVALID_GUESS_STR = Integer.toString(INVALID_GUESS);

  /**
   * The component-under-test (CuT).
   *
   * <p>
   * This is a stateless component so we only need one.
   * The {@link GameCenter} component is thoroughly tested so
   * we can use it safely as a "friendly" dependency.
   */
  private PostGuessRoute CuT;

  // friendly objects
  private GameCenter gameCenter;
  private GuessGame game;

  // attributes holding mock objects
  private Request request;
  private Session session;
  private Response response;
  private TemplateEngine engine;
  private PlayerServices playerSvc;

  /**
   * Setup new mock objects for each test.
   */
  @BeforeEach
  public void setup() {
    request = mock(Request.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    engine = mock(TemplateEngine.class);
    response = mock(Response.class);

    // build the Service and Model objects
    // the GameCenter and GuessingGame are friendly
    gameCenter = new GameCenter();
    game = new GuessGame(NUMBER);
    // but mock up the PlayerService
    playerSvc = mock(PlayerServices.class);
    when(playerSvc.currentGame()).thenReturn(game);
    // store in the Session
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerSvc);

    // create a unique CuT for each test
    CuT = new PostGuessRoute(gameCenter, engine);
  }

  /**
   * Test that the "guess" action handled bad guesses: not a number
   */
  @Test
  public void bad_guess_1() {
    // Arrange the test scenario: The user's guess is not valid number.
    when(request.queryParams(eq(PostGuessRoute.GUESS_PARAM))).thenReturn(NOT_A_NUMBER);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Invoke the test
    CuT.handle(request, response);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
    testHelper.assertViewModelAttribute(
        GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.MESSAGE_TYPE_ATTR, PostGuessRoute.ERROR_TYPE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.MESSAGE_ATTR, PostGuessRoute.makeBadArgMessage(NOT_A_NUMBER));
    testHelper.assertViewModelAttributeIsAbsent(PostGuessRoute.YOU_WON_ATTR);
    //   * test view name
    testHelper.assertViewName(GetGameRoute.VIEW_NAME);
  }

  /**
   * Test that the "guess" action handled bad guesses: out of range
   */
  @Test
  public void bad_guess_2() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq(PostGuessRoute.GUESS_PARAM))).thenReturn(INVALID_GUESS_STR);
    when(playerSvc.makeGuess(47)).thenReturn(GuessResult.INVALID);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Invoke the test
    CuT.handle(request, response);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
    testHelper.assertViewModelAttribute(
        GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.MESSAGE_TYPE_ATTR, PostGuessRoute.ERROR_TYPE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.MESSAGE_ATTR, PostGuessRoute.makeInvalidArgMessage(INVALID_GUESS_STR));
    testHelper.assertViewModelAttributeIsAbsent(PostGuessRoute.YOU_WON_ATTR);
    //   * test view name
    testHelper.assertViewName(GetGameRoute.VIEW_NAME);
  }

  /**
   * Test that the "guess" action handled bad guesses: bad guess
   */
  @Test
  public void bad_guess_3() {
    // Arrange the test scenario: The user's guess is a valid number but is incorrect.
    when(request.queryParams(eq(PostGuessRoute.GUESS_PARAM))).thenReturn(WRONG_GUESS_STR);
    when(playerSvc.makeGuess(3)).thenReturn(GuessResult.WRONG);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Invoke the test
    CuT.handle(request, response);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
    testHelper.assertViewModelAttribute(
        GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.MESSAGE_TYPE_ATTR, PostGuessRoute.ERROR_TYPE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.MESSAGE_ATTR, PostGuessRoute.BAD_GUESS);
    testHelper.assertViewModelAttributeIsAbsent(PostGuessRoute.YOU_WON_ATTR);
    //   * test view name
    testHelper.assertViewName(GetGameRoute.VIEW_NAME);
  }

  /**
   * Test win conditions.
   */
  @Test
  public void you_win() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq(PostGuessRoute.GUESS_PARAM))).thenReturn(Integer.toString(NUMBER));
    when(playerSvc.makeGuess(NUMBER)).thenReturn(GuessResult.WON);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Invoke the test
    CuT.handle(request, response);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
    testHelper.assertViewModelAttribute(
        GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.YOU_WON_ATTR, Boolean.TRUE);
    //   * test view name
    testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
  }

  /**
   * Test loosing conditions.
   */
  @Test
  public void you_loose() {
    // Arrange the test scenario
    when(request.queryParams(PostGuessRoute.GUESS_PARAM)).thenReturn(WRONG_GUESS_STR);
    when(playerSvc.makeGuess(WRONG_GUESS)).thenReturn(GuessResult.LOST);
    // make two guesses, as if these were previous guesses
    game.makeGuess(WRONG_GUESS);
    game.makeGuess(WRONG_GUESS);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Invoke the test
    CuT.handle(request, response);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
    testHelper.assertViewModelAttribute(
        GetHomeRoute.NEW_PLAYER_ATTR, Boolean.FALSE);
    testHelper.assertViewModelAttribute(
        PostGuessRoute.YOU_WON_ATTR, Boolean.FALSE);
    //   * test view name
    testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
  }

  /**
   * Test that CuT redirects to the Home view when a @Linkplain(PlayerServices) object does
   * not exist, i.e. the session timed out or an illegal request on this URL was received.
   */
  @Test
  public void faulty_session() {
    // Arrange the test scenario: There is an existing session with a PlayerServices object
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(null);

    // Invoke the test
    try {
      CuT.handle(request, response);
      fail("Redirects invoke halt exceptions.");
    } catch (HaltException e) {
      // expected
    }

    // Analyze the results:
    //   * redirect to the Game view
    verify(response).redirect(WebServer.HOME_URL);
  }
}
