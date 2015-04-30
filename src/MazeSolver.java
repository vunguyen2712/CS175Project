import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class MazeSolver {

	public static boolean done = false;
	public static void main(String[] args)
	{
		Maze maze = new Maze(5,5);
		Window window = new Window();
		window.render(maze);
		//Scanner sc = new Scanner(System.in);
		//sc.nextLine();
		
		//Calculate the path to the exit
		Agent agent = maze.getAgent();
		agent.calculatePathThroughMaze();
		//Game loop, done is set to true when exit is reachd
		try
		{
		while(!done)
		{
			//Calculate where to go next
			maze.calculateNextMove();
			//Move
			maze.moveAll();
			//Display
			window = new Window();
			window.render(maze);
			//Detect monsters Catching Agent
			maze.detectCatches();
			//System.out.println("(" + agent.getX() + "," + agent.getY() + ")");
			//agent.printStack();
			//agent.printnextMoves();
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Solved!");
		}
		catch (CaughtException e)
		{
			System.out.println("Agent was caught!");
			System.out.println("At position - (" + agent.getX() + "," + agent.getY() + ")");
		}
		//catch (EmptyStackException e)
		//{
			//System.out.println("Monster blocking the way outside of the entrance");
		//}
		
	}
	
}
