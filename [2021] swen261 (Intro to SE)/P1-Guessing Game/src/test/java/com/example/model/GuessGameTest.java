package com.example.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.example.model.GuessGame.GuessResult;

/**
 * The unit test suite for the {@link GuessGame} component.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
@Tag("Model-tier")
public class GuessGameTest {

  private static final int NUMBER = 7;
  private static final int WRONG_GUESS_1 = 3;
  private static final int WRONG_GUESS_2 = 5;
  private static final int WRONG_GUESS_3 = 9;
  private static final int TOO_BIG = 10;
  private static final int TOO_SMALL = -1;

  /**
   * Test that the no-arg constructor works without failure.
   */
  @Test
  public void ctor_noArg() {
    new GuessGame();
  }

  /**
   * Test that the main constructor works without failure.
   */
  @Test
  public void ctor_withArg() {
    final GuessGame CuT = new GuessGame(NUMBER);
    assertEquals("{Game " + NUMBER + "}", CuT.toString());
  }

  /**
   * Test that main constructor catches arguments that are too big.
   */
  @Test
  public void ctor_tooBig() {
    assertThrows(IllegalArgumentException.class, () -> {
      new GuessGame(TOO_BIG);}, "GuessGame allowed large number.");
  }

  /**
   * Test that main constructor catches arguments that are too small.
   */
  @Test
  public void ctor_tooSmall() {
    assertThrows(IllegalArgumentException.class, () -> {
      new GuessGame(TOO_SMALL);}, "GuessGame allowed small number.");
  }

  /**
   * Test the {@link GuessGame#isValidGuess(int)} method.
   */
  @Test
  public void isValidGuess() {
    final GuessGame CuT = new GuessGame();
    // every number from zero to nine are valid
    for (int number=0; number<GuessGame.UPPER_BOUND; number++) {
      assertTrue(CuT.isValidGuess(number));
    }
    // test invalid numbers
    assertFalse(CuT.isValidGuess(TOO_BIG));
    assertFalse(CuT.isValidGuess(TOO_SMALL));
  }

  /**
   * Test the {@link GuessGame#makeGuess(int)} method with invalid argument.
   */
  @Test
  public void make_an_invalid_guess_too_small() {
    final GuessGame CuT = new GuessGame();
    assertEquals(CuT.makeGuess(TOO_SMALL), GuessResult.INVALID);
    assertFalse(CuT.isFinished(), "Game is not finished");
  }

  /**
   * Test the {@link GuessGame#makeGuess(int)} method with invalid argument.
   */
  @Test
  public void make_an_invalid_guess_too_big() {
    final GuessGame CuT = new GuessGame();
    assertEquals(CuT.makeGuess(TOO_BIG), GuessResult.INVALID);
    assertFalse(CuT.isFinished(), "Game is not finished");
  }

  /**
   * Test basic game play in which the player wins on the
   * first guess.
   */
  @Test
  public void testGamePlay_win_first_try() {
    // Arrange the test scenario & invoke test (on ctor)
    final GuessGame CuT = new GuessGame(NUMBER);

    // Analyze results: game start
    assertTrue(CuT.isGameBeginning());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(3, CuT.guessesLeft());

    // Invoke test: first guess, correct
    assertEquals(CuT.makeGuess(NUMBER), GuessResult.WON);
    // Analyze results
    assertFalse(CuT.isGameBeginning());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(2, CuT.guessesLeft());
    assertTrue(CuT.isFinished(), "Game is finished");
  }

  /**
   * Test basic game play in which the player wins on the
   * second guess.
   */
  @Test
  public void testGamePlay_win_second_try() {
    final GuessGame CuT = new GuessGame(NUMBER);

    // first guess: wrong
    assertEquals(CuT.makeGuess(WRONG_GUESS_1), GuessResult.WRONG);
    assertFalse(CuT.isGameBeginning());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(2, CuT.guessesLeft());
    assertFalse(CuT.isFinished(), "Game is not finished");

    // second guess: correct
    assertEquals(CuT.makeGuess(NUMBER),GuessResult.WON);
    assertFalse(CuT.isGameBeginning());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(1, CuT.guessesLeft());
    assertTrue(CuT.isFinished(), "Game is finished");
  }

  /**
   * Test basic game play in which the player wins on
   * the last guess.
   */
  @Test
  public void testGamePlay_win_last_try() {
    final GuessGame CuT = new GuessGame(NUMBER);

    // first guess: wrong
    CuT.makeGuess(WRONG_GUESS_1);

    // second guess: wrong
    CuT.makeGuess(WRONG_GUESS_2);
    assertFalse(CuT.isGameBeginning());
    assertTrue(CuT.hasMoreGuesses());
    assertEquals(1, CuT.guessesLeft());
    assertFalse(CuT.isFinished(), "Game is not finished");

    // third guess: correct
    assertEquals(CuT.makeGuess(NUMBER),GuessResult.WON);
    assertFalse(CuT.hasMoreGuesses());
    assertEquals(0, CuT.guessesLeft());
    assertTrue(CuT.isFinished(), "Game is finished");
  }

  /**
   * Test basic game play in which the player fails to
   * guess after three attempts.
   */
  @Test
  public void testGamePlay_lose() {
    final GuessGame CuT = new GuessGame(NUMBER);

    // first guess: wrong
    CuT.makeGuess(WRONG_GUESS_1);

    // second guess: wrong
    CuT.makeGuess(WRONG_GUESS_2);

    // third guess: wrong
    assertEquals(CuT.makeGuess(WRONG_GUESS_3),GuessResult.LOST);
    assertFalse(CuT.isGameBeginning());
    assertFalse(CuT.hasMoreGuesses());
    assertEquals(0, CuT.guessesLeft());
    assertTrue(CuT.isFinished(), "Game is finished");

    // try to make another guess
    assertThrows(IllegalStateException.class,
        () -> { CuT.makeGuess(WRONG_GUESS_3); },
        "Shouldn't allow more than three guesses.");
  }

}
