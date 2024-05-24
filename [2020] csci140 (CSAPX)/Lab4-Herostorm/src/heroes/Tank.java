package heroes;

public class Tank extends Hero {
    protected static final int HIT_POINTS = 40;
    protected static final int DAMAGE_AMT = 15;
    protected static final double SHIELD_MULT = .9;
    protected Tank(game.Team team) {
        super(Heroes.getName(team, Heroes.Role.TANK), HIT_POINTS);
    }

    public void attack(Hero enemy) {
        enemy.takeDamage(DAMAGE_AMT);
    }

    public Heroes.Role getRole(){
        return Heroes.Role.TANK;
    }

    public void takeDamage(int amt){
        super.takeDamage((int)(amt * SHIELD_MULT));
    }
}
