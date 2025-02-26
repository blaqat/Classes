package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import java.util.Scanner;
import static java.lang.System.*; 

public class MonsterRunner
{
	public static void main( String args[] )
	{
		Scanner keyboard = new Scanner(System.in);
		
		//ask for name and size
		out.print("Enter 1st monster's name : ");
		String mname = keyboard.next();
		out.print("Enter 1st monster's size : ");
		int msize = keyboard.nextInt();
		//instantiate monster one
		Monster monsterOne = new Monster(mname, msize);
		
		//ask for name and size
		out.print("Enter 2nd monster's name : ");
		mname = keyboard.next();
		out.print("Enter 2nd monster's size : ");
		msize = keyboard.nextInt();		
		//instantiate monster two
		Monster monsterTwo = new Monster(mname, msize);

		out.println();

		out.println("Monster 1 - " + monsterOne);
		out.println("Monster 2 - " + monsterTwo);

		out.println();

		out.println("Monster one is " + ((monsterOne.isBigger(monsterTwo))?"bigger":"smaller") + " than Monster two.");
		out.println("Monster one " + ((monsterOne.namesTheSame(monsterTwo))?"has":"does not have") + " the same name as Monster two.");

		out.println("\n\n");

		//ask for name and size
		out.print("Enter 1st monster's name : ");
		mname = keyboard.next();
		out.print("Enter 1st monster's size : ");
		msize = keyboard.nextInt();
		//instantiate monster one
		monsterOne = new Monster(mname, msize);
		
		//ask for name and size
		out.print("Enter 2nd monster's name : ");
		mname = keyboard.next();
		out.print("Enter 2nd monster's size : ");
		msize = keyboard.nextInt();		
		//instantiate monster two
		monsterTwo = new Monster(mname, msize);

		out.println();

		out.println("Monster 1 - " + monsterOne);
		out.println("Monster 2 - " + monsterTwo);

		out.println();

		out.println("Monster one is " + ((monsterOne.isBigger(monsterTwo))?"bigger":"smaller") + " than Monster two.");
		out.println("Monster one " + ((monsterOne.namesTheSame(monsterTwo))?"has":"does not have") + " the same name as Monster two.");

		out.println("\n\n");

		//ask for name and size
		out.print("Enter 1st monster's name : ");
		mname = keyboard.next();
		out.print("Enter 1st monster's size : ");
		msize = keyboard.nextInt();
		//instantiate monster one
		monsterOne = new Monster(mname, msize);
		
		//ask for name and size
		out.print("Enter 2nd monster's name : ");
		mname = keyboard.next();
		out.print("Enter 2nd monster's size : ");
		msize = keyboard.nextInt();		
		//instantiate monster two
		monsterTwo = new Monster(mname, msize);

		out.println();

		out.println("Monster 1 - " + monsterOne);
		out.println("Monster 2 - " + monsterTwo);

		out.println();

		out.println("Monster one is " + ((monsterOne.isBigger(monsterTwo))?"bigger":"smaller") + " than Monster two.");
		out.println("Monster one " + ((monsterOne.namesTheSame(monsterTwo))?"has":"does not have") + " the same name as Monster two.");

		out.println("\n\n");		
	}
}