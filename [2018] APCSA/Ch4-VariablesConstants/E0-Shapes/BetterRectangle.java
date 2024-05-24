/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class BetterRectangle
{
    public static void main (String[] args)
    {
        int length = 0; //lua:  local length = 22
        int width = 0;
        int area = 0;
        int perimeter = 0;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter length: ");
        length = keyboard.nextInt();
        System.out.print("Please enter width: ");
        width = keyboard.nextInt();

        
        area = length * width;
        perimeter = width * 2 + length * 2;

        if(width>0 && length > 0)
        {
            System.out.println("Area of a rectangle: " + area); //lua: print('Area of a rectangle: ',area)
            System.out.println("Perimeter of a rectangle: " + perimeter);
        }
        else
        {
          System.out.println("Invalid Input"); 
        }
    }
}