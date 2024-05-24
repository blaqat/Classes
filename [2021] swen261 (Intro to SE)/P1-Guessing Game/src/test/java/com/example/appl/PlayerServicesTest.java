package com.example.appl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.example.model.GuessGame;
import com.example.model.GuessGame.GuessResult;

/**
 * The unit test suite for the {@link PlayerServices} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("Application-tier")
public class PlayerServicesTest {

  private static final int RIGHT_GUESS = 6;
  private static final int WRONG_GUESS = 3;
  private static final int INVALID_GUESS = 47;

  private GameCenter gameCenter;
  private GuessGame game;
  private PlayerServices CuT;

  @BeforeEach
  public void testSetup() {
    gameCenter = mock(GameCenter.class);
    GuessGame fixedGame = new GuessGame(RIGHT_GUESS);
    when(gameCenter.getGame()).thenReturn(fixedGame);

    // Setup CuT and create the game
    CuT = new PlayerServices(gameCenter);
    game = CuT.currentGame();
  }

  /**
   * Test that you can construct a new Player Service.
   */
  @Test
  public void test_create_service() {
    new PlayerServices(gameCenter);
  }

  /**
   * Test the creation of a new Game.
   */
  @Test
  public void test_create_game() {
    // Analyze results
    //  1) game object exists
    assertNotNull(game);
    //  2) this is the start of the game
    assertTrue(CuT.isStartingGame());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(GuessGame.NUM_OF_GUESSES, CuT.guessesLeft());
  }

  /**
   * Test the reuse of the current Game.
   */
  @Test
  public void test_current_game() {
    // Perform action
    final GuessGame game2 = CuT.currentGame();

    // Analyze results
    assertNotNull(game2);
    // we should get the same Game object from when it was first created
    assertSame(game, game2);
  }

  /**
   * Test the process of finishing the current Game and creating a new one.
   */
  @Test
  public void test_current_game_2() {
    // Setup test
    final PlayerServices CuT = new PlayerServices(gameCenter);
    final GuessGame game = CuT.currentGame();

    // Perform action
    CuT.finishedGame();
    // Make sure to return a different game when called this time
    when(gameCenter.getGame()).thenReturn(new GuessGame());
    final GuessGame game2 = CuT.currentGame();

    // Analyze results
    assertNotNull(game2);
    // we should get a new game after the first one is declared "finished"
    assertNotSame(game, game2);
  }

  /**
   * Test the process of making a guess: invalid.
   */
  @Test
  public void test_make_a_guess_1() {
    // Perform action
    final GuessResult result = CuT.makeGuess(INVALID_GUESS);

    // Analyze results
    assertNotNull(result);
    assertEquals(GuessResult.INVALID, result);
    // an invalid guess does not change the Game state
    assertTrue(CuT.isStartingGame());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(GuessGame.NUM_OF_GUESSES, CuT.guessesLeft());
  }

  /**
   * Test the process of making a guess: one wrong.
   */
  @Test
  public void test_make_a_guess_2() {
    // Perform action
    final GuessResult result = CuT.makeGuess(WRONG_GUESS);

    // Analyze results
    assertNotNull(result);
    assertEquals(GuessResult.WRONG, result);
    // a wrong guess does change the Game state
    assertFalse(CuT.isStartingGame());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(GuessGame.NUM_OF_GUESSES - 1, CuT.guessesLeft());
  }

  /**
   * Test the process of making a guess: two wrong guesses.
   */
  @Test
  public void test_make_a_guess_3() {
    // Perform action
    GuessResult result = CuT.makeGuess(3);
    // second guess
    result = CuT.makeGuess(3);

    // Analyze results
    assertNotNull(result);
    assertEquals(GuessResult.WRONG, result);
    // validate the Game state
    assertFalse(CuT.isStartingGame());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(GuessGame.NUM_OF_GUESSES - 2, CuT.guessesLeft());
  }

  /**
   * Test the process of making a guess: three wrong guesses.
   */
  @Test
  public void test_make_a_guess_4() {
    // Perform action
    GuessResult result = CuT.makeGuess(0);
    // second guess
    result = CuT.makeGuess(0);
    // last guess
    result = CuT.makeGuess(0);

    // Analyze results
    assertNotNull(result);
    assertEquals(GuessResult.LOST, result);
    // validate the Game state
    assertFalse(CuT.isStartingGame());
    assertFalse(CuT.hasMoreGuesses());
    assertEquals(0, CuT.guessesLeft());
  }

  /**
   * Test the process of winning on the first guess.
   */
  @Test
  public void test_win() {
    // Perform action
    GuessResult result = CuT.makeGuess(RIGHT_GUESS);

    // Analyze results
    assertNotNull(result);
    assertEquals(GuessResult.WON, result);
    // validate the Game state
    assertFalse(CuT.isStartingGame(), "not starting game");
    assertTrue(CuT.hasMoreGuesses(), "has more guesses");
    assertEquals(2, CuT.guessesLeft(), "has two guesses");
  }

}
