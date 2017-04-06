import java.util.ArrayList;

public class Card 
{
	private String cardName;
	private int cardID;
	private boolean redCard;
	private ArrayList<Position> legalMoves = new ArrayList<Position>();
	
	public Card(int cardID) 
	{
		
		this.cardID = cardID;
		
		if(cardID == 1) 
		{
			cardName = "BOAR";
			redCard = true;
		} else if(cardID == 2) 
		{
			cardName = "FROG";
			redCard = true;
		} else if(cardID == 3) 
		{
			cardName = "ROOSTER";
			redCard = true;
		} else if(cardID == 4) 
		{
			cardName = "CRANE";
			redCard = false;
		} else if(cardID == 5) 
		{
			cardName = "MONKEY";
			redCard = false;
		} else if(cardID == 6) 
		{
			cardName = "DRAGON";
			redCard = true;
		} else if(cardID == 7) 
		{
			cardName = "GOOSE";
			redCard = false;
		} else if(cardID == 8) 
		{
			cardName = "TIGER";
			redCard = false;
		} else if(cardID == 9) 
		{
			cardName = "MANTIS";
			redCard = true;
		} else if(cardID == 10) 
		{
			cardName = "ELEPHANT";
			redCard = true;
		} else if(cardID == 11) 
		{
			cardName = "CRAB";
			redCard = false;
		} else if(cardID == 12) 
		{
			cardName = "RABBIT";
			redCard = false;
		} else if(cardID == 13) 
		{
			cardName = "EEL";
			redCard = false;
		} else if(cardID == 14) 
		{
			cardName = "COBRA";
			redCard = true;
		} else if(cardID == 15) 
		{
			cardName = "HORSE";
			redCard = true;
		} else if(cardID == 16) 
		{
			cardName = "OX";
			redCard = false;
		}
		setLegalMoves();
	}
	
	private void setLegalMoves() 
	{
		//based on a piece at position (0,0)
		if(cardName.equals("BOAR")) 
		{
			legalMoves.add(new Position(-1,0));
			legalMoves.add(new Position(0,1));
			legalMoves.add(new Position(0,-1));
		} else if(cardName.equals("FROG")) 
		{
			legalMoves.add(new Position(1,1));
			legalMoves.add(new Position(-1,-1));
			legalMoves.add(new Position(0,-2));
		} else if(cardName.equals("ROOSTER"))
		{
			legalMoves.add(new Position(0,-1));
			legalMoves.add(new Position(1,-1));
			legalMoves.add(new Position(0,1));
			legalMoves.add(new Position(-1,1));
		} else if(cardName.equals("CRANE"))
		{
			legalMoves.add(new Position(1,-1));
			legalMoves.add(new Position(1,1));
			legalMoves.add(new Position(-1,0));
			
		} else if(cardName.equals("MONKEY"))
		{
			legalMoves.add(new Position(-1,-1));
			legalMoves.add(new Position(1,-1));
			legalMoves.add(new Position(-1,1));
			legalMoves.add(new Position(1,1));
		} else if(cardName.equals("DRAGON")) 
		{
			legalMoves.add(new Position(-1,-2));
			legalMoves.add(new Position(1,-1));
			legalMoves.add(new Position(-1,2));
			legalMoves.add(new Position(1,1));
		} else if(cardName.equals("GOOSE")) 
		{
			legalMoves.add(new Position(-1,-1));
			legalMoves.add(new Position(0,-1));
			legalMoves.add(new Position(0,1));
			legalMoves.add(new Position(1,1));
		} else if(cardName.equals("TIGER")) 
		{
			legalMoves.add(new Position(-2,0));
			legalMoves.add(new Position(1,0));
		} else if(cardName.equals("MANTIS")) 
		{
			legalMoves.add(new Position(-1,-1));
			legalMoves.add(new Position(1,0));
			legalMoves.add(new Position(-1,1));
		} else if(cardName.equals("ELEPHANT")) 
		{
			legalMoves.add(new Position(0,-1));
			legalMoves.add(new Position(-1,-1));
			legalMoves.add(new Position(0,1));
			legalMoves.add(new Position(-1,1));
		} else if(cardName.equals("CRAB")) 
		{
			legalMoves.add(new Position(0,-2));
			legalMoves.add(new Position(-1,0));
			legalMoves.add(new Position(0,2));
		} else if(cardName.equals("RABBIT")) 
		{
			legalMoves.add(new Position(1,-1));
			legalMoves.add(new Position(-1,1));
			legalMoves.add(new Position(0,2));
		} else if(cardName.equals("EEL")) 
		{
			legalMoves.add(new Position(-1,-1));
			legalMoves.add(new Position(1,-1));
			legalMoves.add(new Position(0,1));
		} else if(cardName.equals("COBRA")) 
		{
			legalMoves.add(new Position(0,-1));
			legalMoves.add(new Position(-1,1));
			legalMoves.add(new Position(1,1));
		} else if(cardName.equals("HORSE")) 
		{
			legalMoves.add(new Position(0,-1));
			legalMoves.add(new Position(-1,0));
			legalMoves.add(new Position(1,0));
		} else if(cardName.equals("OX")) 
		{
			legalMoves.add(new Position(-1,0));
			legalMoves.add(new Position(0,1));
			legalMoves.add(new Position(1,0));
		}
	}
	
	public String toString() {
		return cardName + "- redCard: " + redCard + ", cardID: " + cardID + ", #legal: " + legalMoves.size();
	}
	
	public ArrayList<Position> getLegalMoves() {
		return legalMoves;
	}

	public String getCardName() {
		return cardName;
	}
	
	public boolean redCard() {
		return redCard;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

}
