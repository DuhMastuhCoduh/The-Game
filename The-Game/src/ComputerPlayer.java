import java.util.Random;





public class ComputerPlayer extends Player 
{
	
	public ComputerPlayer(boolean teamRed)
	{
		super(teamRed);
	}
	
	public int cardChoose()
	{
		Random rn = new Random();
		int randomNum = rn.nextInt(2) + 1;
		return randomNum;
	}
	
	public Position move()
	{
		
	}
}
