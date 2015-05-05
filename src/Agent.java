import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
 
 
 public class Agent extends Moveable{
 	
 	private Cell currentCell;
 	private Cell nextCell;
 	private Stack<Cell> visitedCells;
 	private Cell exit;
 	
	private Stack<Cell> path;
	private int cellCost = 1;
	private AStarCell entrance;
	
	private final int  bigNumber = 1000000;
	
	private Stack<Cell> previousMoves;
	private Cell lastMove;
	
	private Stack<AStarCell> previousCells;
	
	private boolean recalculatedLastTime = false;
 	
 	public Agent(Cell entrance, Cell exit)
 	{
 		currentCell = entrance;
 		visitedCells = new Stack<Cell>();
 		this.exit = exit;

		path = new Stack<Cell>();
		
		this.entrance = new AStarCell(entrance, null, 0, 0);
		
		previousMoves = new Stack<Cell>();
		lastMove = entrance;
		
		previousCells = new Stack<AStarCell>();
		previousCells.push(this.entrance);
 		//System.out.print("exit - ");
 		//exit.printCoords1();
 	}
 	
 	@Override
 	public void move() {
 		if(!currentCell.equals(entrance))
 		{
 		previousCells.push(new AStarCell(currentCell, previousCells.peek(), 0, 0));
 		}
 		currentCell.moveCreatureOutOfCell(this);
 		nextCell.moveCreatureIntoCell(this);
 		currentCell = nextCell;
 		currentCell.aStarVisit();
 		
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
 		Cell fallbackCell = nextCell;
 		try
 		{
 		if(!recalculatedLastTime)
 		{
		//aStarPath.pop();
		boolean monstersInNeighbors = false;
		for (int i = 0; i < nextCell.getNeighbors().size(); i++)
		{
			Cell temp = nextCell.getNeighbors().get(i);
			if(temp.hasMonster())
				monstersInNeighbors =  true;
		}
		
		if(nextCell.hasMonster() || monstersInNeighbors)
		{
			recalculatePathThroughMaze();
			recalculatedLastTime = true;
			//System.out.println("recalculated");
		}
		
		monstersInNeighbors = false;
		for (int i = 0; i < nextCell.getNeighbors().size(); i++)
		{
			Cell temp = nextCell.getNeighbors().get(i);
			if(temp.hasMonster())
				monstersInNeighbors =  true;
		}
		
		if(nextCell.hasMonster() || monstersInNeighbors)
		{
			//moveBackOneCell ----- To avoid the monster
			nextCell = previousMoves.pop();
			recalculatedLastTime = true;
			/*if(nextCell.equals(lastMove))
			{
				nextCell = previousMoves.pop();
			}*/
		}
		
 		previousMoves.push(currentCell);
 		lastMove = currentCell;
 		}
 		else
 		{
 			recalculatedLastTime = false;
 			recalculatePathThroughMaze();
 			
 			boolean monstersInNeighbors = false;
 			/*if(nextCell.hasMonster() || monstersInNeighbors)
 			{
 				recalculatePathThroughMaze();
 				recalculatedLastTime = true;
 				System.out.println("recalculated");
 			}*/
 			
 			monstersInNeighbors = false;
 			for (int i = 0; i < nextCell.getNeighbors().size(); i++)
 			{
 				Cell temp = nextCell.getNeighbors().get(i);
 				if(temp.hasMonster())
 					monstersInNeighbors =  true;
 			}
 			
 			if(nextCell.hasMonster() || monstersInNeighbors)
 			{
 				//moveBackOneCell ----- To avoid the monster
 				nextCell = previousMoves.pop();
 				/*if(nextCell.equals(lastMove))
 				{
 					nextCell = previousMoves.pop();
 				}*/
 			}
 			else
 				nextCell = path.pop();
 			previousMoves.push(currentCell);
 		}
		return nextCell;
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
		
		ArrayList<Cell> temp = currentCell.getNeighbors();
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
			if(possibleCells.get(i).getCost() < min.getCost() )
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
				if(possibleCells.get(i).getCost() < min.getCost())
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
				Cell c = tempCell.getCell();
				path.push(c);
				//aStarPath.push(tempCell);
				
				tempCell = tempCell.getParentCell();
				}
				path.push(entrance.getCell());
				//System.out.println("Path found!");
			}
			//System.out.println("Path NOT found! Keep Trying!");
		}
		path.pop();
		
	}
	
	public void recalculatePathThroughMaze()
	{
		boolean pathFound = false;
		ArrayList<AStarCell> possibleCells = new ArrayList<AStarCell>();
		ArrayList<AStarCell> searchedCells = new ArrayList<AStarCell>();
		path = new Stack<Cell>();
		
		
		//Initialize aStarFunction
		
		ArrayList<Cell> temp = currentCell.getNeighbors();
		AStarCell startingPoint = new AStarCell(currentCell, previousCells.pop(), 0, 0);
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
			if(possibleCells.get(i).getCost() < min.getCost() )
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
				if(possibleCells.get(i).getCost() < min.getCost())
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
				Cell c = tempCell.getCell();
				path.push(c);
				
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
		int heuristic = manhattanDistance(c);
		if(c.hasMonster())
		{
			pathCost = bigNumber;
		}
		
		return pathCost + heuristic;
	}
	
	private int manhattanDistance(Cell c)
 	{
 		return Math.abs(exit.getCoordinates()[0]-c.getCoordinates()[0]) + Math.abs(exit.getCoordinates()[1]
 				-c.getCoordinates()[1]);
 	}
 	
 	public int getX()
 	{
 		return currentCell.getCoordinates()[0];
 	}
 	
 	public int getY()
 	{
 		return currentCell.getCoordinates()[1];
 	}
 	
 	public void printStack()
 	{
 		System.out.println("Previous Moves - ");
 		for(int i = 0; i < previousMoves.size(); i++)
 		{
 			System.out.print("(");
 			Cell c = previousMoves.get(i);
 			
 			System.out.print(c.getCoordinates()[0] + "," + c.getCoordinates()[1] + ") - ");
 		}
 		System.out.println("");
 	}
 	
 	public void printnextMoves()
 	{
 		System.out.println("Next Moves - ");
 		for(int i = 0; i < path.size(); i++)
 		{
 			System.out.print("(");
 			Cell c = path.get(path.size() - i - 1);
 			
 			System.out.print(c.getCoordinates()[0] + "," + c.getCoordinates()[1] + ") - ");
 		}
 		System.out.println("");
 	}
 
 }