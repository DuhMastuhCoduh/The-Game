import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("unused")
public class Onitama implements MouseListener
{

	//================================================================================
	//	VARIABLES
	//================================================================================
	private JFrame frame;
	private JPanel panel;

	private Player redPlayer,bluePlayer;

	private Board board;

	private ArrayList<ArrayList<Position>> legalMoves;

	private int mouseX,mouseY;

	private Position currentPos = new Position(0,0);
	private Piece currentPiece = null;
	private ArrayList<Position> moves;

	private boolean redTurn;
	
	private int selectedCard;

	private boolean pressed = false;
	private boolean cardPressed = false;
	//================================================================================
	//	CONSTRUCTOR - starts the game
	//================================================================================

	public Onitama() 
	{

		redPlayer = new Player(true);
		bluePlayer = new Player(false);

		setCards();
		setFirstTurn();
		setLegalMoves();

		//debug

		System.out.println("\nredTurn: " + redTurn);

		System.out.println("\nlegal moves: ");
		for(int i = 0; i < legalMoves.size();i++) {
			for(int c = 0; c < legalMoves.get(i).size();c++) {
				System.out.println("disciple " + i + ": " + legalMoves.get(i).get(c));

			}
		}
		System.out.println();

		//////////////////////

		frame = new JFrame("Onitama");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new OnitamaPanel();
		panel.addMouseListener(this);

		frame.setContentPane(panel);

		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	//================================================================================
	//	STARTUP METHODS - sets up the game
	//================================================================================

	private void setFirstTurn() 
	{
		if(board.getCard().redCard()) 
		{
			redTurn = true;
		} else 
		{
			redTurn = false;
		}
	}

	private void setCards() 
	{
		//array of all cards
		ArrayList<Card> allCards = new ArrayList<Card>();
		for(int i = 1; i < 17; i++) 
		{
			allCards.add(new Card(i));
		}
		//select 5 random cards
		ArrayList<Card> cards = new ArrayList<Card>(5);
		for(int i = 0; i < 5; i++) 
		{
			int randIndex = (int)(Math.random()*(allCards.size())+1);
			cards.add(allCards.get(randIndex-1));
			allCards.remove(randIndex-1);
		}
		//distribute cards
		redPlayer.setCard1(cards.get(0));
		redPlayer.setCard2(cards.get(1));
		bluePlayer.setCard1(cards.get(2));
		bluePlayer.setCard2(cards.get(3));

		System.out.println("redPlayer: " + redPlayer);
		System.out.println("bluePlayer: " + bluePlayer);
		System.out.println();

		board = new Board(cards.get(4));

		System.out.println("board card: " + board.getCard());
	}

	//================================================================================
	//	GAMEPLAY - handles gameplay mechanics and updates
	//================================================================================

	private void setLegalMoves() 
	{
		legalMoves = new ArrayList<ArrayList<Position>>();
		ArrayList<Position> cardMoves = new ArrayList<Position>();

		if(redTurn) 
		{	//sets legal moves for a red turn

			cardMoves = redPlayer.getCard1().getLegalMoves();

			/*goes through each disciple and creates a new position for
			 * each of the first card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < redPlayer.getDisciples().size();i++) 
			{	
				Piece temp = redPlayer.getDisciples().get(i);
				legalMoves.add(new ArrayList<Position>());
				for(int c = 0; c < cardMoves.size(); c++) 
				{
					int tempRow = temp.getPosition().getRow()+cardMoves.get(c).getRow();
					int tempCol = temp.getPosition().getCol()+cardMoves.get(c).getCol();

					if(tempRow > -1 && tempRow < 5)
					{
						if(tempCol > -1 && tempCol < 5) 
						{
							Position tempPos = new Position(tempRow,tempCol);
							if(redPlayer.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			cardMoves = redPlayer.getCard2().getLegalMoves();

			/* goes through each disciple and creates a new position for
			 * each of the second card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < redPlayer.getDisciples().size();i++) 
			{
				Piece temp = redPlayer.getDisciples().get(i);

				for(int c = 0; c < cardMoves.size(); c++) 
				{
					int tempRow = temp.getPosition().getRow()+cardMoves.get(c).getRow();
					int tempCol = temp.getPosition().getCol()+cardMoves.get(c).getCol();

					if(tempRow > -1 && tempRow < 5) 
					{
						if(tempCol > -1 && tempCol < 5) 
						{
							Position tempPos = new Position(tempRow,tempCol);
							if(redPlayer.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			//master
			legalMoves.add(new ArrayList<Position>());
			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = redPlayer.getMaster().getPosition().getRow()+cardMoves.get(i).getRow();
				int tempCol = redPlayer.getMaster().getPosition().getCol()+cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(redPlayer.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}

			cardMoves = redPlayer.getCard1().getLegalMoves();

			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = redPlayer.getMaster().getPosition().getRow()+cardMoves.get(i).getRow();
				int tempCol = redPlayer.getMaster().getPosition().getCol()+cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(redPlayer.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}
		} 
		else 
		{	//sets legal moves for blue turn
			cardMoves = bluePlayer.getCard1().getLegalMoves();

			/*goes through each disciple and creates a new position for
			 * each of the first card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < bluePlayer.getDisciples().size();i++) 
			{
				Piece temp = bluePlayer.getDisciples().get(i);
				legalMoves.add(new ArrayList<Position>());
				for(int c = 0; c < cardMoves.size(); c++) 
				{
					int tempRow = temp.getPosition().getRow()+-1*cardMoves.get(c).getRow();
					int tempCol = temp.getPosition().getCol()+-1*cardMoves.get(c).getCol();

					if(tempRow > -1 && tempRow < 5) 
					{
						if(tempCol > -1 && tempCol < 5) 
						{
							Position tempPos = new Position(tempRow,tempCol);
							if(bluePlayer.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			cardMoves = bluePlayer.getCard2().getLegalMoves();

			/*goes through each disciple and creates a new position for
			 * each of the second card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < bluePlayer.getDisciples().size();i++) 
			{
				Piece temp = bluePlayer.getDisciples().get(i);
				//legalMoves.add(new ArrayList<Position>());
				for(int c = 0; c < cardMoves.size(); c++) 
				{
					int tempRow = temp.getPosition().getRow()+-1*cardMoves.get(c).getRow();
					int tempCol = temp.getPosition().getCol()+-1*cardMoves.get(c).getCol();

					if(tempRow > -1 && tempRow < 5) 
					{
						if(tempCol > -1 && tempCol < 5) 
						{
							Position tempPos = new Position(tempRow,tempCol);
							if(bluePlayer.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}
			//master
			legalMoves.add(new ArrayList<Position>());

			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = bluePlayer.getMaster().getPosition().getRow()+-1*cardMoves.get(i).getRow();
				int tempCol = bluePlayer.getMaster().getPosition().getCol()+-1*cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(bluePlayer.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}

			cardMoves = bluePlayer.getCard1().getLegalMoves();

			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = bluePlayer.getMaster().getPosition().getRow()+-1*cardMoves.get(i).getRow();
				int tempCol = bluePlayer.getMaster().getPosition().getCol()+-1*cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(bluePlayer.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}

		}
	}

	//TODO
	private boolean checkLegalMove() {
		//		int row = afterPos.getRow()-currentPos.getRow();
		//		int col = afterPos.getCol()-currentPos.getCol();
		//		for(Position movesPos: moves) {
		//			if((movesPos.getRow() == row) && (movesPos.getCol() == col)) {
		//				System.out.println("Eureka");
		//				return true;
		//			}
		//		}
		if(redTurn)
		{
			if(redPlayer.getPiece(currentPos)!=null)
				return false;
		} else
		{
			if(bluePlayer.getPiece(currentPos)!=null)
				return false;
		}
		for(int i = 0; i < legalMoves.get(currentPiece.getID()).size();i++) {
			if(currentPos.equals(legalMoves.get(currentPiece.getID()).get(i)))
				return true;
		}

		return false;
	}
	//TODO
	private void move() 
	{
		
		currentPiece.move(currentPos);
		
		Card holdCard = board.getCard();
		
		if(redTurn) 
		{
			if(selectedCard == 1)
			{
				board.setCard(redPlayer.getCard1());
				redPlayer.setCard1(holdCard);
				
			}
			if(selectedCard == 2)
			{
				board.setCard(redPlayer.getCard2());
				redPlayer.setCard2(holdCard);
			}
		} else
		{
			if(selectedCard == 1)
			{
				board.setCard(bluePlayer.getCard1());
				bluePlayer.setCard1(holdCard);
				
			}
			if(selectedCard == 2)
			{
				board.setCard(bluePlayer.getCard2());
				bluePlayer.setCard2(holdCard);
			}
		}
		
		
		
		if(redTurn)
		{
			//if(bluePlayer.getPiece(currentPos)!=null)
				//bluePlayer.capturePiece(currentPos);
		} else
		{
			//if(redPlayer.getPiece(currentPos)!=null)
				//redPlayer.capturePiece(currentPos);
		}

		redTurn = !redTurn;
		setLegalMoves();
	}
	//TODO
	private boolean checkWin() 
	{
		return false;
	}

	//================================================================================
	//	GRAPHICS - handles game display
	//================================================================================

	//TODO
	@SuppressWarnings("serial")
	private class OnitamaPanel extends JPanel
	{

		int boardX = 207,boardY = 203;
		int tileHeight = 77,tileWidth = 77;

		public OnitamaPanel() {

		}

		private void drawPieces(Graphics g) {
			redPlayer.drawPieces(g);
			bluePlayer.drawPieces(g);
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			//draws board
			try {
				final BufferedImage oniBoard = ImageIO.read(new File("resources\\board.png"));
				g.drawImage(oniBoard, 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//draws board bounds
			g.setColor(Color.green);
			g.drawRect(boardX, boardY, tileWidth*5, tileHeight*5);
			for(int i = 0; i < 5;i++) {
				for(int c = 0; c<5;c++) {
					g.drawRect(boardX+c*tileWidth, boardY+i*tileHeight, tileWidth, tileHeight);
				}
			}

			//draws all player pieces
			drawPieces(g);

			//draw cards
			//blue
			g.setColor(Color.blue);	
			g.drawRect(207,80,191,100);  //card 1
			g.drawRect(401,80,191,100);  //card 2
			//red
			g.setColor(Color.red);
			g.drawRect(207,611,191,100);  //card 1
			g.drawRect(401,611,191,100);  //card 2
			//board
			g.setColor(Color.white);
			if(redTurn)
				g.drawRect(597, 345, 191, 100);
			else
				g.drawRect(3,345,191,100);

			//will be accessed by ImageIO.read(new File("resources\\" + cardName + ".png"));)

		}
		//image size constraints
		//pieces:	77x77
		//board:	frame size
		//cards:	191x100

		//square board places
		//top left:		(207,203)
		//bottom left:	(207,588)
		//top right:	(592,203)
		//bottom right:	(592,588)
		//squares are 77x77
		//needs to be a buffer zone of a couple pixels:i disagree

		//card places	width(191) height(100)
		//red card 1:	x(207) y(80) 
		//red card 2:	x(401) y(80)
		//blue card 1:	x(207) y(611)
		//blue card 2:	x(401) y(611)
	}

	//================================================================================
	//	MOUSE EVENTS - handles user interaction with the game
	//================================================================================


	/* PLAN FOR ACTIONS:
	 * 1.	player chooses a card (mouseReleased)
	 * 2.	players clicks and drags a piece (mousePressed)										[TO BE IMPLEMENTED]
	 * 3.	onto legal move and places onto legal move (mouseReleased)							[TO BE IMPLEMENTED]
	 * 4.	checks if legal move, if so, cards are swapped, piece is moves, next turn starts	[TO BE IMPLEMENTED]
	 */

	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { panel.repaint(); }
	public void mouseExited(MouseEvent e) { }

	//TODO
	public void mousePressed(MouseEvent e) 
	{

		mouseX = e.getX();
		mouseY = e.getY();

		if(mouseX>207&&mouseX<592)  //within x bounds of board
		{
			if(mouseY>203&&mouseY<588)  //within y bounds of board
			{

				pressed = true;

				for(int i = 0; i < 5; i++) 
				{
					if(mouseX>207+77*(i))
						currentPos.setCol(i);
					if(mouseY>203+77*(i))
						currentPos.setRow(i);

					if(redTurn) {
						currentPiece = redPlayer.getPiece(currentPos);
					} else {
						currentPiece = bluePlayer.getPiece(currentPos);
					}
				}
				System.out.println("a");
				System.out.println(currentPos);
				System.out.println(currentPiece);

			}
		}
	}

	//TODO
	public void mouseReleased(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if(mouseX>207&&mouseX<592)  //if click is within x bounds of board
		{
			if(!pressed)  //player did not select a piece
			{
				if(mouseY>611&&mouseY<711&&redTurn)  //within y bounds of red cards and red turn
				{

					if(mouseX<398)			//card 1
					{
						System.out.println(redPlayer.getCard1());
						selectedCard = 1;
						cardPressed = true;
					} else if(mouseX>401)	//card 2
					{
						System.out.println(redPlayer.getCard2());
						selectedCard = 2;
						cardPressed = true;
					}
					//moves = redCard.getLegalMoves();
				} else if(mouseY>80&&mouseY<180&&!redTurn)  //within y bounds of blue cards and not red turn
				{

					if(mouseX<398)			//card 1
					{
						System.out.println(bluePlayer.getCard1());
						selectedCard = 1;
						cardPressed = true;
					} else if(mouseX>401)	//card 2
					{
						System.out.println(bluePlayer.getCard2());
						selectedCard = 2;
						cardPressed = true;
					}
					//moves = blueCard.getLegalMoves();
				}
			} else //player has selected a piece
			{		//[IMPLEMENT CHECKING WHICH CARD IS SELECTED]
				if(mouseY>203&&mouseY<588)  //within y bounds of board
				{

					pressed = false;

					for(int i = 0; i < 5; i++) 
					{
						if(mouseX>207+77*(i))
							currentPos.setCol(i);
						if(mouseY>203+77*(i))
							currentPos.setRow(i);

						if(checkLegalMove() && cardPressed)
							move();

					}
					
					cardPressed = false;

					System.out.println("b");
					System.out.println(currentPos);
					System.out.println(currentPiece);

				} else  //not released within y bounds of board
				{
					pressed = false;
					currentPiece = null;
					currentPos = new Position(0,0);
					
					System.out.println("c");
					System.out.println(currentPos);
					System.out.println(currentPiece);
					System.out.println();
				}

			}
		} else
		{
			pressed = false;
			currentPiece = null;
			currentPos = new Position(0,0);

			System.out.println("c");
			System.out.println(currentPos);
			System.out.println(currentPiece);
			System.out.println();
		}



		panel.repaint();
	}

	//================================================================================
	//	MAIN
	//================================================================================


	public static void main(String[] args) {

		new Onitama();

	}

}
