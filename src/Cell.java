package src;

import java.util.ArrayList;



public class Cell {

	private boolean occupied;
	private Moveable creatureInCell;
	private ArrayList<Cell> neighbors;
	private int[] coordinates;
	private int importance; //1,2,3 - symbolizes if entrance, exit, or reward is in this cell 
	private boolean visited;
	private ArrayList<Cell> neighborsIVisited;
	private boolean aStarVisited;
	private Reward reward;
	private boolean hasReward;
	
	public Cell(int x, int y)
	{
		occupied = false;
		coordinates = new int[2];
		coordinates[0] = x;
		coordinates[1] = y;
		visited = false;
		neighborsIVisited = new ArrayList<Cell>();
		aStarVisited = false;
		hasReward = false;
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
	
	public void setImportance(int importance)
	{
		this.importance = importance;
	}
	
	public void moveCreatureOutOfCell(Moveable creature)
	{
		if(creature == creatureInCell)
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
	
	public void visit()
	{
		visited = true;
	}
	
	public void aStarVisit()
	{
		aStarVisited = true;
	}
	
	public boolean visited()
	{
		return visited;
	}
	
	public void visitNeighbor(Cell neighbor)
	{
		neighborsIVisited.add(neighbor);
		neighbor.visit();
	}
	
	public ArrayList<Cell> getVisitedNeighbors()
	{
		return neighborsIVisited;
	}
	
	public boolean aStarVisited()
	{
		return aStarVisited;
	}
	
	public int getNumberOfVisitedNeighbors()
	{
		int numberOfVisitedNeighbors = 0;
		
		for(int i = 0; i < neighbors.size(); i++)
		{
			if(neighbors.get(i).visited())
			{
				numberOfVisitedNeighbors++;
			}
		}
		
		return numberOfVisitedNeighbors;
	}
	
	public boolean hasWestNeighbor(){
		for(Cell n : neighbors){
			int[] neighborC = n.getCoordinates();
			if(neighborC[0] == coordinates[0]-1 && neighborC[1] == coordinates[1]){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasNorthNeighbor(){
		for(Cell n : neighbors){
			int[] neighborC = n.getCoordinates();
			if(neighborC[0] == coordinates[0] && neighborC[1] == coordinates[1]-1){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasEastNeighbor(){
		for(Cell n : neighbors){
			int[] neighborC = n.getCoordinates();
			if(neighborC[0] == coordinates[0]+1 && neighborC[1] == coordinates[1]){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasSouthNeighbor(){
		for(Cell n : neighbors){
			int[] neighborC = n.getCoordinates();
			if(neighborC[0] == coordinates[0] && neighborC[1] == coordinates[1]+1){
				return true;
			}
		}
		return false;
	}
	
	public String printCoords()
	{
		
		return "(" + coordinates[0] + "," + coordinates[1] + ")";
	}
	
	public void printCoords1()
	{
		System.out.println("(" + coordinates[0] + "," + coordinates[1] + ")");
	}
	
	
	public Cell getSpecificNeighbor(int cellNumber)
	{
		return neighbors.get(cellNumber);
	}
	
	public void setReward(Reward reward)
	{
		hasReward = true;
		this.reward = reward;
	}
	
	public boolean hasReward()
	{
		return hasReward;
	}
	
	public Reward getReward()
	{
		return reward;
	}
	
	public boolean hasMonster()
	{
		if(occupied && creatureInCell instanceof Monster)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		//System.out.println("In Cell cell equals");
		if(o instanceof Cell)
		{
		Cell c = (Cell) o;
		if(getCoordinates()[0] == c.getCoordinates()[0] && getCoordinates()[1] == c.getCoordinates()[1])
		{
			return true;
		}
		else
		{
			return false;
		}
		}
		else if(o instanceof AStarCell)
		{
			AStarCell a = (AStarCell) o;
			if(getCoordinates()[0] == a.getCell().getCoordinates()[0] && getCoordinates()[1] == 
					a.getCell().getCoordinates()[1])
			{
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
}
