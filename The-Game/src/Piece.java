
public class Piece 
{
	//DONE
	//BOLEAN POSITON
	private boolean teamRed;
	private Position position;
	private boolean master;
	private int ID;
	private boolean dead = false;

	public Piece(boolean teamRed,Position position, boolean master,int ID) 
	{
		this.teamRed = teamRed;
		this.position = position;
		this.master = master;
		this.ID = ID;
	}
	
	public boolean getDead() {
		return dead;
	}
	
	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
	
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	public int getID()
	{
		return ID;
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
