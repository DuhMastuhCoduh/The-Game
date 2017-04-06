
public class Position 
{
	
	private int row, col;
	private int xCoord, yCoord;
	
	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public Position(int row, int col) 
	{
		this.row = row;
		this.col = col;
		
		xCoord = 207 + 77*col;
		yCoord = 203 + 77*row;
		
	}
	
	public boolean equals(Position pos) {
		if(row == pos.getRow()&&col==pos.getCol())
			return true;
		return false;
	}
	
	public String toString() 
	{
		return "(" + row + "," + col + ")";
	}

	public int getRow() 
	{
		return row;
	}

	public void setRow(int row) 
	{
		this.row = row;
	}

	public int getCol() 
	{
		return col;
	}

	public void setCol(int col) 
	{
		this.col = col;
	}

	
}
