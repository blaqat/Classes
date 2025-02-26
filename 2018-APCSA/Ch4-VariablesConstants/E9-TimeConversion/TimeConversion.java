/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;


public class TimeConversion
{
    public static void main (String[] args)
    {
        int time = 0;
        //String zero = " ";
        Scanner keyboard = new Scanner(System.in);
        
        System.out.print("Enter the time in minuets: ");
        time = keyboard.nextInt();
        
        /*if(time%60<10){
            zero = "0";
        }
        */
        System.out.print("The time is: " + time/60 + ":" + /*zero +*/ time%60/10 + time%60%10);
    }
}