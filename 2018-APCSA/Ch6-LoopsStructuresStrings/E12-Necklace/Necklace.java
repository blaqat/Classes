/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class Necklace
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
    
    static int[] next(int i,int v){
        int[] ret = {v,(i+v)%10};
        return ret;
    }
    
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("------JEWLERY-MATHOMATOLOGISTS------");
        print("Enter the first starting number: ");
        int num1 = keyboard.nextInt();
        print("Enter the second starting number: ");
        int num2 = keyboard.nextInt();
        if(num1>9||num1<-9){num1=Math.abs(num1%10);println("Number 1 set to: " + num1);}
        if(num2>9||num2<-9){num2=Math.abs(num2%10);println("Number 2 set to: " + num2);}
        int key = num1;
        int future = num2;
        print(key+" "+future+" ");
        do{
         int[] keytab = next(key,future);
         key = keytab[0];
         future = keytab[1]; 
         print(future+" ");
        }while(!(key==num1 && future==num2));
        

    }
}