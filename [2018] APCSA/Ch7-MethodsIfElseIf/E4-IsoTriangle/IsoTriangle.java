/* Aiden
 * Programs: Ch7
 * Programming - 4y
 * I hear 7 is a lucky number o-o
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
 */
import java.util.Scanner;

public class IsoTriangle
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

    static void drawBar(int length, String mark){
        for (int i = 1; i <= length; i++) {
            System.out.print(mark);
        }
        System.out.println();
    }
    
    static void addSpaces(int length){
        for (int i = 1;i<=length;i++){
            System.out.print(" ");
        }
    }
    
    static void newIsoTriangle(int size){
        int count = 0;
        for(int i = 1;i<=size;i++){
            addSpaces(size-i);
            drawBar(i+count,"*");
            count++;
        }
    }
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        print("Enter the size: ");
        newIsoTriangle(keyboard.nextInt());
    }
}