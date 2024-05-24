
public class PiggyBank
{
    private int nickels, pennies, dimes, quarters;

    public PiggyBank(){
        setPennies(0);
        setNickels(0);
        setDimes(0);
        setQuarters(0);        
    }

    public PiggyBank(int pen,int nick,int dime, int quart){
        setPennies(pen);
        setNickels(nick);
        setDimes(dime);
        setQuarters(quart);         
    }

    public void setPennies(int set){
        pennies = set;
    }

    public int getPennies(){
        return pennies;
    }

    public void addPenny(){
        setPennies(pennies+1);
    }

    public void setNickels(int set){
        nickels = set;
    }

    public int getNickels(){
        return nickels;
    }

    public void addNickel(){
        setNickels(nickels+1);
    }

    public void setDimes(int set){
        dimes = set;
    }

    public int getDimes(){
        return dimes;
    }

    public void addDime(){
        setDimes(dimes+1);
    }

    public void setQuarters(int set){
        quarters = set;
    }

    public int getQuarters(){
        return quarters;
    }

    public void addQuarter(){
        setQuarters(quarters+1);
    }

    public void withdrawCoins(){
        setPennies(0);
        setNickels(0);
        setDimes(0);
        setQuarters(0);
    }    

    public double getTotal(){
        return (pennies * .01) + (nickels * .05) + (dimes * .1) + (quarters * .25);
    }
    
    public String toString(){
        return Misc.moneyFormat(getTotal());
    }
}
