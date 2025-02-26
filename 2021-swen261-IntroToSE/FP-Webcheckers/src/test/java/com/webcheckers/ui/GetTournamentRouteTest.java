package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.TournamentCenter;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class GetTournamentRouteTest {

    private static Session session1;
    private static Request request1;
    private static Response response;
    private static TemplateEngine engine;
    private static PlayerLobby lobby;
    private static TemplateEngineTester testHelper;
    private static Gson gson;
    private static TournamentCenter tournamentCenter;
    private static GameCenter center;

    @BeforeEach
    public void createMock(){
        lobby = new PlayerLobby();
        gson = new Gson();
        center = new GameCenter();
        tournamentCenter = new TournamentCenter(new GameCenter());
        session1 = mock(Session.class);
        request1 = mock(Request.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        Player player1 = new Player("Player1",null);
        when(session1.attribute("currentUser")).thenReturn(player1);
        when(request1.session()).thenReturn(session1);
        testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    }

    @Test
    public void testCreateTournament(){
        GetTournamentRoute testRoute = new GetTournamentRoute(lobby, engine, gson);
        Player player = session1.attribute("currentUser");
        tournamentCenter.createTournament(player);
        testRoute.handle(request1, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Tournament");
    }

    @Test
    public void testGetWhileInGame(){
        GetTournamentRoute testRoute = new GetTournamentRoute(lobby, engine, gson);
        Player player = session1.attribute("currentUser");
        center.initializeGame(player, new Player("test", null));
        testRoute.handle(request1, response);
        assertNull(player.getCurrentTournament());
    }
}
