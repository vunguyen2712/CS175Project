
public class Agent extends Moveable{

	private int currentX;
	private int currentY;
	
	public Agent(int startingX, int startingY)
	{
		currentX = startingX;
		currentY = startingY;
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		//CalculateNextMove();
		//change the X,Y values based on the result
		throw new RuntimeException("Implement This");
	}

	@Override
	public int calculateNextMove() {
		// TODO Auto-generated method stub
		//For the Agent, apply our ai algorithmn 
		throw new RuntimeException("Implement This");
	}
	
	public int getX()
	{
		throw new RuntimeException("Implement This");
	}
	
	public int getY()
	{
		throw new RuntimeException("Implement This");
	}

}
