/*
 * CS 175 Group 1
 * 
 * CaughtException is a simple exception that allows us to quickly detect when a monster catches the agent
 */
public class CaughtException extends Exception{
	
	public CaughtException(String message)
	{
		super(message);
	}

}
