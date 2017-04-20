

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player 
{
	
	private Card card1,card2;
	private boolean teamRed;

	private ArrayList<Piece> disciples = new ArrayList<Piece>();
	private Piece master;
	
	public Player(boolean teamRed) 
	{
		
		this.teamRed = teamRed;
		if(teamRed)
		{
			for(int i = 0; i < 5;i++) 
			{
				if(i != 2)
					disciples.add(new Piece(teamRed,new Position(4,i),false,i));
				else
					master = new Piece(teamRed,new Position(4,i),true,i);
			}
		} else
		{
			for(int i = 0; i < 5;i++) 
			{
				if(i != 2)
					disciples.add(new Piece(teamRed,new Position(0,i),false,i));
				else
					master = new Piece(teamRed,new Position(0,i),true,i);
			}
		}
	}
	
	public boolean isTeamRed() {
		return teamRed;
	}

	public void setTeamRed(boolean teamRed) {
		this.teamRed = teamRed;
	}
	
	public void drawPieces(Graphics g) {
		for(int i = 0; i < disciples.size();i++) {
			if(teamRed) {
				try {
					final BufferedImage piece = ImageIO.read(new File("resources\\red disciple.png"));
					g.drawImage(piece, disciples.get(i).getPosition().getxCoord(), disciples.get(i).getPosition().getyCoord(), null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					final BufferedImage piece = ImageIO.read(new File("resources\\blue disciple.png"));
					g.drawImage(piece, disciples.get(i).getPosition().getxCoord(), disciples.get(i).getPosition().getyCoord(), null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(teamRed) {
			try {
				final BufferedImage piece = ImageIO.read(new File("resources\\red master.png"));
				g.drawImage(piece, master.getPosition().getxCoord(), master.getPosition().getyCoord(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				final BufferedImage piece = ImageIO.read(new File("resources\\blue master.png"));
				g.drawImage(piece, master.getPosition().getxCoord(), master.getPosition().getyCoord(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String toString() {
		return "teamRed: " + teamRed + ", card1: " + card1 + 
				", card2: " + card2 + ", #disc: " + disciples.size() + 
				", master: " + master.getMaster();
	}
	
	public Piece getPiece(Position pos) 
	{
		for(int i = 0; i < disciples.size();i++) {
			if(disciples.get(i).getPosition().equals(pos)) {
				return disciples.get(i);
			}
		}
		if(master.getPosition().equals(pos))
			return master;
		return null;
	}

	public Card getCard1() {
		return card1;
	}
	
	public ArrayList<Piece> getDisciples() {
		return disciples;
	}
	
	public Piece getMaster() {
		return master;
	}

	public void setCard1(Card card1) {
		this.card1 = card1;
	}

	public Card getCard2() {
		return card2;
	}

	public void setCard2(Card card2) {
		this.card2 = card2;
	}
	
	

}
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.ArrayList;
//
//import javax.imageio.ImageIO;
//
//public class Player 
//{
//	
//	private Card card1,card2;
//	private boolean teamRed;
//
//	private ArrayList<Piece> disciples = new ArrayList<Piece>();
//	private Piece master;
//	
//	public Player(boolean teamRed) 
//	{
//		
//		this.teamRed = teamRed;
//		if(teamRed)
//		{
//			for(int i = 0; i < 5;i++) 
//			{
//				if(i < 2)
//					disciples.add(new Piece(teamRed,new Position(4,i),false,i));
//				else if(i>2)
//					disciples.add(new Piece(teamRed,new Position(4,i-1),false,i-1));
//				else
//					master = new Piece(teamRed,new Position(4,i),true,4);
//			}
//		} else
//		{
//			for(int i = 0; i < 5;i++) 
//			{
//				if(i < 2)
//					disciples.add(new Piece(teamRed,new Position(0,i),false,i));
//				else if(i>2)
//					disciples.add(new Piece(teamRed,new Position(0,i-1),false,i-1));
//				else
//					master = new Piece(teamRed,new Position(0,i),true,4);
//			}
//		}
//	}
//	
//	public void capturePiece(Position pos) {
//		Piece currentPiece = getPiece(pos);
//		currentPiece.setDead(true);
//		for(int i = 0; i < disciples.size();i++) {
//			if(disciples.get(i)!=currentPiece && disciples.get(i).getID()>currentPiece.getID())
//				disciples.get(i).setID(disciples.get(i).getID()-1);
//		}
//		master.setID(master.getID()-1);
//		disciples.remove(currentPiece);
//	}
//	
//	public boolean isTeamRed() {
//		return teamRed;
//	}
//
//	public void setTeamRed(boolean teamRed) {
//		this.teamRed = teamRed;
//	}
//	
//	public void drawPieces(Graphics g) {
//		for(int i = 0; i < disciples.size();i++) {
//			if(teamRed) {
//				try {
//					final BufferedImage piece = ImageIO.read(new File("resources\\red disciple.png"));
//					g.drawImage(piece, disciples.get(i).getPosition().getxCoord(), disciples.get(i).getPosition().getyCoord(), null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			} else {
//				try {
//					final BufferedImage piece = ImageIO.read(new File("resources\\blue disciple.png"));
//					g.drawImage(piece, disciples.get(i).getPosition().getxCoord(), disciples.get(i).getPosition().getyCoord(), null);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		if(teamRed) {
//			try {
//				final BufferedImage piece = ImageIO.read(new File("resources\\red master.png"));
//				g.drawImage(piece, master.getPosition().getxCoord(), master.getPosition().getyCoord(), null);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				final BufferedImage piece = ImageIO.read(new File("resources\\blue master.png"));
//				g.drawImage(piece, master.getPosition().getxCoord(), master.getPosition().getyCoord(), null);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public String toString() {
//		return "teamRed: " + teamRed + ", card1: " + card1 + 
//				", card2: " + card2 + ", #disc: " + disciples.size() + 
//				", master: " + master.getMaster();
//	}
//	
//	public Piece getPiece(Position pos) 
//	{
//		for(int i = 0; i < disciples.size();i++) {
//			if(disciples.get(i).getPosition().equals(pos)) {
//				return disciples.get(i);
//			}
//		}
//		if(master.getPosition().equals(pos))
//			return master;
//		return null;
//	}
//
//	public Card getCard1() {
//		return card1;
//	}
//	
//	public ArrayList<Piece> getDisciples() {
//		return disciples;
//	}
//	
//	public Piece getMaster() {
//		return master;
//	}
//
//	public void setCard1(Card card1) {
//		this.card1 = card1;
//	}
//
//	public Card getCard2() {
//		return card2;
//	}
//
//	public void setCard2(Card card2) {
//		this.card2 = card2;
//	}
//	
//	
//
//}