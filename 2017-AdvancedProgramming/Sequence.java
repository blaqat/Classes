
import java.util.Scanner;

public class Sequence
{
    /* Gets a random number ranging from min - max
     * Easier use of Math.random() method
     */
    static int randomInt(int min, int max){
        return min + (int)( Math.random() * ( (max + 1) - min) );
    }

    static void main(){
        int number, length = 0;
        
        // Until the random number is 0, the sequence and sequence length continues to add on
        do{
            number = randomInt(0,9);

            System.out.print(number + " ");
            length++;
        }
        while(number != 0);

        System.out.print("\nLength of the sequence was " + length);
    }
}
