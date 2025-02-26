/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class Delivery
{
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter package weight in kilograms: ");
        int weight = keyboard.nextInt();
        System.out.print("Enter package legnth in centimeters: ");
        int area = keyboard.nextInt();
        System.out.print("Enter package width in centimeters: ");
        area *= keyboard.nextInt();
        System.out.print("Enter package height in centimeters: ");
        area *= keyboard.nextInt();
        area /= 100000;
        
        if(weight > 27 && !(area > .1)){
            System.out.print("Too heavy.");
        }
        else if(area > .1 && !(weight > 27)){
            System.out.print("Too large.");
        }
        else if(area > .1 && weight > 27){
            System.out.print("Too heavy and too large.");
        }
        else{
            System.out.print("Package meets requirements");
        }
    }
}