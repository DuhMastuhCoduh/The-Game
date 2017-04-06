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
	
////////////////////////VARIBALES////////////////////////

	private JFrame frame;
	private JPanel panel;

	private Player player1,player2;

	private Board board;

	private ArrayList<ArrayList<Position>> legalMoves;

	int mouseX,mouseY;

	private Position currentPos = new Position(0,0);
	private Position lastPos = currentPos;
	private Piece currentPiece = null;

	boolean redTurn;
	
////////////////////////CONSTRUCTOR////////////////////////

	public Onitama() 
	{

		player1 = new Player(true);
		player2 = new Player(false);

		setCards();
		setFirstTurn();
		setLegalMoves();

		//debug

		System.out.println(redTurn);

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
	
////////////////////////START METHODS////////////////////////

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

		ArrayList<Card> allCards = new ArrayList<Card>();
		for(int i = 1; i < 17; i++) 
		{
			allCards.add(new Card(i));
		}

		ArrayList<Card> cards = new ArrayList<Card>(5);
		for(int i = 0; i < 5; i++) 
		{
			int randIndex = (int)(Math.random()*(allCards.size())+1);
			cards.add(allCards.get(randIndex-1));
			allCards.remove(randIndex-1);
		}

		player1.setCard1(cards.get(0));
		player1.setCard2(cards.get(1));
		player2.setCard1(cards.get(2));
		player2.setCard2(cards.get(3));

		System.out.println("player1: " + player1);
		System.out.println("player2: " + player2);
		System.out.println();

		board = new Board(cards.get(4));

		System.out.println("board card: " + board.getCard());
	}
	
////////////////////////GAMEPLAY////////////////////////

	private void setLegalMoves() 
	{
		legalMoves = new ArrayList<ArrayList<Position>>();
		ArrayList<Position> cardMoves = new ArrayList<Position>();

		if(redTurn) 
		{	//sets legal moves for a red turn

			cardMoves = player1.getCard1().getLegalMoves();

			/*goes through each disciple and creates a new position for
			 * each of the first card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < player1.getDisciples().size();i++) 
			{	
				Piece temp = player1.getDisciples().get(i);
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
							if(player1.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			cardMoves = player1.getCard2().getLegalMoves();

			/* goes through each disciple and creates a new position for
			 * each of the second card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < player1.getDisciples().size();i++) 
			{
				Piece temp = player1.getDisciples().get(i);

				for(int c = 0; c < cardMoves.size(); c++) 
				{
					int tempRow = temp.getPosition().getRow()+cardMoves.get(c).getRow();
					int tempCol = temp.getPosition().getCol()+cardMoves.get(c).getCol();

					if(tempRow > -1 && tempRow < 5) 
					{
						if(tempCol > -1 && tempCol < 5) 
						{
							Position tempPos = new Position(tempRow,tempCol);
							if(player1.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			//master
			legalMoves.add(new ArrayList<Position>());
			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = player1.getMaster().getPosition().getRow()+cardMoves.get(i).getRow();
				int tempCol = player1.getMaster().getPosition().getCol()+cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(player1.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}

			cardMoves = player1.getCard1().getLegalMoves();

			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = player1.getMaster().getPosition().getRow()+cardMoves.get(i).getRow();
				int tempCol = player1.getMaster().getPosition().getCol()+cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(player1.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}
		} else 
		{	//sets legal moves for blue turn
			cardMoves = player2.getCard1().getLegalMoves();

			/*goes through each disciple and creates a new position for
			 * each of the first card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < player2.getDisciples().size();i++) 
			{
				Piece temp = player2.getDisciples().get(i);
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
							if(player2.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}

			cardMoves = player2.getCard2().getLegalMoves();

			/*goes through each disciple and creates a new position for
			 * each of the second card's legal moves, and adds them if 
			 * they are within the bounds of the board
			 */
			for(int i = 0; i < player2.getDisciples().size();i++) 
			{
				Piece temp = player2.getDisciples().get(i);
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
							if(player2.getPiece(tempPos)==null)
								legalMoves.get(i).add(tempPos);
						}
					}
				}
			}
			//master
			legalMoves.add(new ArrayList<Position>());
			
			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = player2.getMaster().getPosition().getRow()+-1*cardMoves.get(i).getRow();
				int tempCol = player2.getMaster().getPosition().getCol()+-1*cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(player2.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}

			cardMoves = player2.getCard1().getLegalMoves();

			for(int i = 0; i < cardMoves.size();i++) 
			{

				int tempRow = player2.getMaster().getPosition().getRow()+-1*cardMoves.get(i).getRow();
				int tempCol = player2.getMaster().getPosition().getCol()+-1*cardMoves.get(i).getCol();

				if(tempRow > -1 && tempRow < 5) 
				{
					if(tempCol > -1 && tempCol < 5) 
					{
						Position tempPos = new Position(tempRow,tempCol);
						if(player2.getPiece(tempPos)==null)
							legalMoves.get(legalMoves.size()-1).add(tempPos);
					}
				}
			}

		}
	}

	private void checkLegalMove() {
		
	}

	private void move() 
	{



		redTurn = !redTurn;
	}

	private boolean checkWin() 
	{
		return false;
	}

////////////////////////GRAPHICS////////////////////////
	
	@SuppressWarnings("serial")
	private class OnitamaPanel extends JPanel
	{

		int boardX = 207,boardY = 203;
		int boardHeight = 77,boardWidth = 77;

		public OnitamaPanel() {

		}

		private void drawPieces(Graphics g) {
			player1.drawPieces(g);
			player2.drawPieces(g);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			try {
				final BufferedImage oniBoard = ImageIO.read(new File("resources\\board.png"));
				g.drawImage(oniBoard, 0, 0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			g.setColor(Color.GREEN);
			g.drawRect(boardX, boardY, boardWidth*5, boardHeight*5);
			for(int i = 0; i < 5;i++) {
				for(int c = 0; c<5;c++) {
					g.drawRect(boardX+c*boardWidth, boardY+i*boardHeight, boardWidth, boardHeight);
				}
			}
			drawPieces(g);
		}

	}

////////////////////////MOUSE EVENTS////////////////////////
	
	public void mouseClicked(MouseEvent e) {


	}

	
	public void mouseEntered(MouseEvent e) {


	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
	public void mouseReleased(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();
		
		if(mouseX>207&&mouseX<207+77*5) 
		{
			if(mouseY>203&&mouseY<203+77*5) 
			{
				lastPos = currentPos;
				
				for(int i = 0; i < 5; i++) 
				{
					if(mouseX>207+77*(i)) 
					{
						currentPos.setCol(i);
					}
					if(mouseY>203+77*(i)) 
					{
						currentPos.setRow(i);
					}	
				}
				
				currentPiece = player1.getPiece(currentPos);
				
				if(currentPiece == null) 
				{
					currentPiece = player2.getPiece(currentPos);
				}
				if(currentPiece != null) 
				{
					//move
				}
			}
		}
		panel.repaint();
	}
	
////////////////////////MAIN////////////////////////
	
	public static void main(String[] args) {

		new Onitama();

	}

}
