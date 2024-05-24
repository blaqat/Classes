/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class GuessingGame
{
    static void print(String str){
        System.out.print(str);
    }
    static void println(String str){
        System.out.println(str);
    }    
    static int random(int min, int max){
        max+=1;
        return min + (int)(Math.random() * (max-min));
    }
    static double round(double number, double decimal){
        return (double)Math.round(number*Math.pow(10,decimal));
    }
    
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("------GUESSING-GAME------");
        int user = 0,computer = random(1,20);
        while(user!=computer)
        {
            print("Enter a number between 1 and 20: ");
            user = keyboard.nextInt();
            if(user!=computer)print("Try again.");
            if(user<computer)println(" (too low)");
            else if(user>computer)println(" (too high)");
        }
        print("You won!");
    }
}