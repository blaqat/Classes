package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 
import java.util.Scanner;
public class MorseCodeRunner
{
    public static void main( String args[] )
    {
        Scanner keyboard = new Scanner(in);
        MorseCode test = new MorseCode();
        char letter;

        letter = 'A';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);

        letter = 'B';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);

        letter = '3';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);

        letter = 'Z';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);

        letter = '8';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);

        letter = 'F';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);

        letter = '0';
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);
        
        out.print("Enter a letter :: ");
        letter = keyboard.next().charAt(0);
        test.setChar(letter);
        out.println(test.getMorseCode());
        out.println(test);        
    }
}