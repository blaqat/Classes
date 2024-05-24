/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class DiceRolls
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
    
    static void roll(){
        int ran1 = random(1,6);
        int ran2 = random(1,6);
        println(ran1+"\t"+ran2+"\t"+(ran1+ran2));
    }
    
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("------NOT-EVEN-'BOUT-LUCK------");
        println("Dice 1\tDice 2\tTotal");
        for(int i = 0;i!=5;i++){roll();}
        
    }
}
