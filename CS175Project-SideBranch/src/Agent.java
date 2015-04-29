import java.util.Stack;


public class Agent extends Moveable{
	
	private Cell currentCell;
	private Cell nextCell;
	private Stack<Cell> visitedCells;
	private Cell exit;
	private int path;
	
	public Agent(Cell entrance, Cell exit)
	{
		currentCell = entrance;
		visitedCells = new Stack<Cell>();
		this.exit = exit;
		path = 0;
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
		
		path++;
		if(currentCell.equals(exit))
		{
			MazeSolver.done = true;
		}
	}

	@Override
	public Cell calculateNextMove() {
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
		return nextCell;
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
