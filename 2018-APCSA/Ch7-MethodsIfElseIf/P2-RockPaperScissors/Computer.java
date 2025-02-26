//© A+ Computer Science  -  www.apluscompsci.com

//Computer class 

public class Computer
{
	//instance / member variables
   private String choice;
   
	public Computer()
	{
		randomSetChoice();	
	}
	
	public String getChoice()
	{
		return choice;
	}
	
	public void randomSetChoice()
	{
		//use Math.random()
		int ranchoice = (int)(2 * Math.random() + 1);
		//use switch case
		switch(ranchoice){
			case 1 : choice = "rock"; break;
			case 2 : choice = "paper"; break;
			case 3 : choice = "scissors"; break;
		}
	}	
		
	/*
	 didIWin(Player p) will return the following values
	 	0 - both players have the same choice
	 	1 - the computer had the higher ranking choice
	 	-1 - the player had the higher ranking choice
	 */	
	public int didIWin(Player p)
	{
		String[] choices = {"rock", "paper", "scissors"};
		String pchoice = p.getChoice();

		int pc = (pchoice == "rock")?0:(pchoice == "paper")?1:2;
		int c = (choice == "rock")?0:(choice == "paper")?1:2;

		if(pc == c)
			return 0;
		else if(choices[(c+1)%3].equals(pchoice))
			return  -1;
		else
			return 1;

	}
	
	public String toString()
	{
		return "pooter " + choice;
	}		   
}