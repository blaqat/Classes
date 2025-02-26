//© A+ Computer Science  -  www.apluscompsci.com

//Player class 

public class Player
{
	private String name;
	private String choice;

	public Player(String nm)
	{
		setName("Player1");
		setChoice("");
	} 	
	
	public Player(String nm, String ch)
	{
		setName(nm);
		setChoice(ch);
	} 
		
	public void setName( String nm)
	{
		name = nm;
	}	
		
	public void setChoice( String ch )
	{	
		choice = ch;	
	}	
		
	public String getChoice()
	{
		return choice;
	}	
		
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name + " " + choice;
	}
}