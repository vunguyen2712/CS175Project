
public class Reward {

	private Cell cell;
	private int value;
	
	public Reward(Cell cell, int value)
	{
		this.cell = cell;
		this.value = value;
	}
	
	public Cell getCell(){
		return cell;
	}
	
	public int getValue()
	{
		return value;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof AStarCell)
		{
			if(cell.equals(((AStarCell) o).getCell()))
			{
				return true;
			}
			else
				return false;
		}
		else if(o instanceof Cell)
		{
			if (cell.equals(o))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (o instanceof Reward)
		{
			if (cell.equals(((Reward) o).getCell()))
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
