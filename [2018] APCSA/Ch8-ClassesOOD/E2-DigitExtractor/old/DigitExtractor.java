
/**
 * Write a description of class DigitExtractor here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DigitExtractor
{
    static class Num{
        private int integer;
        public Num(int num){
            if(num<=999&&num>=100)integer = num;
            else{
                Misc.println("Er; not a vlid integer");
            }
        }

        public int getDigit(int typ){
            switch(typ){
                case 1: return (int)Misc.round(integer,-2);
                case 2: return integer/10%10;
                case 3: return integer%100%10;
            }
            return integer;
        }

    }
    public static void main(String args[]){
        Misc.print("Enter an integer: ");
        Num integer = new Num(Misc.keyboard.nextInt());
        char choice;
        do{
            Misc.println("show (W)hole number\nshow(O)nes place number\nshow (T)ens place number\nshow (H)undreds place number\n(Q)uit");
            Misc.print("Enter your choice: ");
            choice = Misc.keyboard.next().charAt(0);
            if(choice=='w')Misc.println("The whole number is: "+integer.integer);
            else if(choice=='o')Misc.println("The ones palce digit is: "+integer.getDigit(3));
            else if(choice=='t')Misc.println("The tens palce digit is: "+integer.getDigit(2));
            else if(choice=='h')Misc.println("The hundreds palce digit is: "+integer.getDigit(1));
            Misc.println("\n\n");
        }
        while(choice!='q');

    }
}
