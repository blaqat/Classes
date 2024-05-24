package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 

public class Grade
{
    private int numGrade;

    public Grade()
    {
        setGrade(0);
    }

    public Grade(int grade)
    {
        setGrade(grade);
    }

    public void setGrade(int grade)
    {
        numGrade = grade;
    }

    public String getLetterGrade( )
    {
        String letGrade="";

        letGrade = (numGrade >= 90)?"A":
        (numGrade >= 80)?"B":
        (numGrade >= 75)?"C":
        (numGrade >= 70)?"D":
        "F";

        return letGrade;
    }

    public String toString()
    {
        return numGrade + " is a " + getLetterGrade() + "\n";
    }
}