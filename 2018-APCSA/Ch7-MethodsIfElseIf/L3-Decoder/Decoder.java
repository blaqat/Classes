package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 

public class Decoder
{
    private char letter;

    public Decoder()
    {
        letter = 0;
    }

    public Decoder(char let)
    {
        setLetter(let);

    }

    public void setLetter(char let)
    {
        letter = let;

    }

    public char deCode()
    {
        char result=0;

        if(letter >= 'a' && letter <= 'z')
            result = (char)('A' + (letter - 'a'));
        else if(letter >= 'A' && letter <= 'Z')
            result = (char)('a' + (letter - 'A'));
        else if(letter >= '0' && letter <= '9')
            result = (char)('A' + (letter - '0'));
        else
            result = '#';

        return result;
    }

    public String toString()
    {
        return letter + " decodes to " + deCode() + "\n";
    }
}