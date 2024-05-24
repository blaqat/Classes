/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;

public class ObjectHeight
{
    public static void main (String[] args)
    {
        double time = 0;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter a time less than 4.5 seconds: ");
        time = keyboard.nextDouble();
        
        if(time<4.5){
            System.out.print("The height of the object is: " + (100-4.9*time*2) + " meters");
        }
        else{
            System.out.print("I know I said to enter a time LESS THAN 4.5");
        }
    }
}