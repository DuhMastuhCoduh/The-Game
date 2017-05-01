import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*	[TO BE IMPLEMENTED]	- = unimplemented;	+ = implemented
 * 	-AI/Computer player for the player to play against
 *	-welcome/instruction screen
 *	-replay option
 */

/*	CURRENT KNOWN BUGS:	- = unresolved;	+ = resolved
 * 	+when an invalid move is selected picks a seemingly random move sometimes?
 * 	+blue team legal moves on wrong pieces
 * 	+sometimes move selection doesnt work
 *  +card and red piece selected then piece released in place, if only one available move, automatically moves it
 * 	+if a piece has died for X team and X team piece land on that space, that piece cant be selected
 * 	+if a piece has died for X team, other X team pieces cannot land in that spot
 */

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

	private ArrayList<ArrayList<Position>> legalMoves = new ArrayList<ArrayList<Position>>();

	private int mouseX,mouseY;

	private Position currentPos = new Position(0,0);
	private Piece currentPiece = null;
	private ArrayList<Position> moves;

	private boolean redTurn;

	private boolean winMessageDisplayed = false;

	private String winner = "";

	private int selectedCard = 0;

	private boolean pressed = false;
	private boolean cardPressed = false;

	private boolean gameOver = false;
//================================================================================
//	CONSTRUCTORs - starts the game
//================================================================================

	public Onitama()
	{
		frame = new JFrame("Onitama");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new WelcomeScreen();
		
		frame.setContentPane(panel);
		
		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void startGame(boolean PVP) 
	{

		redPlayer = new Player(true);
		bluePlayer = new Player(false);

		setCards();
		setFirstTurn();

		System.out.println("\nredTurn: " + redTurn);
		
		panel = new OnitamaPanel();
		panel.addMouseListener(this);

		frame.setContentPane(panel);

		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		panel.repaint();

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

	private void resetLegalMoves() 
	{
		legalMoves = new ArrayList<ArrayList<Position>>();
	}

	private void setLegalMoves() 
	{
		legalMoves = new ArrayList<ArrayList<Position>>();
		ArrayList<Position> cardMoves = new ArrayList<Position>();

		if(redTurn)
		{
			if(selectedCard==1)
			{
				cardMoves = redPlayer.getCard1().getLegalMoves();
			} 
			else if(selectedCard==2)
			{
				cardMoves = redPlayer.getCard2().getLegalMoves();
			}

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
							else if(redPlayer.getPiece(tempPos).getDead())
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

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
						else if(redPlayer.getPiece(tempPos).getDead())
							legalMoves.get(i).add(tempPos);
					}
				}
			}

		}
		else
		{
			if(selectedCard==1)
			{
				cardMoves = bluePlayer.getCard1().getLegalMoves();
			} 
			else if(selectedCard==2)
			{
				cardMoves = bluePlayer.getCard2().getLegalMoves();
			}

			for(int i = 0; i < bluePlayer.getDisciples().size();i++) 
			{	
				Piece temp = bluePlayer.getDisciples().get(i);
				legalMoves.add(new ArrayList<Position>());

				for(int c = 0; c < cardMoves.size(); c++) 
				{
					int tempRow = temp.getPosition().getRow()-cardMoves.get(c).getRow();
					int tempCol = temp.getPosition().getCol()-cardMoves.get(c).getCol();

					if(tempRow > -1 && tempRow < 5)
					{
						if(tempCol > -1 && tempCol < 5) 
						{
							Position tempPos = new Position(tempRow,tempCol);
							if(bluePlayer.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
							else if(bluePlayer.getPiece(tempPos).getDead())
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			legalMoves.add(new ArrayList<Position>());

			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = bluePlayer.getMaster().getPosition().getRow()-cardMoves.get(i).getRow();
				int tempCol = bluePlayer.getMaster().getPosition().getCol()-cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(bluePlayer.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
						else if(bluePlayer.getPiece(tempPos).getDead())
							legalMoves.get(i).add(tempPos);
					}
				}
			}
		}
	}

	
	private boolean checkLegalMove() {
		//System.out.println("HEY "+legalMoves.size());
		//System.out.println("HEY YOU "+legalMoves.get(0));
		for(int i = 0; i < legalMoves.get(currentPiece.getID()).size();i++) {
			if(currentPos.equals(legalMoves.get(currentPiece.getID()).get(i)))
				return true;

		}

		return false;
	}
	
	private void move() 
	{

		currentPiece.move(currentPos);

		Card holdCard = new Card(board.getCard().getCardID());

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
			if(bluePlayer.getPiece(currentPos)!=null) {
				bluePlayer.getPiece(currentPos).setDead(true);
				//System.out.println("GOOOD JOB");
			}
		} else
		{
			if(redPlayer.getPiece(currentPos)!=null) {
				redPlayer.getPiece(currentPos).setDead(true);
				//System.out.println("GOOOD JOB");
			}
		}
		cardPressed = false;
		currentPiece = null;
		redTurn = !redTurn;
		selectedCard = 0;
		resetLegalMoves();
		checkWin();
		panel.repaint();
	}
	
	private void checkWin() 
	{
		if(bluePlayer.getMaster().getDead())
		{
			winner = "Red";
			gameOver = true;
		} else if(bluePlayer.getMaster().getPosition().equals(new Position(4,2)))
		{
			winner = "Blue";
			gameOver = true;
		}
		if(redPlayer.getMaster().getDead())
		{
			winner = "Blue";
			gameOver = true;
		} else if(redPlayer.getMaster().getPosition().equals(new Position(0,2)))
		{
			winner = "Red";
			gameOver = true;
		}
		//gameOver = false;
	}

//================================================================================
//	GRAPHICS - handles game display
//================================================================================
	//TODO
	private class WelcomeScreen extends JPanel implements ActionListener
	{
		
		private JButton playerVComputer, playerVPlayer;
		private JButton howToPlay;
		
		public WelcomeScreen()
		{
			
			this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
			
			
			playerVComputer = new JButton("Player vs. Computer");
			playerVComputer.setActionCommand("pvc");
			playerVComputer.setAlignmentX(JButton.CENTER_ALIGNMENT);
			playerVComputer.addActionListener(this);
			this.add(playerVComputer);
			
			playerVPlayer = new JButton("Player vs. Player");
			playerVPlayer.setActionCommand("pvp");
			playerVPlayer.setAlignmentX(JButton.CENTER_ALIGNMENT);
			playerVPlayer.addActionListener(this);
			this.add(playerVPlayer);
			
			howToPlay = new JButton("How to play");
			howToPlay.setActionCommand("htp");
			howToPlay.setAlignmentX(JButton.CENTER_ALIGNMENT);
			howToPlay.addActionListener(this);
			this.add(howToPlay);
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(arg0.getActionCommand().equals("pvc")) 
			{
				System.out.println("[TODO]");
				//start game against computer player
			}
			else if(arg0.getActionCommand().equals("pvp"))
			{
				startGame(true);
			}
			else if(arg0.getActionCommand().equals("htp"))
			{
				//System.out.println("[TODO]");
				//bring player to screen that tells them how to play
			}
		}
		
	}

	//TODO
	@SuppressWarnings("serial")
	private class OnitamaPanel extends JPanel
	{

		int boardX = 207,boardY = 203;
		int tileHeight = 77,tileWidth = 77;

		public OnitamaPanel() 
		{

		}

		private void drawPieces(Graphics g) 
		{
			redPlayer.drawPieces(g);
			bluePlayer.drawPieces(g);
		}

		private void drawLegalMoves(Graphics g) 
		{
			g.setColor(Color.orange);
			if(currentPiece!=null) 
			{
				for(int i = 0; i < legalMoves.get(currentPiece.getID()).size();i++) 
				{
					Position temp = legalMoves.get(currentPiece.getID()).get(i);
					g.fillRect(temp.getXCoord(),temp.getYCoord(),77,77);
				}
			}
		}

		private void drawRotateImage(int degrees, BufferedImage image, int drawLocationX,int drawLocationY,Graphics g)
		{

			double rotationRequired = Math.toRadians (degrees);
			double locationX = image.getWidth() / 2;
			double locationY = image.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

			// Drawing the rotated image at the required drawing locations
			g.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);

			//http://stackoverflow.com/questions/8639567/java-rotating-images
		}

		public void paintComponent(Graphics g) 
		{

			super.paintComponent(g);

			//draws board
			try {
				final BufferedImage oniBoard = ImageIO.read(new File("src\\resources\\board.png"));
				g.drawImage(oniBoard, 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//g.setColor(Color.white);
			//g.fillRect(currentPos.getXCoord(), currentPos.getYCoord(), 77, 77);

			if(selectedCard != 0)
				drawLegalMoves(g);

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
			drawRotateImage(180,bluePlayer.getCard1().getImage(),207,80,g);
			g.drawRect(207,80,191,100);  //card 1
			drawRotateImage(180,bluePlayer.getCard2().getImage(),401,80,g);
			g.drawRect(401,80,191,100);  //card 2
			//red
			g.setColor(Color.red);
			g.drawImage(redPlayer.getCard1().getImage(), 207, 611, null);
			g.drawRect(207,611,191,100);  //card 1
			g.drawImage(redPlayer.getCard2().getImage(), 401, 611, null);
			g.drawRect(401,611,191,100);  //card 2
			//board
			g.setColor(Color.white);
			if(redTurn)
			{
				g.drawImage(board.getCard().getImage(),597, 345, null);
				g.drawRect(597, 345, 191, 100);
			}
			else
			{
				drawRotateImage(180,board.getCard().getImage(),3,345,g);
				g.drawRect(3,345,191,100);
			}

			if(gameOver) 
			{
				if(!winMessageDisplayed) 
				{
					winMessageDisplayed = true;
					JOptionPane.showMessageDialog(frame,winner);
					//panel.repaint();
				}
				//DRAW IMAGE ON THE BOARD THAT SAYS WHICH TEAM WINS
				//GIVE PLAYER OPTION TO PLAY AGAIN



			}

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

	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }

	//TODO
	public void mousePressed(MouseEvent e) 
	{

		mouseX = e.getX();
		mouseY = e.getY();

		if(!gameOver)
		{
			if(mouseX>207&&mouseX<592)  //within x bounds of board
			{
				if(mouseY>203&&mouseY<588)  //within y bounds of board
				{

					//pressed = true;

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
					if(currentPiece != null)
						if(currentPiece.getDead()) {
							//System.out.println("DEAD: "+currentPiece.getDead());
							currentPiece = null;
						}

					if(currentPiece!=null)
						pressed = true;

//					System.out.println("a");
//					System.out.println(currentPos);
//					if(currentPiece!=null)
//						System.out.println(currentPiece.printPiece());
//					else
//						System.out.println(currentPiece);

				}
			}
			panel.repaint();
		}
	}

	//TODO
	public void mouseReleased(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();

		if(!gameOver)
		{
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
							setLegalMoves();
						} else if(mouseX>401)	//card 2
						{
							System.out.println(redPlayer.getCard2());
							selectedCard = 2;
							cardPressed = true;
							setLegalMoves();
						}
					} else if(mouseY>80&&mouseY<180&&!redTurn)  //within y bounds of blue cards and not red turn
					{

						if(mouseX<398)			//card 1
						{
							System.out.println(bluePlayer.getCard1());
							selectedCard = 1;
							cardPressed = true;
							setLegalMoves();
						} else if(mouseX>401)	//card 2
						{
							System.out.println(bluePlayer.getCard2());
							selectedCard = 2;
							cardPressed = true;
							setLegalMoves();
						}
					}
				} else //player has selected a piece
				{
					if(mouseY>203&&mouseY<588)  //within y bounds of board
					{

						pressed = false;

						for(int i = 0; i < 5; i++) 
						{
							if(mouseX>207+77*(i))
								currentPos.setCol(i);
							if(mouseY>203+77*(i))
								currentPos.setRow(i);

						}

						if(cardPressed)
							if(checkLegalMove())
								move();

//						System.out.println("b");
//						System.out.println(currentPos);
//						if(currentPiece!=null)
//							System.out.println(currentPiece.printPiece());
//						else
//							System.out.println(currentPiece);

					} else  //not released within y bounds of board
					{
						currentPiece = null;
						currentPos = new Position(0,0);

//						System.out.println("c");
//						System.out.println(currentPos);
//						if(currentPiece!=null)
//							System.out.println(currentPiece.printPiece());
//						else
//							System.out.println(currentPiece);
//						System.out.println();
					}

				}
			} else
			{
				currentPiece = null;
				currentPos = new Position(0,0);

//				System.out.println("c");
//				System.out.println(currentPos);
//				if(currentPiece!=null)
//					System.out.println(currentPiece.printPiece());
//				else
//					System.out.println(currentPiece);
//				System.out.println();
			}

			pressed = false;

			panel.repaint();
		} else {
			//GIVE PLAYER OPTION TO PLAY AGAIN
		}
	}

//================================================================================
//	MAIN
//================================================================================


	public static void main(String[] args) {

		new Onitama();

	}

}
