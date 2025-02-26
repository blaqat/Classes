
public class MySavings
{
    static class PiggyBank

    {
        private double bank = 0;

        public double deposit(int type){
            switch(type){
                case 1: bank+=.01; break;
                case 2: bank+=.05; break;
                case 3: bank+=.1; break;
                case 4: bank+=.25; break;
                default: Misc.printTitle("Err; Not a vlid deposit type"); break;
            }
            return bank;
        }

        public double withdraw(double money){
            if(money>bank)Misc.println("Not enough money for withdrawal");
            else bank-=money;            
            return bank;
        }

    }

    public static void main(String args[]){
        PiggyBank piggy = new PiggyBank();
        int choice = 0;
        do{
            Misc.println("1. Show total in bank\n2. Add a penny\n3. Add a nickel\n4. Add a dime\n5. Add a quarter\n6. Take money out of bank\nEnter 0 to quit");
            Misc.print("Enter your choice: ");
            choice = Misc.keyboard.nextInt();
            if(choice==1)Misc.printTitle("Total: "+piggy.bank);
            else if(choice>1&&choice<6)Misc.printTitle("Total: $"+piggy.deposit(choice-1));
            else if(choice==6){
                Misc.println("How much would you like to withdraw? ");
                piggy.withdraw(Misc.keyboard.nextDouble());
                Misc.printTitle("Total: $"+piggy.bank);
            }
            else if(choice==0)Misc.clear();
        }
        while(choice!=0);
    }
}
