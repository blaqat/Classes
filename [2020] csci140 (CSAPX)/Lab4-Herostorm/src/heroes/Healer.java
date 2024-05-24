package heroes;

public class Healer extends Hero {
    protected static int HIT_POINTS = 35;
    protected static final int DAMAGE_AMT = 10;
    protected static final int HEAL_AMT = 10;
    private Party party;
    protected Healer(game.Team team, Party party) {
        super(Heroes.getName(team, Heroes.Role.HEALER), HIT_POINTS);
        this.party = party;
    }

    public void attack(Hero enemy) {
        this.heal(HEAL_AMT);

        for(Hero teammate: party.getHeroes()){
            teammate.heal(HEAL_AMT);
        };

        enemy.takeDamage(DAMAGE_AMT);
    }

    public Heroes.Role getRole(){
        return Heroes.Role.HEALER;
    }
}
