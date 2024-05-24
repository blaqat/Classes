package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier")
public class CheckersGameTest {
    @Test
    public void testSwitchTurnToWhite(){
        Player player = new Player("TestPlayer",null);
        Player otherTestPlayer = new Player("OtherTestPlayer",null);
        CheckersGame game = new CheckersGame(0, player, otherTestPlayer);
        player.joinGame(game, Color.RED);
        otherTestPlayer.joinGame(game, Color.WHITE);
        game.switchTurn();
        Color expected = Color.WHITE;
        Color actual = game.getActiveColor();
        assertEquals(expected, actual);
    }
}
