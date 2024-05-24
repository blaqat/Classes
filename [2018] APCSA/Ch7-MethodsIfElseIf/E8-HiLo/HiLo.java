/* Aiden
 * Programs: Ch7
 * Programming - 4y
 * I hear 7 is a lucky number o-o
 * Maybe its time to move away from triangles to no sided shapes...
 * like hexagons :)
 */
import java.util.Scanner;

public class HiLo
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

    static void checkPoints(int points){println("You have " + points + " points.\n");}

    static int Play(int points){
        Scanner keyboard = new Scanner(System.in);
        checkPoints(points);
        if(points<=0){
            println("Sorry you don't have enough points to play!");
            return points;
        }

        boolean don = false;
        int doub = random(1,10);
        if(doub==7){
            don=true;
            println("----------[WARNING:] DOUBLE OR NOTHING----------\n");
        }

        print("Enter points to risk: ");
        int risk = keyboard.nextInt();
        while(risk > points){
            println("You don't have that many points...");
            print("Enter points to risk: ");
            risk = keyboard.nextInt();           
        }
        if(don)risk*=2;

        print("Predict (1 = High, 0 = Neither, -1 = Low): ");
        int pred = keyboard.nextInt();
        int computer = random(1,13);
        int hilo;
        if(computer>=8&&computer<=13)hilo = 1;
        else if(computer>=1&&computer<=6)hilo = -1;
        else if(computer==7)hilo = 0;
        else {print("ERR:/"); return points;}

        println("Number is " + computer);
        if(pred==hilo){println("You win! (+"+risk*2+" points)\n"); points+=risk*2;}
        else {println("You lose. (-"+risk+" points)\n"); points-=risk;}

        if(don&&pred!=hilo){println("]:) Better luck next time!");points=0;}

        return points;
    }

    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        println("High Low Games\n");
        println("RULES\nNumbers 1 - 6 are low\nNumbers 8 - 13 are high\nNumber 7 is neither high or low\n");
        int coins = 1000;

        while(coins>0){
            coins = Play(coins);
            if(coins>0){
                print("PLAY AGAIN? [y/n]");
                String answer = keyboard.next();
                if(answer.equals("n")){coins = 0;break;}
            }
        }
        print("\nGAME OVER");
    }
}