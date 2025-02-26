package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.TournamentCenter;
import com.webcheckers.model.*;
import com.webcheckers.model.GamePlayer.PlayerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class GetHomeRouteTest {

    private static Session session1;
    private static Request request1;
    private static Response response;
    private static TemplateEngine engine;
    private static PlayerLobby lobby;
    private static TemplateEngineTester testHelper;
    private static TournamentCenter tournamentCenter;
    @BeforeEach
    public void createMock(){
        lobby = new PlayerLobby();
        new Gson();
        new GameCenter();
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
    public void testGetHomepage(){
        GetHomeRoute testRoute = new GetHomeRoute(lobby, tournamentCenter, engine);
        Player currentUser = session1.attribute("currentUser");
        List<GamePlayer> onlinePlayers = lobby.getOnlinePlayers();
        onlinePlayers.remove(currentUser);
        List<GamePlayer> availiablePlayers = new ArrayList<>();
        for(GamePlayer player : onlinePlayers) {
            if ( !player.isPlayingGame() && ( player.getType() == PlayerType.ROBOT || ((Player)player).getCurrentTournament() == null)) {
                availiablePlayers.add(player);
            }
        }
        Collections.sort(availiablePlayers);
        Collections.reverse(availiablePlayers);
        Set<Tournament> invitations = new HashSet<>();
        if(currentUser != null){
            invitations = currentUser.getInvitations();
        }
        testRoute.handle(request1, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Welcome!");
        testHelper.assertViewModelAttribute("message", GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute("currentUser", currentUser);
        testHelper.assertViewModelAttribute("totalPlayers", onlinePlayers.size());
        testHelper.assertViewModelAttribute("playerLobby", availiablePlayers);
        testHelper.assertViewModelAttribute("inviteList", invitations);
        testHelper.assertViewModelAttribute("lobbySize", availiablePlayers.size());
        testHelper.assertViewModelAttribute("inviteCount", invitations.size());
    }
}
