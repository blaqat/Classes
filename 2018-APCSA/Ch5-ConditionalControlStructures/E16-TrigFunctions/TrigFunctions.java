/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class TrigFunctions
{
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        
        System.out.print("Enter an angle in degrees: ");
        double angle = keyboard.nextDouble();
        angle = Math.toRadians(angle);
        
        System.out.println("Sine: " + (double)Math.round(Math.sin(angle)*100)/100);
        System.out.println("Cosine: " + (double)Math.round(Math.cos(angle)*100)/100);
        System.out.println("Tangent: " + (double)Math.round(Math.tan(angle)*100)/100);
    }
}