package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 
import java.util.Scanner;

public class GradeCheck
{
	public static void main( String args[] )
	{
		Grade test = new Grade();
		Scanner keyboard = new Scanner(in);

		out.print("Enter a number grade :: ");
		int grade = keyboard.nextInt();
		test.setGrade(grade);
		out.println(test);

		out.print("Enter a number grade :: ");
		grade = keyboard.nextInt();
		test.setGrade(grade);
		out.println(test);

		out.print("Enter a number grade :: ");
		grade = keyboard.nextInt();
		test.setGrade(grade);
		out.println(test);

		out.print("Enter a number grade :: ");
		grade = keyboard.nextInt();
		test.setGrade(grade);
		out.println(test);

		out.print("Enter a number grade :: ");
		grade = keyboard.nextInt();
		test.setGrade(grade);
		out.println(test);		
	}
}