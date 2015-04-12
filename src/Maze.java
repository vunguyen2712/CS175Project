import java.util.ArrayList;
import java.util.Random;


public class Maze {

	private Cell[][] maze;
	private Cell entrance;
	private Cell exit;
	
	public Maze(int mazeWidth, int mazeHeight)
	{
		//Initialize and call the big method
		maze = new Cell[mazeWidth][mazeHeight];
		generateMaze(mazeWidth, mazeHeight);
		
		/*for (int i = 0; i< mazeWidth; i++)
		{
			for(int j = 0; j < mazeHeight; j++)
			{
				Cell tempCell = maze[i][j];
				int[] coords = tempCell.getCoordinates();
				int x = coords[0];
				int y = coords[1];
				System.out.print("(" + x + "," + y + ") - ");
				System.out.println(tempCell.getNeighbors().size());
				/*for(int k =0; k<tempCell.getNeighbors().size(); i++)
				{
					Cell neighborCell = tempCell.getNeighbors().get(k);
					int[] coords = neighborCell.getCoordinates();
					int x = coords[0];
					int y = coords[1];
					System.out.println("(" + x + "," + y + ")");
				}
			}
		}*/
	}
	
	private void generateMaze(int mazeWidth, int mazeHeight)
	{
		//Create a random number to determine which side of the maze the start location & end location are
		//0 = bottom, 1 = left, 2 = top, 3 = right
		
		int startSide = getSide();
		int endSide = getSide();
		
		//The Offset is the x or y value of the start/end, depending if it on the left/right or top/bottom
		int startOffset;
		//Start on the left or right side of the maze
		if(startSide == 1 || startSide == 3)
		{
			startOffset = getOffset(mazeHeight);
		}
		//Start on the Top or bottom of the maze
		else
		{
			startOffset = getOffset(mazeWidth);
		}
		
		int endOffset;
		//Same as the startOffset, but for the exit of the maze
		if(endSide == 1 || endSide == 3)
		{
			endOffset = getOffset(mazeHeight);
		}
		else
		{
			endOffset = getOffset(mazeWidth);
		}
		
		//Detect whether the entrance/exit could possibly by the same... this can be changed later so that
		//We can allow them to be on the same side
		while(startSide == endSide)
		{
			endSide = getSide();
		}
		
		for(int i = 0; i<mazeWidth; i++)
		{
			for(int j = 0; j <mazeHeight;j++)
			{
				maze[i][j] = new Cell(i,j);
			}
		}
		
		for(int i = 0; i<mazeWidth; i++)
		{
			for(int j = 0; j <mazeHeight;j++)
			{
				//ArrayList<> neighbors = {maze[i-1][j], maze[i+1][j], maze[i][j-1], maze[i][j+1]};
				ArrayList<Cell> neighbors = new ArrayList<Cell>();
				
				if(!(i==0))
				{
					neighbors.add(maze[i-1][j]);
				}
				if(!(i==(mazeWidth-1)))
				{
					neighbors.add(maze[i+1][j]);
				}
				if(!(j == 0))
				{
					neighbors.add(maze[i][j-1]);
				}
				if(!(j==(mazeHeight-1)))
				{
					neighbors.add(maze[i][j+1]);
				}
				
				maze[i][j].setNeighbors(neighbors);
			}
		}
		
		entrance = createEntrance(mazeHeight, mazeWidth, startSide, startOffset);
		
		exit = createExit(mazeHeight, mazeWidth, endSide, endOffset);
		
		createMaze(entrance);
	}
	
	private int getSide()
	{
		Random random = new Random();
		return random.nextInt(4);
	}
	
	private int getOffset(int maxSize)
	{
		Random random = new Random();
		return random.nextInt(maxSize);
	}
	
	private Cell createEntrance(int mazeHeight, int mazeWidth, int startSide, int startOffset)
	{
		Agent agent;
		//Create the entrance. Create the Agent as well so that we can link to it in the Entrance Cell
		if(startSide == 0)
		{
			//Bottom
			agent = new Agent(startOffset,0);
			maze[startOffset][0].setEntrance(agent);
			return maze[startOffset][0];
		}
		else if (startSide == 1)
		{
			//Left
			agent = new Agent(0,startOffset);
			maze[0][startOffset].setEntrance(agent);
			return maze[0][startOffset];
		}
		else if (startSide == 2)
		{
			//Top
			agent = new Agent(mazeHeight-1, startOffset);
			maze[mazeHeight-1][startOffset].setEntrance(agent);
			return maze[mazeHeight-1][startOffset];
		}
		else 
		{
			//Right
			agent = new Agent(startOffset, mazeWidth);
			maze[startOffset][mazeWidth-1].setEntrance(agent);
			return maze[startOffset][mazeWidth-1];
		}
	}
	
	private Cell createExit(int mazeHeight, int mazeWidth, int endSide, int endOffset)
	{
		//Create the exit
		if(endSide == 0)
		{
			//Bottom
			maze[endOffset][0].setExit();
			return maze[endOffset][0];
		}
		else if (endSide == 1)
		{
			//Left
			maze[0][endOffset].setExit();
			return maze[0][endOffset];
		}
		else if (endSide == 2)
		{
			//Top
			maze[mazeHeight-1][endOffset].setExit();
			return maze[mazeHeight-1][endOffset];
		}
		else 
		{
			//Right
			maze[endOffset][mazeWidth-1].setExit();
			return maze[endOffset][mazeWidth-1];
		}
	}
	
	
	//Does a DFS of the maze, starting at the entrace
	/*
	 * 1) Starting at the entrance, pick a neighbor that has not been visited yet
	 * 2) Mark that neighbor as visited
	 * 3) Put the neighbor in the "peopleIVisited" list - to symbolize that you visited this cell, 
	 * and not someone else
	 * 4) Put yourself in that neighbors visited list
	 * 5) Recurse, using that neighbor
	 * 6) Once finished with each unvisited neighbor, set the list of neighbors (available cells to move to)
	 * as the list of neighbors the currentCell visited
	 */
	private void createMaze(Cell currentCell)
	{
		//Fix this, make it a random neighboring cell
		boolean allVisited = false;
		
		while(!allVisited)
		{
			Random random = new Random();
			int i = random.nextInt(currentCell.getNeighbors().size());
			Cell neighbor = currentCell.getNeighbors().get(i);
			
			if(!neighbor.visited())
			{
				neighbor.visitNeighbor(currentCell);
				currentCell.visitNeighbor(neighbor);
				createMaze(neighbor);
			}
			
			if(currentCell.getNumberOfVisitedNeighbors() == currentCell.getNeighbors().size())
			{
				allVisited = true;
			}
		}

		currentCell.setNeighbors(currentCell.getVisitedNeighbors());
	}
	
	public Cell getEntrance()
	{
		return entrance;
	}
	
	public Cell getExit()
	{
		return exit;
	}
}
