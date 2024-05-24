package heroes;

import game.Team;

import java.util.List;

/**
 * The party of heroes.
 *
 * @author RIT CS
 */
public interface Party {
    /**
     * Add a hero to the back of the collection.
     *
     * @param hero the new hero
     */
    public void addHero(Hero hero);

    /**
     * Remove the hero at the front of the collection.
     *
     * @return the hero at the front
     */
    public Hero removeHero();

    /**
     * Get the number of non-fallen heroes.
     *
     * @return number of heroes in the party
     */
    public int numHeroes();

    /**
     * The team which this party is affiliated with.
     *
     * @return the team
     */
    public Team getTeam();

    /**
     * Get all the undefeated heroes in the party.
     *
     * @return the party
     */
    public List<Hero> getHeroes();
}
