
public class Piece 
{
	//DONE
	//BOLEAN POSITON
	private boolean teamRed;
	private Position position;
	private boolean master;

	public Piece(boolean teamRed,Position position, boolean master) 
	{
		this.teamRed = teamRed;
		this.position = position;
		this.master = master;
	}
	
	public void move(Position newPos) 
	{
		position = newPos;
	}
	
	public Position getPosition() 
	{
		return position;
	}
	
	public boolean getTeamRed() 
	{
		return teamRed;
	}
	
	public boolean getMaster() 
	{
		return master;
	}

}
