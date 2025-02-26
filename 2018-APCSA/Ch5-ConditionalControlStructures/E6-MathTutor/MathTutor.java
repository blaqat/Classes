/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;
import java.text.NumberFormat;

public class MathTutor
{
    public static void main (String[] args)
    {
        int num1 = (int)(Math.random()*10)+1;
        int num2 = (int)(Math.random()*10)+1;
        int sign = (int)(Math.random()*(5-1)+1);
        double answer = 0;
        String type = "";

        Scanner keyboard = new Scanner(System.in);

        switch(sign) {
            case 1 : answer = num1+num2; type = "+"; break;
            case 2 : answer = num1-num2; type = "-"; break;
            case 3 : answer = num1*num2; type = "×"; break;
            case 4 : answer = (double)num1/(double)num2; type = "÷"; break;
        }

        System.out.print("What is " + num1 + " " + type + " " + num2 + "? ");
        if(keyboard.nextDouble() == answer){
            System.out.print("Correct!");
        }
        else{
            System.out.print("Incorrect, the correct answer was: " + answer);
        }
    }
}