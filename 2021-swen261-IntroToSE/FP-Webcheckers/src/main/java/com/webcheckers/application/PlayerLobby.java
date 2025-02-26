package com.webcheckers.application;

import java.util.ArrayList;
import java.util.List;

import com.webcheckers.model.AI;
import com.webcheckers.model.GamePlayer;
import com.webcheckers.model.Player;
import spark.Session;

/**
 * The processing and management of players. 
 *@author<a href='mailto:zsm5532@rit.edu'>Zach Montgomery</a>
 * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * =======
 * Holds sign in/out functionality
 * keeps track of signed in players

 */

public class PlayerLobby {

    public static final String NOT_CAPITAL = "Not a valid username. Username should start with a capital letter.";
    public static final String TOO_LONG = "The name you entered is too long, please shorten.";
    public static final String USER_EXISTS = "Name is already in use. Please try another name.";
    public static final String ALPHA_NUM = "Name should only contain alphanumerical character (numbers or letters)";
    private final List<GamePlayer> onlinePlayers;

    public PlayerLobby() {
        this.onlinePlayers = new ArrayList<>();
        onlinePlayers.add(new AI(AI.Difficulty.EASY));
        onlinePlayers.add(new AI(AI.Difficulty.MED));
        onlinePlayers.add(new AI(AI.Difficulty.HARD));
    }

    /**
     *
     * @param userName The requested username from the Web User
     * @param htmlSession Session of the Web User
     * @return Error String
     * Returns null on success
     */
    public String signIn(String userName, Session htmlSession) {

        // Trims and checks for invalid/taken username
        // returns respective error message if caught
        userName = userName.trim();
        char first = userName.charAt(0);
        if (!(first<= 'Z' && first >= 'A')) {
            return NOT_CAPITAL;
        } else if (userName.length() > 13) {
            return TOO_LONG;
        } else if (!(userName.matches("([a-zA-Z0-9]*)$"))) {
            return ALPHA_NUM;
        } else if (getPlayer(userName) != null) {
            return USER_EXISTS;
        }

        // logs player into lobby on success
        Player player = new Player(userName, htmlSession);
        htmlSession.attribute("currentUser", player);
        onlinePlayers.add(player);
        return null;
    }

    /**
     *  Signs out a Player
     * @param session Session of player being signed out
     */
    public void signOut(Session session) {
        Player player = session.attribute("currentUser");
        session.removeAttribute("currentUser");
        onlinePlayers.remove(player);
    }

    /**
     * Gets a Player object by username search
     * @param username
     * @return
     */
    public GamePlayer getPlayer(String username) {
        for (GamePlayer plr: onlinePlayers) {
            if (plr.getName().equals(username))
                return plr;
        }
        return null;
    }

    /**
     * Gets a new copy of the current List of Signed in Players
     * New List with old Player references
     * @return
     */
    public List<GamePlayer> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }
}