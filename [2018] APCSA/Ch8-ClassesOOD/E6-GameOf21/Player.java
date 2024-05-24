public class Player
{
    public static int point;
    private Card[] hand;
    private String name;
    
    public void dealCard(){
        hand[hand.length] = new Card();
    }
    
    public boolean withdrawCard(int num){
        Card card = hand[num-1];
        int worth = getCardWorth(card);
        if(worth + point <= 21){
            point += worth;
            return true;
        }
        return false;
    }
    
    public int getCardWorth(Card card){
        if(card.getNumber() > 10)
            return 10;
        else if(card.equals("Ace") && point + 11 <= 21)
            return 11;
        else
            return card.getNumber();
    }
}
