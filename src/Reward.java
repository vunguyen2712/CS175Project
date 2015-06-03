/*
 * CS 175 Group 1
 * 
 * Reward.java is the class which keeps track of the rewards in the maze. 
 * Each reward houses the Cell that its in, and the integer value which symbolizes how much
 * 		the reward is worth (its score)
 * 
 */



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
	
	
	/*
	 * .equals() checks to see if the reward being checked is the same cell as the object its being compared
	 *	with. I.E. if we compare a reward to an AStarCell, if they represent the same cell, then they are
	 *	considered equal
	*/
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
