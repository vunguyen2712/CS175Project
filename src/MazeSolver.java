
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Logging.Log;


public class MazeSolver {

	public static boolean done = false;
	private static boolean debugRun = true;
	static String version = "1.7";

	public static int mazeSize = 15;
	public static int hardCap = mazeSize * 5;
	public static int move = 1;
	
	public static void main(String[] args)
	{
		System.out.println(hardCap);
		Maze maze = new Maze(mazeSize,mazeSize);
		Window window = new Window(maze);
		int score = 0;
		
		//Scanner sc = new Scanner(System.in);
		//sc.nextLine();

		//Calculate the path to the exit

		window.render();
		Agent agent = maze.getAgent();
		agent.calculatePathThroughMaze();
		try {
			TimeUnit.MILLISECONDS.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Game loop, done is set to true when exit is reached
		try
		{
			//while(!done && move <= hardCap)
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
				Reward temp = maze.checkRewards();
				if(!(temp == null))
				{
					score = score + temp.getValue();
					agent.collectReward(temp.getCell());
				}
				move++;
				System.out.println(move);
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
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!debugRun)
				Log.Log("Success", version);

			System.out.println("Solved!");
			System.out.println("Score - " + score);
		}
		catch (CaughtException e)
		{
			if(!debugRun)
				Log.Log("Failure", version);
			System.out.println("Agent was caught!");
			System.out.println("At position - (" + agent.getX() + "," + agent.getY() + ")");
			System.out.println("( " + agent.getLastCell().getCoordinates()[0] + ", " + agent.getLastCell().getCoordinates()[1] + ")");
		
		}
		//catch (EmptyStackException e)
		//{
			//System.out.println("Monster blocking the way outside of the entrance");
		//}
		catch(Exception e)
		{
			if(!debugRun)
				Log.Log("Error", version);
			System.out.println("Error - Below is the State -");
			System.out.println("Agent Position - (" + agent.getX() + "," + agent.getY() + ")");
			maze.printMonsterPositions();
			agent.printStack();
			agent.printnextMoves();
			System.out.println(e.getClass());
			e.printStackTrace();
		}
		
	}
}
