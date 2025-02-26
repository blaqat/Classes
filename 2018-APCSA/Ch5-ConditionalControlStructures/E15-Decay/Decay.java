/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;

public class Decay
{
    public static void main (String[] args)
    {
        double initial = 0, fin = 0, elapsed = 0, constant = 0, answer;
        String typ;
        Scanner keyboard = new Scanner(System.in);

        System.out.println("1.\tFinal Amount\n2.\tInitial Amount\n3.\tConstant (half-life)");
        System.out.print("Find: ");
        int choice = keyboard.nextInt();

        if(!(choice == 2)){
            System.out.print("Enter initial mass: ");
            initial = keyboard.nextDouble();            
        }
        if(!(choice == 1)){
            System.out.print("Enter final mass: ");
            fin = keyboard.nextDouble();
        }
        if(!(choice == 3)){
            System.out.print("Enter constant value");
            constant = keyboard.nextDouble();
        } 

        System.out.print("Enter elapsed time in years: ");
        elapsed = keyboard.nextDouble(); 

        switch(choice){
            case 1: typ = "Final Amount"; answer = initial * Math.exp(-constant * elapsed); break;
            case 2: typ = "Initial Amount"; answer = fin / Math.exp(-constant * elapsed); break;
            case 3: typ = "Constant (half-life)"; answer = Math.log(fin/initial)/elapsed; break;
            default: typ = "er"; answer = 0; System.out.print("Error; not a valid choice");
        }

        System.out.print(typ + ": " + answer);
    }
}