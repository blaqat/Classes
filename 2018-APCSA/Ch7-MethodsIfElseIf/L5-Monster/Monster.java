package Labs;

//© A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class -
//Lab  -

import static java.lang.System.*; 

public class Monster
{
	private String name;
	private int howBig;	

	public Monster()
	{
		name = "";
		howBig = 0;

	}

	public Monster(String n, int size)
	{
		name = n;
		howBig = size;

	}

	public String getName()
	{
		return name;
	}
	
	public int getHowBig()
	{
		return howBig;
	}
	
	public boolean isBigger(Monster other)
	{
		return howBig > other.getHowBig() ;
	}
	
	public boolean isSmaller(Monster other)
	{
		//call isBigger() use !  <<-- IT SAID TO USE THIS DONT @ ME
		return !isBigger(other);
	}

	public boolean namesTheSame(Monster other)
	{
		return other.getName().equals(name);
	}
	
	public String toString()
	{
		return name + " " + howBig;
	}
}