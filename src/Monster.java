
public class Monster extends Moveable{

	private int currentX;
	private int currentY;
	
	public Monster(int startingX, int startingY)
	{
		currentX = startingX;
		currentY = startingY;
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Implement This");
	}

	@Override
	public int calculateNextMove() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Implement This");
	}
	
	public int getX()
	{
		return currentX;
	}
	public int getY()
	{
		return currentY;
	}
	
}
