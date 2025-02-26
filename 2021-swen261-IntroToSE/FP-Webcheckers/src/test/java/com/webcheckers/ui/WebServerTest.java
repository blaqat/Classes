package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.TournamentCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.Mockito.mock;

@Tag("UI-Tier")
public class WebServerTest {
    private GameCenter center;
    private PlayerLobby lobby;
    private TournamentCenter tournCenter;
    private TemplateEngine engine;
    private Gson gson;

    @BeforeEach
    public void createMock(){
        center = new GameCenter();
        lobby = new PlayerLobby();
        gson = new Gson();
        tournCenter = new TournamentCenter(center);
        engine = mock(TemplateEngine.class);
    }

    @Test
    public void testInitialize(){
        WebServer server = new WebServer(center, lobby, tournCenter, engine, gson);
        server.initialize();
        //
    }

}
