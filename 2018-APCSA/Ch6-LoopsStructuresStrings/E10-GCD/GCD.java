/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class GCD
{
    static void print(String str){
        System.out.print(str);
    }
    static void println(String str){
        System.out.println(str);
    }    
    static int random(int min, int max){
        return min + (int)(Math.random() * (max-min));
    }
    static void swapCheck(double num1, double num2){
        if(num1<num2){
            double temp = num1;
            num1 = num2;
            num2 = temp;
        }
    }
    static double round(double number, double decimal){
        return (double)Math.round(number*Math.pow(10,decimal));
    }
    
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("------GREATEST-COMMON-DENOMINATORS------");
        print("Enter a number: ");
        int num1 = keyboard.nextInt();
        print("Enter a second number: ");
        int num2 = keyboard.nextInt();
        
        while(num2>0){
            int temp = num1 % num2;
            num1 = num2;
            num2 = temp;
        }
        print("The GCD is " + num1);
    }
}