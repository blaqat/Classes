package com.example.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.example.appl.GameCenter;
import com.example.appl.PlayerServices;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

/**
 * The unit test suite for the {@link GetGameRoute} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("UI-tier")
public class GetGameRouteTest {

  /**
   * The component-under-test (CuT).
   */
  private GetGameRoute CuT;

  private Request request;
  private Session session;
  private Response response;
  private TemplateEngine engine;

  /**
   * Setup new mock objects for each test.
   */
  @BeforeEach
  public void setup() {
    request = mock(Request.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    response = mock(Response.class);
    engine = mock(TemplateEngine.class);

    // create a unique CuT for each test
    CuT = new GetGameRoute(engine);
  }

  /**
   * Test that the Game view will create a new game if none exists.
   */
  @Test
  public void new_game() {
    // Arrange the test scenario: The session holds no game.
    final GameCenter center = new GameCenter();
    final PlayerServices playerSvc = center.newPlayerServices();
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerSvc);

    // To analyze what the Route created in the View-Model map you need
    // to be able to extract the argument to the TemplateEngine.render method.
    // Mock up the 'render' method by supplying a Mockito 'Answer' object
    // that captures the ModelAndView data passed to the template engine
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    // Invoke the test (ignore the output)
    CuT.handle(request, response);

    // Analyze the content passed into the render method
    //   * model is a non-null Map
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();
    //   * model contains all necessary View-Model data
    testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
    testHelper.assertViewModelAttribute(GetGameRoute.GAME_BEGINS_ATTR, Boolean.TRUE);
    //   * test view name
    testHelper.assertViewName(GetGameRoute.VIEW_NAME);
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
