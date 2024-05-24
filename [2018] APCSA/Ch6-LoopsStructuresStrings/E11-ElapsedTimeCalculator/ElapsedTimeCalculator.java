/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class ElapsedTimeCalculator
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
        println("------TIME-BETWEEN-HOURS------");
        print("Enter the starting hour: ");
        int initial = keyboard.nextInt();
        print("Enter am or pm: ");
        String aop = keyboard.next();
        int tod = 1;
        if(aop == "pm")tod = 2;
        print("Enter the number of elapsed hours: ");
        int hours = keyboard.nextInt();

        int time = initial + hours;
        if(time>12){
            if(time/12%2 != 0){
                if(tod==1)tod=2;
                else tod = 1;
            }
            time%=12;
        }
        switch(tod)
        {
            case 1: aop = "am"; break;
            case 2: aop = "pm"; break;
            default: aop = "am"; break;
        }
        print("The time is: " + time + ":00" + aop);
    }
}