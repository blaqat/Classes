/* Aiden
 * Programs: Ch7
 * Programming - 4y
 * I hear 7 is a lucky number o-o
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
 */
import java.util.Scanner;

public class AddCoins
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

    static String getDollarAmount(int quarters, int dimes, int nickles, int pennies){
        quarters *= 25;
        dimes *= 10;
        nickles *= 5;
        double total = quarters+dimes+nickles+pennies;

        return "$"+total/100;
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("Enter your total coins:\n");
        
        print("Quarters: ");
        int qua = keyboard.nextInt();
        print("Dimes: ");
        int dim = keyboard.nextInt();
        print("Nickles: ");
        int nick = keyboard.nextInt();
        print("Pennies: ");
        int pen = keyboard.nextInt();
        
        print("\nTotal: "+getDollarAmount(qua,dim,nick,pen));
    }
}