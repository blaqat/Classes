/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

public class Rectangle
{
    public static void main (String[] args)
    {
        int legnth = 22; //lua:  local length = 22
        int width = 5;
        int area = 0;
        int perimeter = 0;
        
        area = legnth * width;
        perimeter = width * 2 + legnth * 22;
        
        System.out.println("Area of a rectangle: " + area); //lua: print('Area of a rectangle: ',area)
        System.out.println("Perimeter of a rectangle: " + perimeter);
    }
}