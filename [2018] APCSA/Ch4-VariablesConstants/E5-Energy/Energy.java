/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;


public class Energy
{
    public static void main (String[] args)
    {
        double mass = 0;
        double energy = 0;
        double c = 0;
        
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter the mass in kilograms: ");
        mass = keyboard.nextDouble();
        c = 3*100000000;
        energy = c*mass*c;
        System.out.print("The energy produced in Joules is - " + energy);
        System.out.print("\nThe number of 100-watt light bulbs powered = " + energy/360000);
    }
}