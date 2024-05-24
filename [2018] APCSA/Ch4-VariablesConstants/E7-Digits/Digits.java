/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;

public class Digits
{
    public static void main (String[] args)
    {
        int digit = 0;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter a three-digit number: ");
        digit = keyboard.nextInt();
        if(digit/100>=1  & digit/100<10){
            System.out.println("The hundreds-place digit is: " + digit/100);

            System.out.println("The tens-place digit is: " + digit%100/10);

            System.out.println("The ones-place digit is: " + digit%100%10);
        }
        else{
            System.out.print("This number isn't a 3 digit number...");
        }
    }
}