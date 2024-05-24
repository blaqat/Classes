package com.webcheckers.model;

import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class PlayerTest {

    private static CheckersGame game;

    @BeforeEach
    public void gameMock(){
        Player player1 = new Player("Player1",null);
        Player player2 = new Player("Player2",null);
        game = new CheckersGame(0, player1, player2);
        player1.joinGame(game, Color.RED);
        player2.joinGame(game, Color.WHITE);
    }

    @Test
    public void testCreatePlayer(){
        Player player = new Player("Test",null);
        String expected = "Test";
        String actual = player.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidMove(){
        Move move = new Move(new Position(5, 0), new Position(2, 5));
        Player player = (Player)game.getRedPlayer();
        Message.Type expected = Message.Type.ERROR;
        Message.Type actual = player.move(move).getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testValidMove(){
        Move move = new Move(new Position(5, 0), new Position(4, 1));
        Player player = (Player)game.getRedPlayer();
        String expected = "Valid move.";
        String actual = player.move(move).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void testBackwardRedSingleMove(){
        Move preMove = new Move(new Position(5, 0), new Position(4, 1));
        Move move = new Move(new Position(4, 1), new Position(5, 0));
        Player player = (Player)game.getRedPlayer();
        player.move(preMove);
        player.validateTurn();
        Message.Type expected = Message.Type.ERROR;
        Message.Type actual = player.move(move).getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testBackwardWhiteSingleMove(){
        Move preMove = new Move(new Position(2, 7), new Position(3, 6));
        Move move = new Move(new Position(3, 6), new Position(2, 7));
        Player player = (Player)game.getWhitePlayer();
        player.move(preMove);
        player.validateTurn();
        String expected = "Invalid move. Singles cannot move backwards.";
        String actual = player.move(move).getText();
        assertEquals(expected, actual);
    }

    @Test
    public void testValidateTurn(){
        Move move = new Move(new Position(2, 7), new Position(3, 6));
        Player player1 = (Player)game.getRedPlayer();
        player1.move(move);
        player1.validateTurn();
        assertEquals(Color.WHITE, game.getActiveColor());
    }

    @Test
    public void testValidJump(){
        Move move = new Move(new Position(5, 4), new Position(4, 3));
        Move move2 = new Move(new Position(2, 1), new Position(3, 2));
        Move jump = new Move(new Position(4, 3), new Position(2, 1));
        Player player1 = (Player)game.getRedPlayer();
        Player player2 = (Player)game.getWhitePlayer();
        player1.move(move);
        player1.validateTurn();
        player2.move(move2);
        player2.validateTurn();
        Message.Type expected = Message.Type.INFO;
        Message.Type actual = player1.move(jump).getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testValidMultiJump(){
        //set of premoves leading to multijump availability
        Move move = new Move(new Position(5, 4), new Position(4, 3));
        Move stall = new Move(new Position(2, 7), new Position(3, 6));
        Move move2 = new Move(new Position(6, 5), new Position(5, 4));
        Move stall2 = new Move(new Position(3, 6), new Position(4, 7));
        Move move3 = new Move(new Position(4, 3), new Position(3, 2));
        Move jump1 = new Move(new Position(2, 1), new Position(4, 3));
        Move jump2 = new Move(new Position(4, 3), new Position(6, 5));
        Player player1 = (Player)game.getRedPlayer();
        Player player2 = (Player)game.getWhitePlayer();
        player1.move(move);
        player1.validateTurn();
        player2.move(stall);
        player2.validateTurn();
        player1.move(move2);
        player1.validateTurn();
        player2.move(stall2);
        player2.validateTurn();
        player1.move(move3);
        player1.validateTurn();
        player2.move(jump1);
        Message.Type expected = Message.Type.INFO;
        Message.Type actual = player2.move(jump2).getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testBackupMove(){
        Move move = new Move(new Position(5, 0), new Position(4, 1));
        Player player1 = (Player)game.getRedPlayer();
        player1.move(move);
        Piece fromPiece = game.getBoard().getSpace(move.getInitialPosition().getRow(), move.getInitialPosition().getCell()).getPiece();
        assertNull(fromPiece);
        player1.backupMove();
        fromPiece = game.getBoard().getSpace(move.getInitialPosition().getRow(), move.getInitialPosition().getCell()).getPiece();
        assertNotNull(fromPiece);
    }

    @Test
    public void testHadJumpError(){
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
        Message.Type actual = player1.validateTurn().getType();
        Message.Type expected = Message.Type.ERROR;
        assertEquals(expected, actual);
    }

    @Test
    public void testHasMultiJumpError(){
        Move red1 = new Move(new Position(5, 2), new Position(4, 3));
        Move white1 = new Move(new Position(2, 3), new Position(3, 4));
        Move red2 = new Move(new Position(6, 1), new Position(5, 2));
        Move white2 = new Move(new Position(1, 4), new Position(2, 3));
        Move red3 = new Move(new Position(4, 3), new Position(3, 2));
        Move whiteJump = new Move(new Position(2, 1), new Position(4, 3));
        Player player1 = (Player)game.getRedPlayer();
        Player player2 = (Player)game.getWhitePlayer();
        player1.move(red1);
        player1.validateTurn();
        player2.move(white1);
        player2.validateTurn();
        player1.move(red2);
        player1.validateTurn();
        player2.move(white2);
        player2.validateTurn();
        player1.move(red3);
        player1.validateTurn();
        player2.move(whiteJump);
        Message.Type actual = player2.validateTurn().getType();
        Message.Type expected = Message.Type.ERROR;
        assertEquals(expected, actual);
    }

}
