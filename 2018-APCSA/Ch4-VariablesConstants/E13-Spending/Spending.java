/* Aiden
 * 10/4/17
 * Programming - 4y
 * Triangles are the best shape
 * But we are making rectangles
 */

import java.util.Scanner;
import java.text.NumberFormat;

public class Spending
{
    public static void main (String[] args)
    {
        double food;
        double clothes;
        double entertainment;
        double rent;
        double total;
        
        Scanner keyboard = new Scanner(System.in);
        NumberFormat percent = NumberFormat.getPercentInstance();

        System.out.print("Enter the amount spent last month on the following items:\n");
        
        System.out.print("Food: ");
        food = Double.valueOf(keyboard.next().replace("$", ""));
        System.out.print("Clothing: ");
        clothes = Double.valueOf(keyboard.next().replace("$", ""));
        System.out.print("Entertainment: ");
        entertainment = Double.valueOf(keyboard.next().replace("$", ""));
        System.out.print("Rent: ");
        rent = Double.valueOf(keyboard.next().replace("$", ""));
        
        total = food + clothes + entertainment + rent;
        food /= total/100;
        clothes /= total/100;
        entertainment /= total/100;
        rent /= total/100;
        
        System.out.println("Category\tBudget");
        System.out.println("Food\t\t" + (double)Math.round(food * 100) / 100 + "%");        //+  percent.format(food/total));
        System.out.println("Clothing\t" + (double)Math.round(clothes * 100) / 100 + "%");      //+    percent.format(clothes/total));
        System.out.println("Entertainment\t" + (double)Math.round(entertainment * 100) / 100  + "%"); //+   percent.format(entertainment/total));
        System.out.println("Rent\t\t" + (double)Math.round(rent * 100) / 100 + "%");        //+  percent.format(rent/total));
        
    }
}