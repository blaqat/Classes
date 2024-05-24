/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */
import java.util.Scanner;
import java.text.NumberFormat;
public class Grade
{
    public static void main (String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Enter the grade: ");
        double grade = keyboard.nextDouble();

        if(grade >= 90){
            System.out.println("A");
        }
        else if(grade >= 80){
            System.out.println("B");
        }  
        else if(grade >= 70){
            System.out.println("C");
        }    
        else if(grade >= 60){
            System.out.println("D");
        }    
        else{
            System.out.println("F");
        }    

    }
}