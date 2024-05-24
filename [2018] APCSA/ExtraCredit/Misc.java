
import java.util.Scanner;

public class Misc
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
    static void clear(){
        System.out.print('\u000C');
    }
    static void printBar(){
        Misc.println("_________________________________________________________________");
    }
    static void printTitle(String title){
        Misc.printBar();
        Misc.println(title);
        Misc.printBar();
    }
    static Scanner keyboard = new Scanner(System.in);
}