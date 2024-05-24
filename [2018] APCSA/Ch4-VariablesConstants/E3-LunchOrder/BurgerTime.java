/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;
import java.text.NumberFormat;

public class BurgerTime
{
    public static void main (String[] args)
    {
        double price = 0;
        /*
         * Burger: $1.69
         * Fries: $1.09
         * Soda: $0.99
         * Tax: 6.5%
         */
        //(double)(int)(13.789/.01)/100);
        
        Scanner keyboard = new Scanner(System.in);
        NumberFormat money = NumberFormat.getCurrencyInstance();

        System.out.print("Enter the number of burgers: ");
        price += ( 1.69 * keyboard.nextInt() ); 
        System.out.print("Enter the number of fries: ");
        price += ( 1.09 * keyboard.nextInt() );
        System.out.print("Enter the number of sodas: ");
        price += ( 0.99 * keyboard.nextInt() );
        
        System.out.println("Total before tax: " + money.format(price));
        
        System.out.println("Tax: " + money.format(price * 0.065));
        //System.out.println("Tax: " + (double)(int)((price * 0.065)/.01)/100);
        price = price + price * 0.065;
        
        System.out.println("Final total: " +  money.format(price));        
        
        //Part B
        System.out.print("\n-\tPART B\t\t-\n");
        System.out.println("Enter amount tendered: ");
        price = Double.valueOf(keyboard.next().replace("$", "")) - price;
        System.out.println("Change: " +  money.format(price));
    }
}