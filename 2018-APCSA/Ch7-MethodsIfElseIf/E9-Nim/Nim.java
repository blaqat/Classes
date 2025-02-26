/* Aiden
 * Programs: Ch7
 * Programming - 4y
 * I hear 7 is a lucky number o-o
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
 */
import java.util.Scanner;

public class Nim
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

    static boolean isValidEntry(int entry, int stones)
    {
        if((entry>3 || entry < 1) || entry > stones)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    static int drawStones(int type,int stones){
        int input;
        if(type==-1) input=random(1,3);
        else input = type;
        if(isValidEntry(input,stones)) return input;
        else if(stones > 0) {
            return drawStones(type,stones);
        }
        else return 0;
    }

    static int nextRound(int stones){
        Scanner input = new Scanner(System.in);
        int entry = 0;
        while(!isValidEntry(entry,stones)&&stones>0)
        {
            print("There are "+stones+" stones. How many would you like? ");
            entry = input.nextInt();
            if(stones-drawStones(entry,stones)<=0) {println("The computer won!"); return -1;}
        }
        stones -= drawStones(entry,stones);
        int com = drawStones(-1,stones); 
        println("There are "+stones+" stones. The computer takes "+com+" stones.");
        if(stones-com<=0) {println("The player won!"); return -1;}
        return stones-com;
    }

    public static void main (String[] args)
    {
        Scanner input = new Scanner(System.in);
        int stones = random(15,30);
        while(stones>=0){
            stones = nextRound(stones);
        }
    }
}