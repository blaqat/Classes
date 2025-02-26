package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.util.Message;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.TemplateEngine;
import spark.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier") @Testable
public class TestPostSignInRoute {
    final static String VALID_USERNAME = "I_AmAPlayer";
    final static String USED_NAME = "AlreadyUsed";
    final static String INVALID_USERNAME_NULL = "";
    final static String INVALID_USERNAME_SPACES = "     ";
    final static String INVALID_USERNAME_PUNCT = "\"Hello There\"";

    private PostSignInRoute CuT;

    private static Session session;
    private static Request request;
    private PlayerLobby playerLobby;
    private Response response;
    private TemplateEngine engine;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);
        playerLobby = mock(PlayerLobby.class);
        playerLobby.signIn(USED_NAME, session);

        when(request.session()).thenReturn(session);


        CuT = new PostSignInRoute(playerLobby, engine);
    }

  /**
   * Test that the "guess" action handled bad guesses: out of range
   */
  @Test
  public void invalid_user_1() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq("name"))).thenReturn(INVALID_USERNAME_NULL);
    when(playerLobby.signIn(INVALID_USERNAME_NULL, session)).thenReturn(PlayerLobby.NOT_CAPITAL);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    String signInResult = playerLobby.signIn(INVALID_USERNAME_NULL, session);

    // Invoke the test
    CuT.handle(request, response);
    assertEquals(signInResult, PlayerLobby.NOT_CAPITAL);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        "title", "Sign In");
    testHelper.assertViewModelAttribute(
        "message", Message.error(signInResult));
    //   * test view name
    testHelper.assertViewName("signin.ftl");
  }

    /**
   * Test that the "guess" action handled bad guesses: out of range
   */
  @Test
  public void invalid_user_2() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq("name"))).thenReturn(INVALID_USERNAME_PUNCT);
    when(playerLobby.signIn(INVALID_USERNAME_PUNCT, session)).thenReturn(PlayerLobby.ALPHA_NUM );

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    String signInResult = playerLobby.signIn(INVALID_USERNAME_PUNCT, session);

    // Invoke the test
    CuT.handle(request, response);

    assertEquals(signInResult, PlayerLobby.ALPHA_NUM );

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        "title", "Sign In");
    testHelper.assertViewModelAttribute(
        "message", Message.error(signInResult));
    //   * test view name
    testHelper.assertViewName("signin.ftl");
  }

    /**
   * Test that the "guess" action handled bad guesses: out of range
   */
  @Test
  public void invalid_user_3() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq("name"))).thenReturn(INVALID_USERNAME_SPACES);
    when(playerLobby.signIn(INVALID_USERNAME_SPACES, session)).thenReturn(PlayerLobby.ALPHA_NUM );

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    String signInResult = playerLobby.signIn(INVALID_USERNAME_SPACES, session);
    

    // Invoke the test
    CuT.handle(request, response);

    assertEquals(signInResult, PlayerLobby.ALPHA_NUM );

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        "title", "Sign In");
    testHelper.assertViewModelAttribute(
        "message", Message.error(signInResult));
    //   * test view name
    testHelper.assertViewName("signin.ftl");
  }

    /**
   * Test that the "guess" action handled bad guesses: out of range
   */
  @Test
  public void user_exists() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq("name"))).thenReturn(USED_NAME);
    when(playerLobby.signIn(USED_NAME, session)).thenReturn(PlayerLobby.USER_EXISTS);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    String signInResult = playerLobby.signIn(USED_NAME, session);

    // Invoke the test
    CuT.handle(request, response);

    assertEquals(signInResult, PlayerLobby.USER_EXISTS);

    // Analyze the results:
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(
        "title", "Sign In");
    testHelper.assertViewModelAttribute(
        "message", Message.error(signInResult));
    //   * test view name
    testHelper.assertViewName("signin.ftl");
  }

  /**
   * Test that the "guess" action handled bad guesses: out of range
   */
  @Test
  public void valid_user() {
    // Arrange the test scenario: The session holds a new game.
    when(request.queryParams(eq("name"))).thenReturn(VALID_USERNAME);
    when(playerLobby.signIn(VALID_USERNAME, session)).thenReturn(null);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    String signInResult = playerLobby.signIn(VALID_USERNAME, session);

    // Invoke the test
    CuT.handle(request, response);
    assertNull(signInResult);

  }

}
