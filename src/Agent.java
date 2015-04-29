import java.util.ArrayList;
import java.util.Stack;
 
 
 public class Agent extends Moveable{
 	
 	private Cell currentCell;
 	private Cell nextCell;
 	private Stack<Cell> visitedCells;
 	private Cell exit;
 	
	private Stack<Cell> path;
	private int cellCost = 1;
	private AStarCell entrance;
 	
 	public Agent(Cell entrance, Cell exit)
 	{
 		currentCell = entrance;
 		visitedCells = new Stack<Cell>();
 		this.exit = exit;

		path = new Stack<Cell>();
		
		this.entrance = new AStarCell(entrance, null, 0, 0);
 		//System.out.print("exit - ");
 		//exit.printCoords1();
 	}
 	
 	@Override
 	public void move() {
 		// TODO Auto-generated method stub
 		currentCell.moveCreatureOutOfCell();
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
	public void calculateNextMove() 
	{
		//To detect if the agent will move to a cell with a monster on it, check if the nextCell has a 
		//monster in it
		nextCell = path.pop();
		//if(nextCell.hasMonster())
		// calculatePathThroughMaze()
		
		//if(nextCell.stillHasMonster()
			//moveBackOneCell ----- To avoid the monster
		
	}
	
	
	//TODO: Take monsters into consideration while calulating path through maze
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
				
				tempCell = tempCell.getParentCell();
				}
				path.push(entrance.getCell());
			}
		}
	}
	
	private int calculateCellDistance(Cell c)
	{
		int pathCost = cellCost;
		int heuristic = manhattanDistance(c);
		
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
 
 }