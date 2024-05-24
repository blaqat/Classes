/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class Hailstone
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
    static double round(double number, double decimal){
        return (double)Math.round(number*Math.pow(10,decimal));
    }
    
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("------METEROTICIANS------");
        print("Enter the starting number: ");
        int num = keyboard.nextInt();
        
        while(num>=1){
            print(num+", ");
            if(num==1)break;
            if(num%2==0)num/=2;
            else num=num*3+1;
            
        }

    }
}