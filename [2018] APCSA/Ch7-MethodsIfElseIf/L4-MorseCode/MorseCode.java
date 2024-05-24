package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 

public class MorseCode
{
    private char letter;

    public MorseCode()
    {
        letter = 0;
    }

    public MorseCode(char let)
    {
        setChar(let);
    }

    public void setChar(char let)
    {
        letter = let;
    }

    public String getMorseCode()
    {
        String[] morseCodes = {
                ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", 
                "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", 
                "..-", "...-", ".--", "-..-", "-.--", "--..", "-----", ".----", "..---", 
                "...--", "....-", ".....", "-....", "--...", "---..", "----."
            };

        String morse="";

        int morseIndex = letter-((letter >= 'A' && letter <= 'Z')?'A':
                (letter >= '0' && letter <= '9')?'0'-26:0);

        morse = (morseIndex > morseCodes.length - 1)?"ERROR":morseCodes[morseIndex];

        return morse;       
    }

    public String toString()
    {
        return letter + " is " + getMorseCode() + " in morse!\n";
    }
}