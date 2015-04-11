import java.util.ArrayList;



public class Cell {

	private boolean occupied;
	private Moveable creatureInCell;
	private ArrayList<Cell> neighbors;
	private int[] coordinates;
	private int importance; //1,2,3 - symbolizes if entrance, exit, or reward is in this cell 
	
	public Cell(ArrayList<Cell> neighbors, int x, int y, int importance)
	{
		occupied = false;
		this.neighbors = neighbors;
		coordinates = new int[2];
		coordinates[0] = x;
		coordinates[1] = y;
		this.importance = importance;
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
