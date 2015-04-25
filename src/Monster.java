import java.util.Random;


public class Monster extends Moveable{

	private Cell currentCell;
	private Cell nextCell;
	
	public Monster(Cell startingCell)
	{
		currentCell = startingCell;
		currentCell.moveCreatureIntoCell(this);
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		//move out of currentCell
		//move into nextCell
		
		currentCell.moveCreatureOutOfCell();
		nextCell.moveCreatureIntoCell(this);
		currentCell = nextCell;
	}

	@Override
	public void calculateNextMove() {
		Random random = new Random();
		int newCell = random.nextInt(currentCell.getNeighbors().size());
		
		nextCell = currentCell.getSpecificNeighbor(newCell);
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
	
}
