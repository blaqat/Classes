
public class Num
{
    private int number;
    
    public Num(){
        number = 0;
    }
    
    public Num(int num){
        setNumber(num);
    }
    
    public void setNumber(int num){
        number = num;   
    }
    
    public int getOnes(){
        return number%10;
    }
    
    public int getTens(){
        return number/10%10;
    }
    
    public int getHundreds(){
        return number/100%100%10;
    }
    
    public int getNumber(){
        return number;
    }
}
