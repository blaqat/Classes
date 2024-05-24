package heroes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HeroParty implements Party{
    private game.Team team;
    private List<Hero> heroes;

    public HeroParty(game.Team team, int seed){
        this.team = team;
        heroes = new LinkedList<Hero>();

        this.addHero(Hero.create(Heroes.Role.BERSERKER, team, this));
        this.addHero(Hero.create(Heroes.Role.HEALER, team, this));
        this.addHero(Hero.create(Heroes.Role.TANK, team, this));

        Collections.shuffle(heroes, new Random(seed));
    }

    public List<Hero> getHeroes(){
        return heroes;
    }

    public game.Team getTeam(){
        return team;
    }

    public int numHeroes(){
        return this.heroes.size();
    }

    public void addHero(Hero hero){
        this.heroes.add(hero);
    }

    public Hero removeHero(){
        return this.heroes.remove(0);
    }

    public heroes.HeroParty battleParty(heroes.HeroParty otherParty){
        Hero my = this.removeHero();
        Hero their = otherParty.removeHero();
        System.out.println("***"+my.getName()+" vs "+their.getName()+"!\n");

        my.attack(their);
        if(!their.hasFallen()) {
            their.attack(my);
            otherParty.addHero(their);
        }
        if(!my.hasFallen())
            this.addHero(my);

        return otherParty;
    }

    @Override
    public String toString(){
        String retString = "";
        retString += this.team == game.Team.DRAGON?"DRAGONS":this.team == game.Team.LION?"LIONS":"Unknown";
        retString += ":\n";

        for(Hero hero: this.getHeroes()){
            retString += hero + "\n";
        }

        return retString;
    }
}
