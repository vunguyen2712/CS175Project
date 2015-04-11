import java.util.ArrayList;



public class Cell {

	private boolean occupied;
	private Moveable creatureInCell;
	private ArrayList<Cell> neighbors;
	private int[] coordinates;
	private int importance; //1,2,3 - symbolizes if entrance, exit, or reward is in this cell 
	
	public Cell(int x, int y)
	{
		occupied = false;
		coordinates = new int[2];
		coordinates[0] = x;
		coordinates[1] = y;
	}
	
	public void setNeighbors(ArrayList<Cell> neighbors)
	{
		this.neighbors = neighbors;
	}
	
	public void setEntrance(Agent agent)
	{
		occupied = true;
		creatureInCell = agent;
		setImportance(1);
	}
	
	public void setExit()
	{
		setImportance(2);
	}
	
	private void setImportance(int importance)
	{
		this.importance = importance;
	}
	
	public void moveCreatureOutOfCell()
	{
		occupied = false;
	}
	
	public void moveCreatureIntoCell(Moveable creature)
	{
		occupied = true;
		creatureInCell = creature;
	}
	public boolean isOccupied()
	{
		return occupied;
	}
	
	public Moveable getCreature()
	{
		return creatureInCell;
	}
	
	public ArrayList<Cell> getNeighbors()
	{
		return neighbors;
	}
	
	public int[] getCoordinates()
	{
		return coordinates;
	}
	
	public int isImportant()
	{
		return importance;
	}
	
}
