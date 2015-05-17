package src;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
 
 
 public class Agent extends Moveable{
 	
 	private AStarCell currentCell;
 	private AStarCell nextCell;
 	//private Stack<Cell> visitedCells;
 	// visible items
 	private Cell exit;
 	private ArrayList<Reward> rewards;
 	private int rewardsLeft;
 	
	private Stack<AStarCell> path;
	private int cellCost = 1;
	private AStarCell entrance;
	
	private final int  bigNumber = 1000000;
	
	//private Stack<Cell> previousMoves;
	private Cell lastMove;
	
	//private Stack<AStarCell> previousCells;
	
	private boolean recalculatedLastTime = false;
 	
 	public Agent(Cell entrance, Cell exit, ArrayList<Reward> rewards)
 	{
 		
 		//visitedCells = new Stack<Cell>();
 		this.exit = exit;
 		this.rewards = rewards;
 		rewardsLeft = rewards.size();
 		
		path = new Stack<AStarCell>();
		
		this.entrance = new AStarCell(entrance, null, 0, 0);
		currentCell = this.entrance;
		
		//previousMoves = new Stack<Cell>();
		//lastMove = entrance;
		
		//previousCells = new Stack<AStarCell>();
		//previousCells.push(this.entrance);
 		//System.out.print("exit - ");
 		//exit.printCoords1();
 	}
 	
 	@Override
 	public void move() {
 		lastMove = currentCell.getCell();
 		
 		currentCell.getCell().moveCreatureOutOfCell(this);
 		nextCell.getCell().moveCreatureIntoCell(this);
 		currentCell = nextCell;
 		currentCell.getCell().aStarVisit();
 		
 		if(currentCell.equals(exit))
 		{
 			MazeSolver.done = true;
 		}
 	}
	/*@Override
 	public void calculateNextMove() {
 		//For each unvisited neighbor, calculate the distance to the goal node
 		//For now, the cost to go to that node is constant
 		//Take the path with the samllest value
 		//If no unvisited neighbors, go back to the last visited node
 		int bigNumber = 1000000000;
 		int minValue = bigNumber;
 		Cell minCell = exit;
 		for (Cell neighbor : currentCell.getNeighbors())
 		{
 			if(!neighbor.aStarVisited())
 			{
 			int temp = manhattanDistance(neighbor);
 			if(temp < minValue)
 			{
 				minCell = neighbor;
 				minValue = temp;
 			}
 			}
 		}
 		
 		if(minValue == bigNumber)
 		{
 			visitedCells.pop();
 			minCell = visitedCells.pop();
 
 			minCell.printCoords();
 		}
 		
 		nextCell = minCell;
 		visitedCells.push(nextCell);
+	}*/
	
 	@Override
	public Cell calculateNextMove() 
	throws EmptyStackException
	{
		//To detect if the agent will move to a cell with a monster on it, check if the nextCell has a 
		//monster in it

		nextCell = path.pop();
 		Cell fallbackCell = nextCell.getCell();
 		try
 		{
 			//Calculate if a monster is in one of the neighboring cells of nextCell
		boolean monstersInNeighbors = false;
		for (int i = 0; i < nextCell.getCell().getNeighbors().size(); i++)
		{
			Cell temp = nextCell.getCell().getNeighbors().get(i);
			if(temp.hasMonster())
				monstersInNeighbors =  true;
		}		
		if(nextCell.getCell().hasMonster() || monstersInNeighbors)
		{
 		if(!recalculatedLastTime)
 		{
		//aStarPath.pop();

		recalculatePathThroughMaze();
		recalculatedLastTime = true;
		//System.out.println("recalculated");
		
		monstersInNeighbors = false;
		for (int i = 0; i < nextCell.getCell().getNeighbors().size(); i++)
		{
			Cell temp = nextCell.getCell().getNeighbors().get(i);
			if(temp.hasMonster())
				monstersInNeighbors =  true;
		}
		//Path would still lead to agent being caught
		if(nextCell.getCell().hasMonster() || monstersInNeighbors || nextCell.equals(fallbackCell))
		{
			//moveBackOneCell ----- To avoid the monster
			calculateMoveWhenMonsterInFront();
		}
		else{
		
 		//previousCells.push(currentCell);
 		//lastMove = currentCell;
		}
 		}
 		else
 		{
 			recalculatedLastTime = false;
 			recalculatePathThroughMaze();
 			
 			monstersInNeighbors = false;
 			/*if(nextCell.hasMonster() || monstersInNeighbors)
 			{
 				recalculatePathThroughMaze();
 				recalculatedLastTime = true;
 				System.out.println("recalculated");
 			}*/
 			
 			monstersInNeighbors = false;
 			for (int i = 0; i < nextCell.getCell().getNeighbors().size(); i++)
 			{
 				Cell temp = nextCell.getCell().getNeighbors().get(i);
 				if(temp.hasMonster())
 					monstersInNeighbors =  true;
 			}
 			
 			if(nextCell.getCell().hasMonster() || monstersInNeighbors)
 			{
 				//moveBackOneCell ----- To avoid the monster
 				calculateMoveWhenMonsterInFront();
 				/*if(nextCell.equals(lastMove))
 				{
 					nextCell = previousMoves.pop();
 				}*/
 			}
 			else
 				nextCell = path.pop();
 			//previousCells.push(currentCell);
 		}
		}
		return nextCell.getCell();
 		}
 		catch(EmptyStackException E)
 		{
 			if(fallbackCell.equals(entrance))
 				return entrance.getCell();
 			return fallbackCell;
 		}
	}
	
	public void calculatePathThroughMaze()
	{
		boolean pathFound = false;
		ArrayList<AStarCell> possibleCells = new ArrayList<AStarCell>();
		ArrayList<AStarCell> searchedCells = new ArrayList<AStarCell>();
		
		//Initialize aStarFunction
		
		ArrayList<Cell> temp = currentCell.getCell().getNeighbors();
		searchedCells.add(entrance);
		
		for(int i = 0; i < temp.size(); i++)
		{
			Cell tempCell = temp.get(i);
			if(!searchedCells.contains(temp.get(i)))
			{
			int cost = calculateCellDistance(tempCell);
			
			possibleCells.add(new AStarCell(tempCell, entrance , cost, 0));
			//System.out.println("Added ("+ tempCell.getCoordinates()[0] + "," + tempCell.getCoordinates()[1] + ") - " + cost);
			}
		}
		//System.out.println(temp.size());
		
		//Find the "closest" neighbor
		AStarCell min = possibleCells.get(0);
		for(int i = 0; i < possibleCells.size(); i++)
		{
			int costForMin = min.getCost() + min.getHeuristic();
			int costForTempCell = possibleCells.get(i).getCost() + possibleCells.get(i).getHeuristic();
			if(costForTempCell < costForMin)
			{
				min = possibleCells.get(i);
				//System.out.println("newMinInInit");
			}
		}
		
		//System.out.println(possibleCells.size());
		searchedCells.add(min);
		possibleCells.remove(min);
		if(min.equals(exit))
		{
			pathFound = true;
			AStarCell tempCell = min;
			
			//Trace back your steps, adding each step to the path stack
			while(!tempCell.equals(entrance))
			{
			path.push(tempCell);
			//aStarPath.push(tempCell);
			
			tempCell = tempCell.getParentCell();
			}
			path.push(entrance);
		}
		while(!pathFound)
		{
			//System.out.println(iteration);
			//get the last cell added to searchedCells
			AStarCell mostRecentCell = searchedCells.get(searchedCells.size() - 1);
			
			//Add that cell's neighbors to the possibleCellsList
			
			temp = mostRecentCell.getCell().getNeighbors();
			//System.out.println("--------");
			//System.out.println(temp.size());
			
			//repeat, as done in Initialization step
			for(int i = 0; i < temp.size(); i++)
			{
				Cell tempCell = temp.get(i);
				if(!searchedCells.contains(temp.get(i)))
				{
				int cost = calculateCellDistance(tempCell);
				
				possibleCells.add(new AStarCell(tempCell, mostRecentCell , cost, mostRecentCell.getCost()+1));
				//System.out.println("Added ("+ tempCell.getCoordinates()[0] + "," + tempCell.getCoordinates()[1] + ") - " + cost);
				}
			}
			
			//System.out.println(possibleCells.size());
			
			min = possibleCells.get(0);
			for(int i = 0; i < possibleCells.size(); i++)
			{
				int costForMin = min.getCost() + min.getHeuristic();
				int costForTempCell = possibleCells.get(i).getCost() + possibleCells.get(i).getHeuristic();
				if(costForTempCell < costForMin)
				{
					min = possibleCells.get(i);
					//System.out.println("new min");
				}
				
				//System.out.println(i);
			}
			
			possibleCells.remove(min);
			searchedCells.add(min);
			
			if(min.equals(exit))
			{
				//System.out.println("Found");
				pathFound = true;
				AStarCell tempCell = min;
				
				//Trace back your steps, adding each step to the path stack
				while(!tempCell.equals(entrance))
				{
				path.push(tempCell);
				//aStarPath.push(tempCell);
				
				tempCell = tempCell.getParentCell();
				}
				path.push(entrance);
				//System.out.println("Path found!");
			}
			//System.out.println("Path NOT found! Keep Trying!");
		}
		path.pop();
		//previousMoves.push(entrance.getCell());
		
	}
	
	public void recalculatePathThroughMaze()
	{
		/*try{
			System.out.println("Recalculating");
			TimeUnit.MILLISECONDS.sleep(10000);
		}
		catch (Exception e)
		{
			
		}*/
		boolean pathFound = false;
		ArrayList<AStarCell> possibleCells = new ArrayList<AStarCell>();
		ArrayList<AStarCell> searchedCells = new ArrayList<AStarCell>();
		path = new Stack<AStarCell>();
		
		
		//Initialize aStarFunction
		
		ArrayList<Cell> temp = currentCell.getCell().getNeighbors();
		AStarCell startingPoint = currentCell;
		//aStarPath = new Stack<AStarCell>();
		searchedCells.add(startingPoint);
		
		for(int i = 0; i < temp.size(); i++)
		{
			Cell tempCell = temp.get(i);
			if(!searchedCells.contains(temp.get(i)))
			{
			int cost = calculateCellDistance(tempCell);
			
			possibleCells.add(new AStarCell(tempCell, startingPoint , cost, 0));
			//System.out.println("Added ("+ tempCell.getCoordinates()[0] + "," + tempCell.getCoordinates()[1] + ") - " + cost);
			}
		}
		//System.out.println(temp.size());
		
		//Find the "closest" neighbor
		AStarCell min = possibleCells.get(0);
		for(int i = 0; i < possibleCells.size(); i++)
		{				
		int costForMin = min.getCost() + min.getHeuristic();
		int costForTempCell = possibleCells.get(i).getCost() + possibleCells.get(i).getHeuristic();
		if(costForTempCell < costForMin)
		{
			min = possibleCells.get(i);
				//System.out.println("newMinInInit");
			}
		}
		
		//System.out.println(possibleCells.size());
		searchedCells.add(min);
		possibleCells.remove(min);

		while(!pathFound)
		{
			//System.out.println(iteration);
			//get the last cell added to searchedCells
			AStarCell mostRecentCell = searchedCells.get(searchedCells.size() - 1);
			
			//Add that cell's neighbors to the possibleCellsList
			
			temp = mostRecentCell.getCell().getNeighbors();
			//System.out.println("--------");
			//System.out.println(temp.size());
			
			//repeat, as done in Initialization step
			for(int i = 0; i < temp.size(); i++)
			{
				Cell tempCell = temp.get(i);
				if(!searchedCells.contains(temp.get(i)))
				{
				int cost = calculateCellDistance(tempCell);
				
				possibleCells.add(new AStarCell(tempCell, mostRecentCell , cost, mostRecentCell.getCost()+1));
				//System.out.println("Added ("+ tempCell.getCoordinates()[0] + "," + tempCell.getCoordinates()[1] + ") - " + cost);
				}
			}
			
			//System.out.println(possibleCells.size());
			
			min = possibleCells.get(0);
			for(int i = 0; i < possibleCells.size(); i++)
			{
				int costForMin = min.getCost() + min.getHeuristic();
				int costForTempCell = possibleCells.get(i).getCost() + possibleCells.get(i).getHeuristic();
				if(costForTempCell < costForMin)
				{
					min = possibleCells.get(i);
					//System.out.println("new min");
				}
				
				//System.out.println(i);
			}
			
			possibleCells.remove(min);
			searchedCells.add(min);
			
			if(min.equals(exit))
			{
				//System.out.println("Found");
				pathFound = true;
				AStarCell tempCell = min;
				
				//Trace back your steps, adding each step to the path stack
				while(!tempCell.equals(startingPoint))
				{
				path.push(tempCell);
				
				//aStarPath.push(tempCell);
				
				tempCell = tempCell.getParentCell();
				}
				//System.out.println("Path found!");
			}
			//System.out.println("Path NOT found! Keep Trying!");
		}
		
	}
	
	private int calculateCellDistance(Cell c)
	{
		int pathCost = cellCost;
		
		int nextRewardPos= rewardToVisit(c);
		int heuristic = manhattanDistance(c, rewards.get(nextRewardPos).getCell());
		
//		int heuristic = manhattanDistance(c);
		if(c.hasMonster())
		{
			pathCost = pathCost + bigNumber;
		}
		else
			pathCost = pathCost++;
		
		return pathCost + heuristic;
	}
	
	private int manhattanDistance(Cell c)
 	{
 		return Math.abs(exit.getCoordinates()[0]-c.getCoordinates()[0]) + Math.abs(exit.getCoordinates()[1]
 				-c.getCoordinates()[1]);
 	}

	private int manhattanDistance(Cell c, Cell goalCell)  // goalCell could be either reward of the exit
 	{
 		return Math.abs(goalCell.getCoordinates()[0]-c.getCoordinates()[0]) + Math.abs(goalCell.getCoordinates()[1]
 				-c.getCoordinates()[1]);
 	}

	
/*	rewardToVisit(Cell c) function returns the postion of the smallest reward
 */
	private int rewardToVisit(Cell c)
	{
		int minDistance = bigNumber;
		int minDistaneRewardPos = 0;
		for (int i = 0;  i < rewards.size(); ++i)
		{
			int distanceToReward = manhattanDistance(c, rewards.get(i).getCell());
			if (distanceToReward < minDistance)
			{
				minDistance = distanceToReward;
				minDistaneRewardPos = i;
			}
		}
		
		return minDistaneRewardPos;
	}
	
 	public int getX()
 	{
 		return currentCell.getCell().getCoordinates()[0];
 	}
 	
 	public int getY()
 	{
 		return currentCell.getCell().getCoordinates()[1];
 	}
 	
 	public void printStack()
 	{
 		System.out.println("Previous Moves - ");
 		//for(int i = 0; i < previousCells.size(); i++)
 		{
 			//System.out.print("(");
 			//Cell c = previousCells.get(i).getCell();
 			
 			//System.out.print(c.getCoordinates()[0] + "," + c.getCoordinates()[1] + ") - ");
 		}
 		System.out.println("");
 	}
 	
 	private void calculateMoveWhenMonsterInFront()
 	{
		if(!currentCell.equals(entrance))
		{
		nextCell = currentCell.getParentCell();
		path.push(currentCell);
		recalculatedLastTime = true;
		}
		else
		{
			nextCell = currentCell;
		}
 	}
 	
 	public void printnextMoves()
 	{
 		System.out.println("Next Moves - ");
 		for(int i = 0; i < path.size(); i++)
 		{
 			System.out.print("(");
 			Cell c = path.get(path.size() - i - 1).getCell();
 			
 			System.out.print(c.getCoordinates()[0] + "," + c.getCoordinates()[1] + ") - ");
 		}
 		System.out.println("");
 	}
 	
 	public Cell getLastCell()
 	{
 		return lastMove;
 	}
 	
 	public Cell getCurrentCell()
 	{
 		return currentCell.getCell();
 	}
 
 }