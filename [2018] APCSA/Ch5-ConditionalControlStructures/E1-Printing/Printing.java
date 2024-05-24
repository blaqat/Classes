/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;
import java.text.NumberFormat;
public class Printing
{
    public static void main (String[] args)
    {
        double copies; //lua:  local length = 22
        double price;
        Scanner keyboard = new Scanner(System.in);
        NumberFormat money = NumberFormat.getCurrencyInstance();

        System.out.print("Enter the number of copies to be printed: ");
        copies = keyboard.nextDouble();

        if(copies <= 99 || copies >= 0)
        {
            price = .30;
        }
        else if(copies <= 499){
            price = .28;
        }
        else if(copies <= 749){
            price = .27;
        }
        else if(copies <= 1000){
            price = .26;
        }
        else
        {
            price = .25;
        }

        if(copies <= 0)
        {
            System.out.println("Invalid Input");
        }
        else{
            copies *= price;

            System.out.println("Price per copy is: " + money.format(price));
            System.out.println("Total cost is: " + money.format(copies));
        }
    }
}