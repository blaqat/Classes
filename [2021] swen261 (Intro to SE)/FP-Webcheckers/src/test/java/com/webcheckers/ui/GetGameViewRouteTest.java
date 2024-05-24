package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class GetGameViewRouteTest {

    private static Session session;
    private static Request request;
    private static Response response;
    private static TemplateEngine engine;
    private static PlayerLobby lobby;
    private static Gson gson;
    private static GameCenter center;
    private static TemplateEngineTester testHelper;

    @BeforeEach
    public void createMock(){
        lobby = new PlayerLobby();
        gson = new Gson();
        center = new GameCenter();
        session = mock(Session.class);
        request = mock(Request.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        when(request.session()).thenReturn(session);
        testHelper = new TemplateEngineTester();
        Player red = new Player("red", null);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("currentUser")).thenReturn(red);
    }

    @Test
    public void testInGameView(){
        GetGameViewRoute testRoute = new GetGameViewRoute(lobby, engine, gson);
        Player red = session.attribute("currentUser");
        Player white = new Player("red", null);
        CheckersGame currentGame = center.initializeGame(red, white);
        testRoute.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "GAMETIME!!!!");
        testHelper.assertViewModelAttribute("message", GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute("currentUser", red);
        testHelper.assertViewModelAttribute("viewMode", GameView.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", red);
        testHelper.assertViewModelAttribute("whitePlayer", white);
        testHelper.assertViewModelAttribute("activeColor", currentGame.getActiveColor());
        testHelper.assertViewModelAttribute("board", currentGame.getBoard());
    }

    @Test
    public void testResignView(){
        GetGameViewRoute testRoute = new GetGameViewRoute(lobby, engine, gson);
        Player red = session.attribute("currentUser");
        Player white = new Player("red", null);
        CheckersGame currentGame = center.initializeGame(red, white);
        currentGame.setGameState(CheckersGame.GameState.OVER_RESIGN);
        white.exitGame(false, lobby);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage",( (red.getColor() == Color.WHITE) ? "Red" : "White") + " has resigned.");
        testRoute.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "GAMETIME!!!!");
        testHelper.assertViewModelAttribute("message", GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute("currentUser", red);
        testHelper.assertViewModelAttribute("viewMode", GameView.PLAY);
        testHelper.assertViewModelAttribute("redPlayer", red);
        testHelper.assertViewModelAttribute("whitePlayer", white);
        testHelper.assertViewModelAttribute("activeColor", currentGame.getActiveColor());
        testHelper.assertViewModelAttribute("board", currentGame.getBoard());
        testHelper.assertViewModelAttribute("modeOptionsAsJSON", gson.toJson(modeOptions));
    }
}
