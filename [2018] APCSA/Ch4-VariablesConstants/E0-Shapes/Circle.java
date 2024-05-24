/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class Circle
{
    public static void main (String[] args)
    {
        double radius;
        double circumference;
        double area;
        
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter radius: ");
        radius = keyboard.nextDouble();

        area = Math.PI * Math.pow(radius, 2);
        circumference = 2 * Math.PI * radius;

        if(radius > 0)
        {
            System.out.println("Area of a rectangle: " + (double)Math.round(area * 100)/100); //lua: print('Area of a rectangle: ',area)
            System.out.println("Circumference of a rectangle: " + (double)Math.round(circumference * 100)/100);
        }
        else
        {
          System.out.println("Invalid Input"); 
        }
    }
}