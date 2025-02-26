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
public class PostSignOutRouteTest {

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
        lobby.signIn("Player1", session);
        Player player = (Player)lobby.getPlayer("Player1");
        tournamentCenter.createTournament(player);
        when(request.session()).thenReturn(session);
        when(session.attribute("currentUser")).thenReturn(player);
    }

    @Test
    public void testSignOut(){
        PostSignOutRoute testRoute = new PostSignOutRoute(lobby, tournamentCenter, engine);
        Player player = (Player)lobby.getPlayer("Player1");
        assertNotNull(player.getCurrentTournament());
        assertTrue(lobby.getOnlinePlayers().contains(player));
        testRoute.handle(request, response);
        assertNull(player.getCurrentTournament());
        assertFalse(lobby.getOnlinePlayers().contains(player));
    }
}
