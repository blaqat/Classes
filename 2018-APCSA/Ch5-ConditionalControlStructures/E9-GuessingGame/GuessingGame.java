/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;
import java.text.NumberFormat;

public class GuessingGame
{
    public static void main (String[] args)
    {
        final int computer = (int)(Math.random()*(20)+1);
        int user;
        String type = "";

        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter a number between 1 and 20: ");
        user = keyboard.nextInt();

        if(user < 1 && user > 20)
        {
            System.out.print("You didn't enter a number in between 1 and 20 smh...");
        }
        else{
            System.out.println("Computer's Number: " + computer);
            System.out.println("Player's Number: " + user);
            if(user == computer){
                System.out.println("You were correct! Congratz!11");
            }
            else{
                System.out.println("Better luck next time");
            }
        }
    }
}