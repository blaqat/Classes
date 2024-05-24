
/**
 * Write a description of class DigitExtractor here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class DigitExtractor
{
    public static void main(){
        Misc.print("Enter an integer: ");
        Num number = new Num(Misc.keyboard.nextInt());
        boolean cont = true;

        while(cont){
            Misc.println("\nshow (W)hole number.\nshow (O)nes place digit.\nshow (T)ens place digit.\nshow (H)undreds place digit.\n(Q)uit");
            Misc.print("Enter your choice: ");
            String choice = Misc.keyboard.next().toLowerCase();

            if(choice.equals("h")){
                Misc.println("The hundreds place digit is: " + number.getHundreds());
            }
            else if(choice.equals("t")){
                Misc.println("The tens place digit is: " + number.getTens());
            }
            else if(choice.equals("o")){
                Misc.println("The ones place digit is: " + number.getOnes());
            }
            else if(choice.equals("w")){
                Misc.println("The whole number is: " + number.getNumber());
            }
            else if(choice.equals("q")){
                cont = false;
            }
            else {
                Misc.print("Invalid Input.");
            }
        }
    }
}
