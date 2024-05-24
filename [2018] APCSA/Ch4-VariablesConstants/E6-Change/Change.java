/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;


public class Change
{
    public static void main (String[] args)
    {
        int money = 0;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter the change in cents: ");
        money = keyboard.nextInt();
        System.out.print("The mininum number of coins is: ");
        System.out.print("\n    Quarters: " + money/25);
        money = money-(25*(money/25));
        System.out.print("\n    Dimes: " + money/10);
        money = money-10*(money/10);
        System.out.print("\n    Nickels: " + money/5);
        money = money-5*(money/5);
        System.out.print("\n    Pennies: " + money);
    }
}