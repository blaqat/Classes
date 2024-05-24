
public class Rectangle_Player
{
    static void main(){
        Rectangle square = new Rectangle(5,5);
        Rectangle rect = new Rectangle(5,2);
        
        Misc.println("Square: ");
        Misc.println("Area: " + square.getArea() + " | Perimeter: " + square.getPerimeter());
        Misc.println("" + square);
        
        Misc.println("\nRectangle: ");
        Misc.println("Area: " + rect.getArea() + " | Perimeter: " + rect.getPerimeter());
        Misc.println("" + rect);
        
        if(square.equals(rect)){
            Misc.println("\nSquare and Rectangle are equal");
        }
        else {
            Misc.println("\nSquare and Rectangle are not equal");
        }
    }
}
