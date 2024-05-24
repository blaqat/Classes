/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class DigitSum
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
        println("------ADD-DIGITS-FROM-NUMBERS------");
        print("Enter a positive integer: ");
        int number = keyboard.nextInt();
        int sum = 0;
        for(int i = (int)Math.log10(number) + 1;i>0;i-=1)
        {
            sum += (int)(number/Math.pow(10,i-1))%10;
        }
        
        print("The sum of the digits are " + sum);
    }
}