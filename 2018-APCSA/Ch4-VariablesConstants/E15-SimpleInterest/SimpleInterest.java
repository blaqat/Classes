/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;
import java.text.NumberFormat;

public class SimpleInterest
{
    public static void main (String[] args)
    {
        double princ;
        int years;
        double amount;
        double rate;
        
        Scanner keyboard = new Scanner(System.in);
        NumberFormat money = NumberFormat.getCurrencyInstance();
        
        System.out.print("Enter the principal: ");
        princ = Double.valueOf(keyboard.next().replace("$", ""));
        System.out.print("Enter the number of years: ");
        years = keyboard.nextInt();
        System.out.print("Enter the interest rate: ");
        rate = keyboard.nextDouble();
        
        amount = princ * (1 + years * rate);
        
        System.out.print("The value after the term is: " + money.format(amount));
  
        //Part B
        System.out.print("\n-\tPART B\t\t-");
        System.out.print("\nEnter the desired amount: ");
        amount = Double.valueOf(keyboard.next().replace("$", ""));
        System.out.print("Enter the number of years: ");
        years = keyboard.nextInt();
        System.out.print("Enter the interest rate: ");
        rate = keyboard.nextDouble();
        
        princ = amount * (1 + years * rate);
        
        System.out.print("The principal that will need to be invested is: " + princ);
         
    }
}