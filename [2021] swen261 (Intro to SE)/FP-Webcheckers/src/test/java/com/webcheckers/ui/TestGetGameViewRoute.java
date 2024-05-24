package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import spark.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class TestGetGameViewRoute {
    PlayerLobby playerLobby;
    TemplateEngine templateEngine;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Player currentPlayer;

    GetGameViewRoute testViewRoute;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        templateEngine = mock(TemplateEngine.class);
        currentPlayer = mock(Player.class);
        when(request.session().attribute("currentUser")).thenReturn(currentPlayer);

        //testViewRoute = new GetGameViewRoute(playerLobby, templateEngine,gson);
    }

//    @Test
//    public void testHandleNull() {
//        when (currentPlayer.getGameSession()).thenReturn(null);
//        assertEquals(null, testViewRoute.handle(request, response));
//    }

    public void testHandleValid() {
        final PlayerLobby playerLob = new PlayerLobby();
        when(session.attribute(GetGameViewRoute.PLAYERSERVICES_KEY)).thenReturn(playerLob);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        testViewRoute.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewName(GetGameViewRoute.VIEW_NAME);
        /*
        Map<String, Object> vm = new HashMap<>();

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        GetGameViewRoute t = mock(GetGameViewRoute.class);
        when(templateEngine.render(new ModelAndView(vm, "game.ftl"))).thenReturn(null);
        when(t.handle(request, response)).thenReturn(templateEngine.render(new ModelAndView(vm, "game.ftl")));
        assertEquals(t, testViewRoute.handle(request, response));*/
    }
}
