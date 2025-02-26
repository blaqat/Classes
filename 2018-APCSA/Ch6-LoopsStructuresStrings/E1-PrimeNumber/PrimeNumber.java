/* Aiden
 * Programs: Ch6. 1,2,5,6,7,8,10,11,12,13,14,16
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class PrimeNumber
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

    static Boolean isPrime(int number){
        if(number>1){
            if(number==2) return true;
            if(number%2==0) return false;
            for(int i=3;i<(double)number/2;i+=2){
                if(number%i==0)return false;
            }
            return true;
        }
        return false;
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        println("---------FIND-PRIME-NUMBERS---------");
        
        print("Enter a number: ");
        int user = keyboard.nextInt();
        if(isPrime(user)) println("This is a prime number.");
        else println("This is not a prime number.");
        
        println("\n------FIND-PRIME-NUMBERS-BETWEEN-NUMBERS------");
        print("Enter another number: ");
        int user2 = keyboard.nextInt();
        if(user2<user){
            int[] temp = {user,user2};
            user = temp[1];
            user2 = temp[0];
        }
        println("Prime numbers in between " + user + " and " + user2 + " are:");
        for(int i=user+1;i<user2;i++){
            if(isPrime(i)) println(""+i);
        }
    }
}