
public class Coin
{
    private int faceUp;

    public Coin(){
        flipCoin();
    }

    public int flipCoin(){
        faceUp = Misc.random(0,1);
        return faceUp;
    }

    public int showFace(){
        return faceUp;
    }

    public String toString(){
        switch(faceUp){
            case 0: return "The coin is heads";
            case 1: return "The coin is tails";
            default: return "The coin is uncertain";
        }
    }
}
