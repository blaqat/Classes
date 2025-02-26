import java.util.Scanner;
public class Volume
{
    public static void main (String[] args)
    {
        double val;
        Scanner keyboard = new Scanner(System.in);

        //Volume of rectangular prism
        System.out.println("Rectangular Prism");
        System.out.print("Enter the length: ");
        val = keyboard.nextDouble();
        System.out.print("Enter the width: ");
        val *= keyboard.nextDouble();
        System.out.print("Enter the height: ");
        val *= keyboard.nextDouble();
        System.out.println("The volume is: " + val);

        //Volume of a sphere
        System.out.println("\nSphere");
        System.out.print("Enter the radius: ");
        val = keyboard.nextDouble();
        //(double)Math.round(area * 100)/100)
        val = Math.PI * Math.pow((val * 2),3)/6;
        val = (double)Math.round(val * 100)/100;
        System.out.println("The volume is: " + val);

        //Volume of a cube
        System.out.println("\nCube");
        System.out.print("Enter the length of each side: ");
        val = keyboard.nextDouble();
        val = Math.pow(val,3);
        System.out.println("The volume is: " + val);

        
    }
}