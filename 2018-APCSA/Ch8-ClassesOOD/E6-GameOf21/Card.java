public class Card{
    private String name;
    private int number;
    static String[] names = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "King", "Queen"};
    
    public Card(){
        setNumber(Misc.random(1,13)%10);
    }
    
    public Card(int num){
        setNumber(num);
    }
    
    public void setNumber(int num){
       number = num; 
       name = names[num - 1];
    }
    
    public boolean equals(int worth){
        return number == worth;
    }

    public boolean equals(String nam){
        return nam.equals(name);
    }    
    
    public int getNumber(){
        return number;
    }
    
    public void setWorth(){
        
    }
    

}
