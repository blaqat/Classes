package heroes;

public abstract class Hero {
    private String name;
    private int hp;
    private int MAX_HP;
    protected  Hero(String name, int hitPoints){
        this.name = name;
        this.hp = hitPoints;
        this.MAX_HP = hitPoints;
    };

    public abstract void attack(Hero enemy);
    public abstract Heroes.Role getRole();

    public static Hero create(Heroes.Role role, game.Team team, Party party){
        return switch (role) {
            case BERSERKER -> new Berserker(team);
            case HEALER -> new Healer(team, party);
            case TANK -> new Tank(team);
        };
    };

    public String getName(){
        return this.name;
    };

    public boolean hasFallen(){
        return this.hp <= 0;
    };

    public void heal(int amt){
        this.hp += amt;
        if(this.hp > this.MAX_HP)
            this.hp = this.MAX_HP;
        System.out.println(this.getName() + " heals "+amt+" points");
    };

    public void takeDamage(int amt){
        this.hp -= amt;
        System.out.println(this.getName() + " takes "+amt+" damage");

        if(this.hasFallen())
            System.out.println(this.getName() + " has fallen!");
    };

    @Override
    public String toString(){
        return "" + this.getName() + ", " + this.getRole() + ", " + this.hp + "/" + this.MAX_HP;
    }
}
