package com.webcheckers.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Color;
import com.webcheckers.model.Player;
import spark.Session;

import static org.mockito.Mockito.mock;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("Application-tier")
public class GameCenterTest {
  Player p1;
  Player p2;

  /**
   * Test the ability to make a new PlayerService.
   */
  public void test_make_player_service() {
  }

  /**
   * Test the ability to make a new PlayerService.
   */
  @Test
  public void test_make_game() {
    final GameCenter CuT = new GameCenter();
    Session session1 = mock(Session.class);
    Session session2 = mock(Session.class);
    p1 = new Player("Player1", session1);
    p2 = new Player("Player2", session2);
    // Invoke test
    final CheckersGame game = CuT.initializeGame(p1, p2);

    // Analyze the results
    // 1) the returned game is real
    assertNotNull(game);
    // 2) the game is at the beginning
    assertTrue(p1.isPlayingGame());
    assertTrue(p2.isPlayingGame());
    assertSame(p1.getGameSession(), game);
    assertSame(p1.getGameSession(), p2.getGameSession());
    assertEquals(Color.RED, p1.getColor());
    assertEquals(Color.WHITE, p2.getColor());
  }

  /**
   * Test the game stats: no games played.
   */
//  @Test
//  public void game_stored_on_init() {
//    final GameCenter CuT = new GameCenter();
//    Session session1 = mock(Session.class);
//    Session session2 = mock(Session.class);
//    p1 = new Player("Player1", session1);
//    p2 = new Player("Player2", session2);
//    // Invoke test
//    final CheckersGame game = CuT.initializeGame(p1, p2);
//
//    // Analyze the results
//    assertNotNull(CuT.getGame(game.getId()));
//    assertSame(CuT.getGame(game.getId()), game);
//  }

  /**
   * Test the game stats: one game played.
   */
  @Test
  public void game_stored() {
    final GameCenter CuT = new GameCenter();
    Session session1 = mock(Session.class);
    Session session2 = mock(Session.class);
    p1 = new Player("Player1", session1);
    p2 = new Player("Player2", session2);
    // Invoke test
    final CheckersGame game = CuT.initializeGame(p1, p2);
    CuT.storeGame(game);
    // Analyze the results
    assertNotNull(CuT.getGame(1));
    assertSame(CuT.getGame(1), game);
  }
}
