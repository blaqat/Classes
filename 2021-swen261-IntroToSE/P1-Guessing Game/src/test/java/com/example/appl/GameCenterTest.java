package com.example.appl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.example.model.GuessGame;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("Application-tier")
public class GameCenterTest {

  /**
   * Test the ability to make a new PlayerService.
   */
  public void test_make_player_service() {
    final GameCenter CuT = new GameCenter();
    // Invoke test
    final PlayerServices playerSvc = CuT.newPlayerServices();
    // Analyze results
    assertNotNull(playerSvc);
  }

  /**
   * Test the ability to make a new PlayerService.
   */
  public void test_make_game() {
    final GameCenter CuT = new GameCenter();
    // Invoke test
    final GuessGame game = CuT.getGame();

    // Analyze the results
    // 1) the returned game is real
    assertNotNull(game);
    // 2) the game is at the beginning
    assertTrue(game.isGameBeginning());
    // 4) the game stats message
    assertEquals(GameCenter.NO_GAMES_MESSAGE, CuT.getGameStatsMessage());
  }

  /**
   * Test the game stats: no games played.
   */
  @Test
  public void game_statistics_message_0() {
    final GameCenter CuT = new GameCenter();

    // Analyze the results
    assertEquals(GameCenter.NO_GAMES_MESSAGE, CuT.getGameStatsMessage());
  }

  /**
   * Test the game stats: one game played.
   */
  @Test
  public void game_statistics_message_1() {
    final GameCenter CuT = new GameCenter();

    // simulate a single game ending
    CuT.gameFinished();

    // Analyze the results
    assertEquals(GameCenter.ONE_GAME_MESSAGE, CuT.getGameStatsMessage());
  }

  /**
   * Test the game stats: two games played.
   */
  @Test
  public void game_statistics_message_2() {
    final GameCenter CuT = new GameCenter();

    // simulate two games ending
    CuT.gameFinished();
    CuT.gameFinished();

    // Analyze the results
    assertEquals(String.format(GameCenter.GAMES_PLAYED_FORMAT, 2),
        CuT.getGameStatsMessage());
  }

  /**
   * Test the game stats: three games played.
   */
  @Test
  public void game_statistics_message_3() {
    final GameCenter CuT = new GameCenter();

    // simulate three games ending
    CuT.gameFinished();
    CuT.gameFinished();
    CuT.gameFinished();

    // Analyze the results
    assertEquals(String.format(GameCenter.GAMES_PLAYED_FORMAT, 3),
        CuT.getGameStatsMessage());
  }

}
