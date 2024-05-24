package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.TournamentCenter;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class PostLeaveTournamentRouteTest {

    private static Session session;
    private static Request request;
    private static Response response;
    private static TemplateEngine engine;
    private static PlayerLobby lobby;
    private static TournamentCenter tournamentCenter;

    @BeforeEach
    public void createMock(){
        tournamentCenter = new TournamentCenter(new GameCenter());
        lobby = new PlayerLobby();
        session = mock(Session.class);
        request = mock(Request.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        Player player = new Player("Player1",null);
        tournamentCenter.createTournament(player);
        when(request.session()).thenReturn(session);
        when(session.attribute("currentUser")).thenReturn(player);
    }

    @Test
    public void testLeaveTournament(){
        PostLeaveTournamentRoute testRoute = new PostLeaveTournamentRoute(lobby, tournamentCenter, engine);
        Player player = request.session().attribute("currentUser");
        assertTrue(player.getCurrentTournament() != null);
        testRoute.handle(request, response);
        assertTrue(player.getCurrentTournament() == null);
    }
}
