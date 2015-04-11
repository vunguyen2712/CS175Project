import java.util.Random;


public class Maze {

	private Cell[][] maze;
	
	public Maze(int mazeWidth, int mazeHeight)
	{
		//Initialize and call the big method
		maze = new Cell[mazeWidth][mazeHeight];
		generateMaze(mazeWidth, mazeHeight);
	}
	
	private void generateMaze(int mazeWidth, int mazeHeight)
	{
		throw new RuntimeException("implement check to make sure start/end are different");
		//Create a random number to determine which side of the maze the start location & end location are
		//0 = bottom, 1 = left, 2 = top, 3 = right
		Random random = new Random();
		int startSide = random.nextInt(4);
		int endSide = random.nextInt(4);
		
		//The Offset is the x or y value of the start/end, depending if it on the left/right or top/bottom
		int startOffset;
		//Start on the left or right side of the maze
		if(startSide == 1 || startSide == 3)
		{
			startOffset = random.nextInt(mazeHeight);
		}
		//Start on the Top or bottom of the maze
		else
		{
			startOffset = random.nextInt(mazeWidth);
		}
		
		int endOffset;
		//Same as the startOffset, but for the exit of the maze
		if(endSide == 1 || endSide == 3)
		{
			endOffset = random.nextInt(mazeHeight);
		}
		else
		{
			endOffset = random.nextInt(mazeWidth);
		}
		
		for(int i = 0; i<mazeWidth; i++)
		{
			for(int j = 0; j <mazeHeight;j++)
			{
				maze[i][j] = new Cell(i,j);
			}
		}
		
		Agent agent;
		//Create the entrance. Create the Agent as well so that we can link to it in the Entrance Cell
		if(startSide == 0)
		{
			//Bottom
			agent = new Agent(startOffset,0);
			maze[startOffset][0].setEntrance(agent);
		}
		else if (startSide == 1)
		{
			//Left
			agent = new Agent(0,startOffset);
			maze[0][startOffset].setEntrance(agent);
		}
		else if (startSide == 2)
		{
			//Top
			agent = new Agent(mazeHeight-1, startOffset);
			maze[mazeHeight-1][startOffset].setEntrance(agent);
		}
		else 
		{
			//Right
			agent = new Agent(startOffset, mazeWidth);
			maze[startOffset][mazeWidth-1].setEntrance(agent);
		}
		
		//Create the exit
		if(endSide == 0)
		{
			//Bottom
			maze[endOffset][0].setExit();
		}
		else if (endSide == 1)
		{
			//Left
			maze[0][endOffset].setExit();
		}
		else if (startSide == 2)
		{
			//Top
			maze[mazeHeight-1][endOffset].setExit();
		}
		else 
		{
			//Right
			maze[endOffset][mazeWidth-1].setExit();
		}
	}
}
