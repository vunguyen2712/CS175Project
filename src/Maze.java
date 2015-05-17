package src;

import java.util.ArrayList;
import java.util.Random;


public class Maze {

	private Cell[][] maze;
	private Cell entrance;
	private Cell exit;
	private ArrayList<Monster> monsters;
	private Agent agent;
	private ArrayList<Reward> rewards;
	
	public Maze(int mazeWidth, int mazeHeight)
	{
		//Initialize and call the big method
		maze = new Cell[mazeWidth][mazeHeight];
		monsters = new ArrayList<Monster>();
		rewards = new ArrayList<Reward>();
		generateMaze(mazeWidth, mazeHeight);
		
		/*int[] encoords = entrance.getCoordinates();
		int enx = encoords[0];
		int eny = encoords[1];
		System.out.println("Entrance Coords - (" + enx + "," + eny + ")");*/
		//System.out.println(exit.getCoordinates() + "/n");
		/*for (int i = 0; i< mazeWidth; i++)
		{
			for(int j = 0; j < mazeHeight; j++)
			{
				Cell tempCell = maze[i][j];
				int[] coords = tempCell.getCoordinates();
				int x = coords[0];
				int y = coords[1];
				System.out.print("Cell (" + x + "," + y + ") Has - ");
				System.out.println(tempCell.getNeighbors().size() + " neighbors, listed below -");
				for(int k =0; k<tempCell.getNeighbors().size(); k++)
				{
					Cell neighborCell = tempCell.getNeighbors().get(k);
					coords = neighborCell.getCoordinates();
					x= coords[0];
					y = coords[1];
					System.out.println("(" + x + "," + y + ")");
				}
			}
		}*/
		/*for (int m = 0; m < monsters.size(); m++)
		{
			int x = monsters.get(m).getX();
			int y = monsters.get(m).getY();
			
			System.out.println("(" + x + "," + y + ")");
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
		int maxCycles;
		int amountOfCyclesInMaze = 0;
		
		if(mazeWidth <= mazeHeight)
		{
			maxCycles = mazeWidth *2;
		}
		else
		{
			maxCycles = mazeHeight *2;
		}
		
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
		
		exit = createExit(mazeHeight, mazeWidth, endSide, endOffset);
		
		entrance = createEntrance(mazeHeight, mazeWidth, startSide, startOffset);
		//don't allow exit and entrance to be the same
		while(exit.equals(entrance))
		{
			endSide = getSide();
			if(endSide == 1 || endSide == 3)
			{
				endOffset = getOffset(mazeHeight);
			}
			else
			{
				endOffset = getOffset(mazeWidth);
			}
			exit = createExit(mazeHeight, mazeWidth, endSide, endOffset);
		}

		createMaze(entrance, maxCycles, 0, 0);
		
		//Create the monsters and agent
		setMonsters(mazeWidth, mazeHeight);
		setRewards(mazeWidth, mazeHeight);
		agent = new Agent(entrance, exit, rewards);
		getEntrance().setEntrance(agent);
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
		//Create the entrance. Create the Agent as well so that we can link to it in the Entrance Cell
		if(startSide == 0)
		{
			//Bottom
			//agent = new Agent(maze[startOffset][0], exit);
			//maze[startOffset][0].setEntrance(agent);
			return maze[startOffset][0];
		}
		else if (startSide == 1)
		{
			//Left
			//agent = new Agent(maze[0][startOffset], exit);
			//maze[0][startOffset].setEntrance(agent);
			return maze[0][startOffset];
		}
		else if (startSide == 2)
		{
			//Top
			//agent = new Agent(maze[mazeHeight-1][startOffset], exit);
			//maze[mazeHeight-1][startOffset].setEntrance(agent);
			return maze[mazeHeight-1][startOffset];
		}
		else 
		{
			//Right
			//agent = new Agent(maze[startOffset][mazeWidth-1], exit);
			//maze[startOffset][mazeWidth-1].setEntrance(agent);
			return maze[startOffset][mazeWidth-1];
		}
	}
	
	private Cell createExit(int mazeHeight, int mazeWidth, int endSide, int endOffset)
	{
		//Create the exit
		if(endSide == 0)
		{
			//Bottom
			//Cell temp = maze[endOffset][0];
			//while ()
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
	private void createMaze(Cell currentCell, int maxCycles, int amountOfCyclesInMaze, int amountOfStepsSinceLastCycle)
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
				amountOfStepsSinceLastCycle++;
				createMaze(neighbor, maxCycles, amountOfCyclesInMaze, amountOfStepsSinceLastCycle);
			}
			else if(neighbor.visited() && amountOfCyclesInMaze < maxCycles && amountOfStepsSinceLastCycle > 4)
			{
				neighbor.visitNeighbor(currentCell);
				currentCell.visitNeighbor(neighbor);
				amountOfCyclesInMaze++;
				currentCell.setImportance(5);
				amountOfStepsSinceLastCycle = 0;
				createMaze(neighbor, maxCycles, amountOfCyclesInMaze, amountOfStepsSinceLastCycle);
			}
			
			if(currentCell.getNumberOfVisitedNeighbors() == currentCell.getNeighbors().size())
			{
				allVisited = true;
			}
		}

		currentCell.setNeighbors(currentCell.getVisitedNeighbors());
	}
	
	public void setMonsters(int mazeWidth, int mazeHeight)
	{
		int max;
		if(mazeWidth <= mazeHeight)
		{
			max = mazeWidth;
		}
		else
		{
			max = mazeHeight;
		}
		
		Random random = new Random();
		
		
		for (int i = 0; i< max/2; i++)
		{
			
			int randomX = random.nextInt(mazeWidth);
			int randomY = random.nextInt(mazeHeight);
			Cell monsterCell = maze[randomX][randomY];
			
			while(monsterCell.equals(entrance) || monsterCell.equals(exit))
			{
				randomX = random.nextInt(mazeWidth);
				randomY = random.nextInt(mazeHeight);
				monsterCell = maze[randomX][randomY];
			}
			
			Monster temp = new Monster(monsterCell);
			monsters.add(temp);
			maze[randomX][randomY].moveCreatureIntoCell(temp);
		}
		
	}
	
	private void setRewards(int mazeWidth, int mazeHeight)
	{
		
		int max;
		if(mazeWidth <= mazeHeight)
		{
			max = mazeWidth;
		}
		else
		{
			max = mazeHeight;
		}
		
		Random random = new Random();
		
		for (int i = 0; i< max/2; i++)
		{
			
			int randomX = random.nextInt(mazeWidth);
			int randomY = random.nextInt(mazeHeight);
			Cell rewardCell = maze[randomX][randomY];
			
			while(rewardCell.equals(entrance) || rewardCell.equals(exit) || rewardCell.hasReward())
			{
				randomX = random.nextInt(mazeWidth);
				randomY = random.nextInt(mazeHeight);
				rewardCell = maze[randomX][randomY];
			}
			int value = random.nextInt(6);
			value = value * 100;
			Reward temp = new Reward(rewardCell, value);
			rewards.add(temp);
			maze[randomX][randomY].setReward(temp);
		}
	}
	
	public Cell getEntrance()
	{
		return entrance;
	}
	
	public Cell getExit()
	{
		return exit;
	}
	
	public Cell[][] getMaze(){
		return maze;
	}
	
	public ArrayList<Monster> getMonsters()
	{
		return monsters;
	}
	
	public ArrayList<Reward> getReward()
	{
		return rewards;
	}
	
	public Agent getAgent()
	{
		return agent;
	}
		
	public boolean monstersCollision(Cell nextCell, int monPos)
	{
		for (int i = 0; i < monPos; ++i)
		{
			if (monsters.get(i).getNextCell().equals(nextCell))
			{
				return true;
			}
		
		}
		return false;
	}
	 
	public void calculateNextMove()
	{
		agent.calculateNextMove();
		
		for (int i = 0; i < monsters.size(); ++i)
		{
			Cell movedToCell = monsters.get(i).calculateNextMove();
			// check if there is no collision in next move
			int recalTimes = 0;  // used to check for trap
			while (monstersCollision(movedToCell,i))
			{
				movedToCell = monsters.get(i).calculateNextMove(); // recalculate the next random postion
				++ recalTimes;
				//System.out.println("monster collision");
				if (recalTimes > 3) // if encounter a trap
				{
					// recalculate next postion for all monsters
					for (int j = 0; j < i; ++j)
					{
						monsters.get(j).calculateNextMove();
					}
					movedToCell = monsters.get(i).calculateNextMove(); // recalculate the next random postion
					recalTimes = 0;
					//System.out.println("recalculated all next move for monsters!");
				}
			}
			//System.out.println("monster new postion!");
		}
	}
	
	public void moveAll()
	{
		agent.move();
		for(Monster m : monsters)
		{
			m.move();
		}
	}
	
	public void printMonsterPositions()
	{
		System.out.print("Monster Positions -");
		for (Monster m : monsters)
		{
			System.out.print(" (" + m.getX() + "," + m.getY() + ")");
		}
		System.out.println("");
	}
	
	public void detectCatches()
	throws CaughtException
	{
		for (Monster m : monsters)
		{
			if(m.getX() == agent.getX() && m.getY() == agent.getY())
			{
				throw new CaughtException("Caught");
			}
			//The agent and the monster moved past eachother in the "hallway
			else if(m.getLastCell().equals(agent.getCurrentCell())
					&& m.getCurrentCell().equals(agent.getLastCell()))
			{
				System.out.println("( " + m.getCurrentCell().getCoordinates()[0] + ", " + m.getCurrentCell().getCoordinates()[1] + ")");
				System.out.println("( " + m.getLastCell().getCoordinates()[0] + ", " + m.getLastCell().getCoordinates()[1] + ")");
				throw new CaughtException("Caught");
			}
		}
	}
}
