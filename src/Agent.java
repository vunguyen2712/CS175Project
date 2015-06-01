package src;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;
 
 
 public class Agent extends Moveable{
 	
 	private AStarCell currentCell;
 	private AStarCell nextCell;
 	//private Stack<Cell> visitedCells;
 	// visible items
 	private Cell exit;
 	private ArrayList<Reward> rewards;
 	
	private Stack<AStarCell> path;
	private int cellCost = 1;
	private AStarCell entrance;
	private Cell currentGoal;
	private Cell nearestReward;
	private boolean headToExit;
	private Stack<AStarCell> pathToExit;
	
	private final int  bigNumber = 1000000;
	
	//private Stack<Cell> previousMoves;
	private Cell lastMove;
	
	//private Stack<AStarCell> previousCells;
	
	private boolean recalculatedLastTime = false;
	
	private String heuristic;
	private ArrayList<Reward> nearbyRewards;
 	
 	public Agent(Cell entrance, Cell exit, ArrayList<Reward> rewards)
 	{
 		
 		//visitedCells = new Stack<Cell>();
 		this.exit = exit;
 		this.rewards = rewards;
 		for (int i=0; i < this.rewards.size(); ++i)
 		{
 			System.out.println("goal Cell = (" + rewards.get(i).getCell().getCoordinates()[0] + "," + rewards.get(i).getCell().getCoordinates()[1]+")" + "--- value: " + rewards.get(i).getValue());
 		}
 		
		path = new Stack<AStarCell>();
		
		this.entrance = new AStarCell(entrance, null, 0, 0);
		currentCell = this.entrance;
		currentGoal = exit;
		nearestReward = calculateGoal();
		headToExit = false;
		
		pathToExit = new Stack<AStarCell>();
		calculatePathToExit(entrance);
		System.out.println(pathToExit());
		
		heuristic = "VoD";
		//heuristic = "Cluster";
//		heuristic = "Max Distance";
		//previousMoves = new Stack<Cell>();
		//lastMove = entrance;
		
		
		nearbyRewards = new ArrayList<Reward>();
		
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
 		calculatePathToExit(nextCell.getCell());
 		
 		if(currentCell.equals(exit))
 		{
 			MazeSolver.done = true;
 		}
 		if(nearbyRewards.size() > 0 && nearbyRewards.contains(currentCell))
 		{
 			nearbyRewards.remove(currentCell);
 		}
 	}
	
 	@Override	
	public Cell calculateNextMove() 
	throws EmptyStackException
	{
		//To detect if the agent will move to a cell with a monster on it, check if the nextCell has a 
		//monster in it

 		int pathToExit = pathToExit();
 		int timeLeft = MazeSolver.hardCap - MazeSolver.move;
 		
 		if(timeLeft < pathToExit + MazeSolver.mazeSize && !headToExit)
 		{
 			System.out.println("time to head for the exit");
 			headToExit = true;
 			recalculatePathThroughMaze();
 		}
 		try
 		{
			nextCell = path.pop();
	 		Cell fallbackCell = nextCell.getCell();
 		try
 		{
 			//Calculate if a monster is in one of the neighboring cells of nextCell
		boolean monstersInNeighbors = calculateIfMonsterInNextCell(nextCell);
		if(nextCell.getCell().hasMonster() || monstersInNeighbors)
		{
	 		if(!recalculatedLastTime)
	 		{
				//aStarPath.pop();
		
				recalculatePathThroughMaze();
				recalculatedLastTime = true;
				//System.out.println("recalculated");
				
				monstersInNeighbors = calculateIfMonsterInNextCell(nextCell);
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
	 			
	 			monstersInNeighbors = calculateIfMonsterInNextCell(nextCell);
	 			
	 			if(nextCell.getCell().hasMonster() || monstersInNeighbors)
	 			{
	 				//moveBackOneCell ----- To avoid the monster
	 				calculateMoveWhenMonsterInFront();

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
 		//Reached a reward
 		catch (EmptyStackException e)
 		{
 			recalculatePathThroughMaze();
 			nextCell = path.pop();
 			return nextCell.getCell();
 		}
	}
	
	public void calculatePathThroughMaze()
	{
		boolean pathFound = false;
		ArrayList<AStarCell> possibleCells = new ArrayList<AStarCell>();
		ArrayList<AStarCell> searchedCells = new ArrayList<AStarCell>();
		Cell goal = calculateBestGoal();
		currentGoal = goal;
		
		//Initialize aStarFunction
		
		ArrayList<Cell> temp = currentCell.getCell().getNeighbors();
		searchedCells.add(entrance);
		
		for(int i = 0; i < temp.size(); i++)
		{
			Cell tempCell = temp.get(i);
			if(!searchedCells.contains(temp.get(i)))
			{
			int cost = calculateCellDistance(tempCell, goal);
			
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
		if(min.equals(goal))
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
				int cost = calculateCellDistance(tempCell, goal);
				
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
			
			if(min.equals(goal))
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
		
		Cell goal = calculateBestGoal();
		boolean pathFound = false;
		ArrayList<AStarCell> possibleCells = new ArrayList<AStarCell>();
		ArrayList<AStarCell> searchedCells = new ArrayList<AStarCell>();
		path = new Stack<AStarCell>();
		currentGoal = goal;
		
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
			int cost = calculateCellDistance(tempCell, goal);
			
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

		if(min.equals(goal))
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
				int cost = calculateCellDistance(tempCell, goal);
				
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
			
			if(min.equals(goal))
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
	
	private void calculatePathToExit(Cell nextCell)
	{
		if(!headToExit && !nextCell.equals(exit))
		{
	
		//Cell goal = exit;
		boolean pathFound = false;
		ArrayList<AStarCell> possibleCells = new ArrayList<AStarCell>();
		ArrayList<AStarCell> searchedCells = new ArrayList<AStarCell>();
		pathToExit = new Stack<AStarCell>();
		
		
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
			int cost = calculateCellDistance(tempCell, exit);
			
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
	
		if(min.equals(exit))
		{
			//System.out.println("Found");
			pathFound = true;
			AStarCell tempCell = min;
			
			//Trace back your steps, adding each step to the path stack
			while(!tempCell.equals(startingPoint))
			{
			pathToExit.push(tempCell);
			
			//aStarPath.push(tempCell);
			
			tempCell = tempCell.getParentCell();
			}
			//System.out.println("Path found!");
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
				int cost = calculateCellDistance(tempCell, exit);
				
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
				pathToExit.push(tempCell);
				
				//aStarPath.push(tempCell);
				
				tempCell = tempCell.getParentCell();
				}
				//System.out.println("Path found!");
			}
			//System.out.println("Path NOT found! Keep Trying!");
		}
		}
	}
	
	private int calculateCellDistance(Cell c, Cell goal)
	{
		int pathCost = cellCost;
		
		//int nextRewardPos= rewardToVisit(c);
		int heuristic = manhattanDistance(c, goal);
		
//		int heuristic = manhattanDistance(c);
		if(c.hasMonster())
		{
			pathCost = pathCost + bigNumber;
		}
		else
			pathCost = pathCost++;
		
		return pathCost + heuristic;
	}
	
/* calculateGoal() returns the closest reward (Cell)
 * 
 */
	private Cell calculateGoal()
	{
		Cell c = currentCell.getCell();
		Cell goalCell;
		if(rewards.size() > 0 && !headToExit)
		//if(rewards.size() > 0)
		{
			int closestRewardIndex = rewardToVisit(c);
			Cell closestReward = rewards.get(closestRewardIndex).getCell();
			goalCell = closestReward;
			//System.out.println("goal Cell = (" + goalCell.getCoordinates()[0] + "," + goalCell.getCoordinates()[1]+")");
		}
		else
		{
			//System.out.println("goal cell = exit");
			goalCell = exit;
		}
		return goalCell;
	}

	/*
	 * calculateGoalGroups() returns the closest cell to the agent, which is in a tightly packed group 
	 * (the idea is that the agent should try to go to rewards that collect rewards in rapid succession
	 */
	
/* calculateBestGoal() returns the best reward (Cell) - based on reward value / distance
 * 
 */
	private Cell calculateBestGoal()
	{
		Cell c = currentCell.getCell();
		Cell goalCell = exit;
		if(heuristic.equals("VoD"))
		{
		if(rewards.size() > 0 && !headToExit)
		//if(rewards.size() > 0)
		{
			int bestRewardIndex = bestRewardToVisit(c);
			goalCell = rewards.get(bestRewardIndex).getCell();
			System.out.println("goal Cell = (" + goalCell.getCoordinates()[0] + "," + goalCell.getCoordinates()[1]+")");
		}
		else
		{
			//System.out.println("goal cell = exit");
			goalCell = exit;
		}
		}
		
		
		else if(heuristic.equals("Cluster"))
		{
			if(nearbyRewards.size() == 0 && !headToExit)
			{
			//Calculate value based on the neighbors who are near to a cell
			int clusterValue = 0;
			Cell bestGoal = exit;
			for (Reward r : rewards)
			{
				try
				{
					ArrayList<Reward> temp = search(r.getCell(), 0, new ArrayList<Cell>(), new ArrayList<Reward>());
					
					int tempValue = addValue(temp);
					
					if(tempValue > clusterValue)
					{
						bestGoal = r.getCell();
						clusterValue = tempValue;
						nearbyRewards = temp;
					}
					else if(tempValue == clusterValue)
					{
						int distanceToCurrentBest = manhattanDistance(currentCell.getCell(), bestGoal);
						int distanceToThisReward = manhattanDistance(currentCell.getCell(), r.getCell());
						if(distanceToThisReward < distanceToCurrentBest)
						{
							bestGoal = r.getCell();
							clusterValue = tempValue;
							nearbyRewards = temp;
						}
					}
				}
				catch (IndexOutOfBoundsException e)
				{
					//no path found, ignore this reward
				}
			}
			goalCell = bestGoal;
			System.out.println(goalCell.printCoords());
			//nearbyRewards.remove(bestGoal);
			}
			else if (headToExit)
			{
				goalCell = exit;
			}
			else
			{
				int bestRewardIndex = bestRewardToVisit(c);
				goalCell = nearbyRewards.get(bestRewardIndex).getCell();
				//nearbyRewards.remove(bestRewardIndex);
				goalCell.printCoords1();
			}
		}
		
		
		
		else if(heuristic.equals("Max Distance"))
		{
			if(headToExit)
				goalCell = exit;
			else
				goalCell = getFarthestReward();
		}
		return goalCell;
	}
	
	private int addValue(ArrayList<Reward> rewards)
	{
		int returnValue =0;
		for(Reward r : rewards)
		{
			returnValue = returnValue + r.getValue();
		}
		return returnValue;
	}
	
	private ArrayList<Reward> search(Cell cell, int depth, ArrayList<Cell> searched, ArrayList<Reward> rewardsInCluster)
	{
		int maxDepth = 5;
		int clusterValue = 0;
		searched.add(cell);
		ArrayList<Reward> re = rewardsInCluster;
		if(depth > maxDepth)
		{
			return rewardsInCluster;
		}
		else
		{
			int d = depth + 1;
			if(cell.hasReward())
			{
				clusterValue = clusterValue + cell.getReward().getValue();
				re.add(cell.getReward());
			}
			
			for(Cell n : cell.getNeighbors())
			{
				if(!searched.contains(n))
				{
					re = search(n, d, searched,re);
				}
			}
			return re;
		}
	}
	
	private int manhattanDistance(Cell c)
 	{
 		return Math.abs(exit.getCoordinates()[0]-c.getCoordinates()[0]) + Math.abs(exit.getCoordinates()[1]
 				-c.getCoordinates()[1]);
 	}

	private int manhattanDistance(Cell c, Cell goalCell)  // goalCell could be either reward or the exit
 	{
 		return Math.abs(goalCell.getCoordinates()[0]-c.getCoordinates()[0]) + Math.abs(goalCell.getCoordinates()[1]
 				-c.getCoordinates()[1]);
 	}

	private int pathToExit()
	{
		return pathToExit.size();
	}
	
/*	rewardToVisit(Cell c) function returns the index of the closest reward
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
/*	bestRewardToVisit(Cell c) function returns the index of the BEST reward - based on rewardValue / Distance - VoD
 */
	private int bestRewardToVisit(Cell c)
	{
		double maxVoD = -1.0;
		int bestRewardPos = 0;
		
		int minManhattanD = bigNumber; // min and < 6
		int minPosWithinFiveSteps = -1;  // position of the minManhattanD
		boolean foundAwardWithinFiveSteps = false; 
		
		for (int i = 0;  i < rewards.size(); ++i)
		{	
			int manhattanD = manhattanDistance(c, rewards.get(i).getCell());
			double VoD = rewards.get(i).getValue() /  (manhattanD / 1.0);
//			System.out.println("Value: " + rewards.get(i).getValue() +  " ManahatDis: " +  manhattanDistance(c, rewards.get(i).getCell()) + " VoD " + i + " = " + VoD);
			if (manhattanD <  6)  // go to rewards within 5 manhattanDistance unit, if there isn't any near by goal -> go for the goal with the best VoD value
			{
				if(manhattanD < minManhattanD)
				{
					minManhattanD = manhattanD;
					minPosWithinFiveSteps = i;
				}
				foundAwardWithinFiveSteps = true;
			}
			if (VoD > maxVoD)
			{
				maxVoD = VoD;
				bestRewardPos = i;
			}
		}
		if (foundAwardWithinFiveSteps)
			return minPosWithinFiveSteps;
		else
			return bestRewardPos;
	}

	private Cell getFarthestReward()
	{
		int maxDistance = 0;
		Cell farthestCell;
		if(rewards.size()>0)
		{
		farthestCell = rewards.get(0).getCell();
		
		for(Reward r: rewards)
		{
			int distance = manhattanDistance(r.getCell());
			if(distance > maxDistance)
			{
				farthestCell = r.getCell();
				maxDistance = distance;
			}
		}
		
		}
		else
			farthestCell = exit;
		return farthestCell;
	}
	
	public void collectReward(Cell c)
	{
		Iterator<Reward> iter = rewards.iterator();

		while (iter.hasNext()) {
		    Reward r = iter.next();

		    if (r.getCell().equals(c))
		        iter.remove();
		}
		
		Iterator<Reward> i = nearbyRewards.iterator();
		while(i.hasNext())
		{
			Reward r = i.next();
			if(r.getCell().equals(c))
			{
				i.remove();
			}
		}
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
 	
/* 	calculateMoveWhenMonsterInFront() moves the agent back 1 move when a monster is in the front
 */
 	private void calculateMoveWhenMonsterInFront()
 	{
		if(!currentCell.equals(entrance))
		{
		nextCell = currentCell.getParentCell();
		path.push(currentCell);
		recalculatedLastTime = true;
		
		//Moving back would cause a collision
		if(calculateIfMonsterInNextCell(nextCell))
		{
			nextCell = currentCell;
		}
		}
		else
		{
			nextCell = currentCell;
		}
 	}
 	
 	private boolean calculateIfMonsterInNextCell(AStarCell nextCell)
 	{
 		boolean monstersInNeighbors = false;
			for (int i = 0; i < nextCell.getCell().getNeighbors().size(); i++)
			{
				Cell temp = nextCell.getCell().getNeighbors().get(i);
				if(temp.hasMonster())
					monstersInNeighbors =  true;
			}
			
			return monstersInNeighbors;
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
 	
 	public Cell getNextCell()
 	{
 		if(!path.empty())
 			return path.peek().getCell();
 		return getCurrentCell();
 	}
 	
 	public boolean getHeadToExit(){
 		return headToExit;
 	}
 	
 	public void caught(){
 		currentCell.getCell().moveCreatureOutOfCell(this);
 		currentCell = new AStarCell(lastMove, currentCell, 0, 0);
 	}
 
 	public void removeReward(Reward reward)
 	{
 		nearbyRewards.remove(reward);
 	}
 }