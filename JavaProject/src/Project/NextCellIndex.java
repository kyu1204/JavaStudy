package Project;

public class NextCellIndex {
	private int array_1;
	private int array_2;
	private boolean life;

	public int Array_1() {
		return array_1;
	}

	public int Array_2() {
		return array_2;
	}

	public boolean Life() {
		return life;
	}
	
	public NextCellIndex(int i,int j,boolean life)
	{
		this.array_1 = i;
		this.array_2 = j;
		this.life = life;
	}

}
