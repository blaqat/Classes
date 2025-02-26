import java.util.Scanner;
public class QuadriaticEquation
{
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);
        double a;
        double b;
        double c;
        double quadp;
        double quadn;
        
        System.out.print("Enter value for a: ");
        a = keyboard.nextInt();
        System.out.print("Enter value for b: ");
        b = keyboard.nextInt();
        System.out.print("Enter value for c: ");
        c = keyboard.nextInt();
        
        quadp = ( -(b) + Math.sqrt(Math.pow(b,2) - 4 * a * c))/(2 * a);
        quadn = ( -(b) - Math.sqrt(Math.pow(b,2) - 4 * a * c))/(2 * a);
        System.out.print("The roots are " + quadp + " and " + quadn);
        
    }
}