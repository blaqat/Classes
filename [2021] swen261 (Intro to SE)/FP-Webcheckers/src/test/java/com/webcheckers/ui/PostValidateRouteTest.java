package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.webcheckers.application.GameCenter;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class PostValidateRouteTest {
    PlayerLobby playerLobby;
    TemplateEngine templateEngine;

    private Request request;
    private Session session;
    private Response response;
    private Player currentPlayer;
    private Gson gson;

    PostValidateRoute testValidateRoute;
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        templateEngine = mock(TemplateEngine.class);
        currentPlayer = mock(Player.class);
        gson = new Gson();
        Player player1 = new Player("Player1", session);
        Player player2 = new Player("Player2", session);
        CheckersGame game = new CheckersGame(0, player1, player2);
        player1.joinGame(game, Color.RED);
        player2.joinGame(game, Color.WHITE);
        GameCenter gc = new GameCenter();
        when(request.session().attribute("currentUser")).thenReturn(currentPlayer);

        testValidateRoute = new PostValidateRoute(gc,playerLobby, templateEngine,gson);
    }
    @Test
    public void handleValid(){
        String moveString = request.queryParams("actionData");
        Move move = gson.fromJson(moveString, Move.class);
        Player currentUser = request.session().attribute("currentUser");

        //flips move coordinates if player is WHITE
        if(currentUser.getColor() == Color.WHITE){
            move = move.getFlipped();
        }
        //attempts the Ajax move through the move method and immediately returns a Message result
        String expected = gson.toJson(currentUser.move(move));
        String actual = (String)testValidateRoute.handle(request,response);
        assertEquals(expected,actual);
                
    }

}
