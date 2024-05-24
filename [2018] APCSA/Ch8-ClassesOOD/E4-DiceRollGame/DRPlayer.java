public class DRPlayer
{
    private Die die1 = new Die(), die2 = new Die();
    private int points;
    private String name;
    
    public DRPlayer(){
        setPoints(1000);
        setName("Player");
    }
    
    public DRPlayer(String usn){
        setPoints(1000);
        setName(usn);
    }
    
    public void setName(String nam){
        name = nam;
    }
    
    public void setPoints(int to){
        points = to;
    }
    
    public void addPoints(int add){
        setPoints( points + add );
    }
 
    public void subtractPoints(int sub){
        setPoints( points - sub );
    }    
   
    public int getPoints(){
        return points;
    }
    
    public int rollDice(){
        die1.roll();
        die2.roll();
        return die1.getSide() + die2.getSide();
    }
    
    public void displayPoints(){
        Misc.println(name + " has " + points + " points.");
    }
    
    public boolean isHigh(){
        return die1.getSide() + die2.getSide() >= 8;
    }
    
    public boolean isLow(){
        return die1.getSide() + die2.getSide() <= 6;
    }

}
