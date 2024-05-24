

public class MySavings
{
    public static void main(){
        PiggyBank bank = new PiggyBank();
        boolean cont = true;
        
        Misc.println("1. Show total in bank.\n2. Add a penny.\n3. Add a nickel.\n4. Add a dime.\n5. Add a quarter.\n6. Take money out of the bank.\nEnter 0 to quit");
        
        while(cont){
            switch(Misc.keyboard.nextInt()){
                case 0: cont = false; break;
                case 1: Misc.println(""+bank); break;
                case 2: bank.addPenny(); break;
                case 3: bank.addNickel(); break;
                case 4: bank.addDime(); break;
                case 5: bank.addQuarter(); break;
                case 6: bank.withdrawCoins(); break;
                default: Misc.println("\nInvalid input."); break;
            }
        }
    }
}
