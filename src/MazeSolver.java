/*
 * ICS 175 Group 1
 * 
 * Maze Solver is the main class for the AI. The process goes as follows -
 * 1) Create the Maze
 * 2) Initialize the score and the status
 * 3) Display the state of the maze
 * 4) while the agent has not reached the exit and while the move hard cap hasn't been reached - 
 * 		- calculate where the monsters and the agent should move next
 * 		- move them
 * 		-detect if a major change occured (Agent caught, reward eaten, agent reaches exit)
 * 		- display the new state of the maze
 * 
 * 5) record whether the agent has been caught or if it finished the maze, or if an error occured
 */




import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Logging.Log;


public class MazeSolver {

	public static boolean done = false;
	private static boolean debugRun = true;
	static String version = "1.11";

	public static int mazeSize = 15;
	public static int hardCap = mazeSize * 10;
	public static int move = 1;
	
	public static void main(String[] args)
	{
		//System.out.println(hardCap);
		
		// Step 1
		Maze maze = new Maze(mazeSize,mazeSize);
		Window window = new Window(maze);
		
		// Step 2
		int score = 0;
		String status = "Collecting rewards";
		//Scanner sc = new Scanner(System.in);
		//sc.nextLine();
		
		// Step 3
		window.render(score, 0, hardCap, status);
		Agent agent = maze.getAgent();
		
		//Have the agent calculate its path to a goal
		agent.calculatePathThroughMaze();
		try {
			if(!debugRun)
			{TimeUnit.MILLISECONDS.sleep(00);
			}
			else
			{

				TimeUnit.MILLISECONDS.sleep(400);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		//Game loop, done is set to true when exit is reached
		try
		{
			while(!done && (move <= hardCap))
			//while(!done)
			{
				//Calculate where to go next
				maze.calculateNextMove();
				if(agent.getHeadToExit())
					status = "Heading toward exit";
				//Move
				maze.moveAll();
				//Display
				window.render(score, move, hardCap, status);
				//Detect monsters Catching Agent
				maze.detectCatches();
				Reward temp = maze.checkRewards();
				if(!(temp == null))
				{
					score = score + temp.getValue();
					agent.collectReward(temp.getCell());
				}
				move++;
				if(move > hardCap)
				{
					throw new CaughtException("Cap");
				}
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
					if(!debugRun)
					{TimeUnit.MILLISECONDS.sleep(00);
					}
					else
					{

						TimeUnit.MILLISECONDS.sleep(400);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//Logging section
			if(!debugRun)
				Log.Log("Success", version, score);
			status = "Exit reached";
			window.render(score, move, hardCap, status);
			System.out.println("Solved!");
			System.out.println("Score - " + score);
		}
		catch (CaughtException e)
		{
			if(e.getMessage().equals("Cap"))
			{
				status = "Out of moves";
				System.out.println("Hard cap was reached");
				window.render(score, move-1, hardCap, status);
			}
			else
			{
				status = "Caught";
				System.out.println("Agent was caught!");
				System.out.println("At position - (" + agent.getX() + "," + agent.getY() + ")");
				System.out.println("( " + agent.getLastCell().getCoordinates()[0] + ", " + agent.getLastCell().getCoordinates()[1] + ")");
				window.render(score, move, hardCap, status);
			}
			if(!debugRun)
				Log.Log("Failure", version, score);
			
		}
		//catch (EmptyStackException e)
		//{
			//System.out.println("Monster blocking the way outside of the entrance");
		//}
		catch(Exception e)
		{
			if(!debugRun)
				Log.Log("Error", version, score);
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
