package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 
import java.util.Scanner;

public class HexToBinaryRunner
{
    public static void main( String args[] )
    {
        Scanner keyboard = new Scanner(in);
        HexToBinary test = new HexToBinary();

        out.print("Enter a letter :: ");
        char letter = keyboard.next().charAt(0);
        test.setHex(letter);
        out.println(test);

        out.print("Enter a letter :: ");
        letter = keyboard.next().charAt(0);
        test.setHex(letter);
        out.println(test);

        out.print("Enter a letter :: ");
        letter = keyboard.next().charAt(0);
        test.setHex(letter);
        out.println(test);

        out.print("Enter a letter :: ");
        letter = keyboard.next().charAt(0);
        test.setHex(letter);
        out.println(test);

        out.print("Enter a letter :: ");
        letter = keyboard.next().charAt(0);
        test.setHex(letter);
        out.println(test);
        
        out.print("Enter a letter :: ");
        letter = keyboard.next().charAt(0);
        test.setHex(letter);
        out.println(test);
    }
}