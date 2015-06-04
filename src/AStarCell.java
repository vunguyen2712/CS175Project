/*
 * CS 175 Group 1
 * 
 * AStarCell is used in the Agent class both during the AStarSearch to find the next goal, as well as during
 * 		monster avoidance to allow the agent to retrace its steps while avoiding the monster
 * 
 * What AStarCell comprises is the cell that it represenets, the "parent cell" (The cell that it was reached 
 * 		from via the AStarPath) the heuristic (this represents both the heuristic applied to this cell 
 * 		as well as the cost to traverse to this cell), and the path cost
 */


public class AStarCell {

	private Cell cell;
	private AStarCell parentCell;
	//heuristic = cost to move to that cell  (manhattan distance to the exit from that cell)
	private int heuristic;
	//cost = cost to move to that cell along its path so far
	private int cost;
	
	public AStarCell(Cell c, AStarCell parentCell, int heuristic, int cost)
	{
		cell = c;
		this.parentCell = parentCell;
		this.heuristic = heuristic;
		this.cost = cost;
	}
	
	public Cell getCell()
	{
		return cell;
	}
	public void setCell(Cell c)
	{
		cell = c;
	}
	
	public AStarCell getParentCell()
	{
		return parentCell;
	}
	
	public int getHeuristic()
	{
		return heuristic;
	}
	
	public int getCost()
	{
		return cost;
	}
	
	
	//.equals() checks the coordinates of the object the AStarCell is being compared to
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof AStarCell)
		{
			return AStarEquals((AStarCell) o);
		}
		else if(o instanceof Cell)
		{
			return cellEquals((Cell) o);
		}
		else
			return false;
	}

	
	public boolean cellEquals(Cell c)
	{
		//System.out.println("In Cell Equals");
		return cell.equals(c);
	}
	
	public boolean AStarEquals(AStarCell c)
	{
		//System.out.println("in astar equals");
		return cell.equals(c.getCell());
	}
}