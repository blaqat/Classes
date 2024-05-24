public class Die
{
    private int side;

    public Die(){
        roll();
    }

    public void setSide(int to){
        side = to;
    }

    public int getSide(){
        return side;
    }
    
    public void roll(){
        setSide(1 + (int)(Math.random() * (6)));
    }

}
