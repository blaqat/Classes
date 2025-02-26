package com.webcheckers.model;

import com.webcheckers.application.GameCenter;

import java.util.*;

/**
 * Class will represent a tournament that handles all functionality of the tournament
 *  @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 *  @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class Tournament {
    private Player leader;
    private final List<Player> participants;
    private int maxPlayers;
    private boolean inProgress;
    private List<Player[] > bracket;
    private GameCenter gameCenter;
    private boolean isOver;
    private int remainingPlayers;
    private Player winner;
    private Set<CheckersGame> gamesInProgress;

    public Tournament(Player leader, GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        this.bracket = null;
        this.inProgress = false;
        this.leader = leader;
        this.maxPlayers = 4;
        this.participants = new ArrayList<>();
        this.winner = null;
        this.remainingPlayers = 0;
        this.gamesInProgress = new HashSet<>();
        participants.add(leader);
    }

    /**
     * starts the game setting inProgress and starting the round
     */
    public void start() {
        if (participants.size() > maxPlayers) {
            return;
        } else if (participants.size()<maxPlayers) {
            return;
        }
        inProgress = true;
        this.remainingPlayers = participants.size();
        startRound();
        return;
    }

    /**
     * initializes all the bracket matchups
     * and all players begin playing their first checkers game
     * the participant list is cleared and only the winner of the game is readded to the
     * list
     */
    public void startRound() {
        for (Player[] matchup: bracket) {
            gamesInProgress.add(gameCenter.initializeGame(matchup[0], matchup[1]));
        }
        bracket = null;
        this.participants.clear();
    }

    /**
     * helper to shuffle and create matchups given the current
     * participant list
     */
    public void seed() {
        List<Player> playersToSeed = new ArrayList<>();
        for (Player player: participants) {
            if (!player.isPlayingGame()) {
                playersToSeed.add(player);
            }
        }
        bracket = new ArrayList<>();
        Collections.shuffle(playersToSeed);

        while (playersToSeed.size() > 1) {
            Player player = playersToSeed.remove(0);
            for (Player other: playersToSeed) {
                if (player.getTournamentRound() == other.getTournamentRound()) {
                    Player[] matchup = new Player[2];
                    matchup[0] = player;
                    matchup[1] = other;
                    playersToSeed.remove(other);
                    bracket.add(matchup);
                    break;
                }
            }
        }
        if (bracket.size() == 0) {
            bracket = null;
        }
    }

    public List<Player[] > getBracket() {
        return bracket;
    }

    /**
     * removes player from tournament and reseeds bracket based on missing player
     * @param player player leaving tournament
     */
    public void leave(Player player) {
        participants.remove(player);
        seed();
    }

    /**
     * adds player to tournament and reseeds bracket based on new player
     * @param player player joining
     */
    public void join(Player player) {
        participants.add(player);
        player.joinTournament(this);
        seed();
    }

    /**
     * winning Player class calls this to report that they won and
     * should be rentered for their next game
     * also seeds and checks if their next game is ready
     * if so, they are immediately initiated into the next round of checkers
     * if there is nobody left in the tournament, they are redirected to winning screen
     * @param winner
     */
    public void reportWinner(Player winner) {
        gamesInProgress.remove(winner.getGameSession());
        if (!participants.contains(winner)) {
            participants.add(winner);
        }
        seed();
        remainingPlayers--;
        if (bracket != null) {
            for (Player[] matchup: bracket) {
                for (Player player: matchup) {
                    if (player == winner) {
                        gamesInProgress.add(gameCenter.initializeGame(matchup[0], matchup[1]));
                        participants.remove(matchup[1]);
                        participants.remove(matchup[0]);
                        bracket.remove(matchup);
                        return;
                    }
                }
            }
        }
        if (remainingPlayers<= 1) {
            endTournament(winner);
            winner.achievements.add("Tournament Winner");
        }
    }

    /**
     * changes status of the game and sets winner for
     * UI display purposes
     * @param winner
     */
    public void endTournament(Player winner) {
        this.winner = winner;
        isOver = true;
    }

    public Player getWinner() {
        return winner;
    }

    /**
     * controller for UI to set max players before tournament begins
     * @param value
     */
    public void setMaxPlayers(int value) {
        this.maxPlayers = value;
        if (maxPlayers<4) {
            maxPlayers = 4;
        }
    }

    public boolean isOver() {
        return isOver;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public Set<CheckersGame> getGamesInProgress() {
        return gamesInProgress;
    }

    /**
     * if leader exits tournament completely, new leader is randomly chosen
     */
    public void nextLeader() {
        if (participants.size() > 0) {
            this.leader = participants.get(0);
        }
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public Player getLeader() {
        return this.leader;
    }
}