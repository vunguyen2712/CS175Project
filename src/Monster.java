
/*
 * CS 175 Group 1
 * 
 * Monster.java symbolizes the monsters roaming around the maze
 * 
 * The monsters roam the maze randomly, by calculating where to move next each time within the "game loop"
 * 
 * 
 */

import java.util.Random;

public class Monster extends Moveable{

	private Cell currentCell;
	private Cell nextCell;
	private Cell lastCell;
	
	public Monster(Cell startingCell)
	{
		currentCell = startingCell;
		currentCell.moveCreatureIntoCell(this);
	}
	
	
	//Move takes the nextCell generated in calculateNextMove(), moves the monster into that cell
	// Removes it from the cell that it currently is in, and saves that currentCell as the lastCell
	@Override
	public void move() {
		//move out of currentCell
		//move into nextCell
		lastCell = currentCell;
		currentCell.moveCreatureOutOfCell(this);
		nextCell.moveCreatureIntoCell(this);
		currentCell = nextCell;
	}

	//To find out what cell to move to next, we look at all the neighbors of the current cell, and randomly
	//		pick one
	@Override
	public Cell calculateNextMove() {
		Random random = new Random();
		int newCell = random.nextInt(currentCell.getNeighbors().size());
		
		nextCell = currentCell.getSpecificNeighbor(newCell);
		return nextCell;
		//nextCell.printCoords();
	}
	
	public int getX()
	{
		return currentCell.getCoordinates()[0];
	}
	public int getY()
	{
		return currentCell.getCoordinates()[1];
	}
	public Cell getCurrentCell()
	{
		return currentCell;
	}
	public Cell getNextCell()
	{
		return nextCell;
	}
	
	public Cell getLastCell()
	{
		return lastCell;
	}
	
}
