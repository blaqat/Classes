
import java.util.Scanner;

public class Temperature
{
    //Gets the average high temperature from an array of high temperatures
    static double averageHighTemp(int[] temps){
        double high = 0;

        for(int i = 0;  i<temps.length;  i++){
            high+=temps[i];
        }

        return high/temps.length;
    }

    static void main(){
        Scanner keyboard = new Scanner(System.in);
        int[] temperatures = new int[5]; //Used an array for if someone wanted to get specific days in the future

        //Asks the user to enter high temperature for 5 days and stores the data in the temperatures array
        for(int i = 1; i<=5; i++){ 
            System.out.print("Enter the high temperature on day " + i + ":\t");
            temperatures[i-1] = keyboard.nextInt();
        }

        System.out.print("\nThe average high temperature is: \t" + averageHighTemp(temperatures));
    }
}
