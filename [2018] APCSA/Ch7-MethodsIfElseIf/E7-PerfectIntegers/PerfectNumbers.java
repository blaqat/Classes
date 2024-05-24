/* Aiden
 * Programs: Ch7
 * Programming - 4y
 * I hear 7 is a lucky number o-o
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
 */
import java.util.Scanner;

public class PerfectNumbers
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

    static boolean isPerfect(int number){
        for(int x = 0;x<49;x++){
            if(Math.pow(2,x-1)*(Math.pow(2,x)-1)==number)return true;
        }
        return false;
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        for(int x = 0;x<100;x++){
            if(isPerfect(x))println(""+x);
        }
    }
}