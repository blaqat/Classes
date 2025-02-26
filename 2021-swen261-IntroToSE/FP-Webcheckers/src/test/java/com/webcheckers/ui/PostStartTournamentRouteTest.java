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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class PostStartTournamentRouteTest {

    private static Session session;
    private static Request request;
    private static Response response;
    private static TemplateEngine engine;
    private static PlayerLobby lobby;
    private static TournamentCenter tournamentCenter;
    private static Gson gson;

    @BeforeEach
    public void createMock(){
        tournamentCenter = new TournamentCenter(new GameCenter());
        lobby = new PlayerLobby();
        gson = new Gson();
        session = mock(Session.class);
        request = mock(Request.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        Player player = new Player("Player1",null);
        when(request.session()).thenReturn(session);
        when(session.attribute("currentUser")).thenReturn(player);
    }

    @Test
    public void testStartTournament(){
        PostStartTournamentRoute testRoute = new PostStartTournamentRoute(lobby, tournamentCenter, engine, gson);
        Player player = request.session().attribute("currentUser");
        tournamentCenter.createTournament(player);
        Player other1 = new Player("other1", session);
        Player other2 = new Player("other2", session);
        Player other3 = new Player("other3", session);
        Tournament tournament = player.getCurrentTournament();
        assertEquals(1, tournament.getParticipants().size());
        tournament.join(other1);
        tournament.join(other2);
        tournament.join(other3);
        assertEquals(4, tournament.getParticipants().size());
        testRoute.handle(request, response);
        assertTrue(tournament.isInProgress());
    }
}
