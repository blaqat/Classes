package com.webcheckers.application;

import com.webcheckers.model.Player;
import com.webcheckers.model.Tournament;

import java.util.ArrayList;
import java.util.List;

/**
 *  Controller to help deal with tournament creation and proper tournament leaving
 *  stores tournaments as well
 *  @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 *  @author<a href='mailto:zsm5532@rit.edu'>Zachary Montgomery</a>
 */
public class TournamentCenter {
    private final List<Tournament> activeTournaments;
    private final GameCenter gameCenter;

    public TournamentCenter(GameCenter gameCenter) {
        this.activeTournaments = new ArrayList<>();
        this.gameCenter = gameCenter;
    }

    /**
     * creates a new tournament, stores it, and adds the given leader player to the tournament
     * @param leader tournament creator
     * @return the created tournament
     */
    public Tournament createTournament(Player leader) {
        Tournament tournament = new Tournament(leader, gameCenter);
        activeTournaments.add(tournament);
        leader.joinTournament(tournament);
        return tournament;
    }

    /**
     * properly removes a player from the tournament by settings status and attributes accordingly
     * @param player player leaving
     */
    public void leaveTournament(Player player) {
        Tournament tournament = player.getCurrentTournament();
        if (tournament != null) {
            tournament.leave(player);
            player.leaveTournament();
            if (tournament.getLeader() == player) {
                tournament.nextLeader();
            }
        }
        activeTournaments.removeIf(t -> t.getParticipants().size() == 0 && t.getLeader() == null);
    }

    public List<Tournament> getActiveTournaments() {
        return new ArrayList<>(activeTournaments);
    }
}