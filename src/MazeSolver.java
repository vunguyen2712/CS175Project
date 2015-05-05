import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Logging.Log;


public class MazeSolver {

	public static boolean done = false;
	public static void main(String[] args)
	{
		Maze maze = new Maze(15,15);
		Window window = new Window(maze);
		
		//Scanner sc = new Scanner(System.in);
		//sc.nextLine();

		//Calculate the path to the exit
		Agent agent = maze.getAgent();
		agent.calculatePathThroughMaze();
		window.render();
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Game loop, done is set to true when exit is reached
		try
		{
			while(!done)
			{
				//Calculate where to go next
				maze.calculateNextMove();
				//Move
				maze.moveAll();
				//Display
				window.render();
				//Detect monsters Catching Agent
				maze.detectCatches();
				//System.out.println("(" + agent.getX() + "," + agent.getY() + ")");
				//agent.printStack();
				//agent.printnextMoves();
				/* prints monster coordinates
				for(int i = 0; i < maze.getMonsters().size(); i++){
					System.out.print(maze.getMonsters().get(i).getCurrentCell().printCoords());
				}
				System.out.println();
				*/
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.Log("Success");

			System.out.println("Solved!");
		}
		catch (CaughtException e)
		{

			Log.Log("Failure");
			System.out.println("Agent was caught!");
			System.out.println("At position - (" + agent.getX() + "," + agent.getY() + ")");
		}
		//catch (EmptyStackException e)
		//{
			//System.out.println("Monster blocking the way outside of the entrance");
		//}
		catch(Exception e)
		{

			Log.Log("Error");
			System.out.println("Error - Below is the State -");
			System.out.println("Agent Position - (" + agent.getX() + "," + agent.getY() + ")");
			maze.printMonsterPositions();
			agent.printStack();
			agent.printnextMoves();
		}
		
	}
	
}
