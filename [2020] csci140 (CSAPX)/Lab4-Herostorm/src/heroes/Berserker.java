package heroes;

public class Berserker extends Hero {
    protected static final int HIT_POINTS = 30;
    protected static final int DAMAGE_AMT = 20;
    protected Berserker(game.Team team) {
        super(Heroes.getName(team, Heroes.Role.BERSERKER), HIT_POINTS);
    }

    public void attack(Hero enemy) {
        enemy.takeDamage(DAMAGE_AMT);
    }

    public Heroes.Role getRole(){
        return Heroes.Role.BERSERKER;
    }
}
