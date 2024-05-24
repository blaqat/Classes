/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class DigitsDisplay
{
    static void print(String str){
        System.out.print(str);
    }

    static void println(String str){
        System.out.println(str);
    }   
    
    static double round(double number, double decimal){
        return (double)Math.round(number*Math.pow(10,decimal));
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("---FIND-DIGITS-FROM-NUMBERS-again-:/---");
/*
 * 402
 * n/100%100
 * 
 * 7002
 * n/1000  (1000 = 10^3)   
 * 1000 > i = 4  10^i = 10000  
 * i-1
 * 7002/(10^i-1) or 7002*(10^-(i-1))
 */
        print("Enter a positive integer: ");
        int number = keyboard.nextInt();
        int counter = (int)Math.log10(number) + 1;
        println("The digits of this number are: ");
        for(int i = counter;i>0;i-=1)
        {
            println(""+(int)(number/Math.pow(10,i-1))%10);
        }

    }
}