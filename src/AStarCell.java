
public class AStarCell {

	private Cell cell;
	private AStarCell parentCell;
	//heuristic = cost to move to that cell  manhattan distance to the exit from that cell
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