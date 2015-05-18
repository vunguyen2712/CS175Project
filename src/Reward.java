
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
}
