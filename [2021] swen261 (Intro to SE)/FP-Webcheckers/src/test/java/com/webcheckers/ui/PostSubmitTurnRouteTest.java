package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class PostSubmitTurnRouteTest {

    private static Session session;
    private static Request request;
    private static Response response;
    private static TemplateEngine engine;
    private static GameCenter center;
    private static PlayerLobby lobby;
    private static Gson gson;

    @BeforeEach
    public void createMock(){
        center = new GameCenter();
        lobby = new PlayerLobby();
        gson = new Gson();
        session = mock(Session.class);
        request = mock(Request.class);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        Player player1 = new Player("Player1",null);
        Player player2 = new Player("Player2",null);
        CheckersGame game = new CheckersGame(0, player1, player2);
        player1.joinGame(game, Color.RED);
        player2.joinGame(game, Color.WHITE);
        when(request.session()).thenReturn(session);
        when(session.attribute("currentUser")).thenReturn(player1);
    }

    @Test
    public void testValidTurnChange(){
        PostSubmitTurnRoute testRoute = new PostSubmitTurnRoute(center, lobby, engine, gson);
        Player player = request.session().attribute("currentUser");
        Move red = new Move(new Position(5, 2), new Position(4, 3));
        player.move(red);
        String json = (String) testRoute.handle(request, response);
        Message message = gson.fromJson(json, Message.class);
        String actual = message.getText();
        String expected = "true";
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidTurnChange(){
        PostSubmitTurnRoute testRoute = new PostSubmitTurnRoute(center, lobby, engine, gson);
        Player player = request.session().attribute("currentUser");
        CheckersGame game = player.getGameSession();
        Move red1 = new Move(new Position(5, 2), new Position(4, 3));
        Move white1 = new Move(new Position(2, 1), new Position(3, 2));
        Move red2 = new Move(new Position(6, 1), new Position(5, 2));
        Player player1 = (Player)game.getRedPlayer();
        Player player2 = (Player)game.getWhitePlayer();
        player1.move(red1);
        player1.validateTurn();
        player2.move(white1);
        player2.validateTurn();
        player1.move(red2);
        String json = (String) testRoute.handle(request, response);
        Message message = gson.fromJson(json, Message.class);
        String actual = message.getText();
        String expected = "Invalid turn. When at least one jump is available, you must perform a jump.";
        assertEquals(expected, actual);
    }

}
