import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class MazeSolver {

	public static boolean done = false;
	public static void main(String[] args)
	{
		Maze maze = new Maze(10,10);
		Window window = new Window();
		window.render(maze);
		//Scanner sc = new Scanner(System.in);
		//sc.nextLine();
		
		//Game loop, done is set to true when exit is reachd
		while(!done)
		{
			//Calculate where to go next
			maze.calculateNextMove();
			//Move
			maze.moveAll();
			//Display
			window = new Window();
			window.render(maze);
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Solved!");
	}
	
}
