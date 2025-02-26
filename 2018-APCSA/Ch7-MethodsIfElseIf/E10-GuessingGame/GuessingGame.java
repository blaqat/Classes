/* Aiden
 * Programs: Ch7
 * Programming - 4y
 * I hear 7 is a lucky number o-o
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
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
    
    static void giveHint(int guess,int ran){
        if(guess>ran){
            println("Hint: try a lower number");
        }
        else if(guess<ran){
            println("Hint: try a higher number");
        }
    }
    
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        final int ran = random(1,20);
        int guess;
        do{
            print("Enter a number between 1 and 20: ");
            guess = keyboard.nextInt();
            if(guess>=1&&guess<=20){
                giveHint(guess,ran);
            }
            else println("Smh that's not a number inbetween 1 and 20");
        } while(guess!=ran);
        println("You \"won\"!");
    }
}