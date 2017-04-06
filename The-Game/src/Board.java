
public class Board 
{
	
	private Card card;
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public static final char[][] board = new char[][] 
	{
		{'e','e','b','e','e'},
		{'e','e','e','e','e'},
		{'e','e','e','e','e'},
		{'e','e','e','e','e'},
		{'e','e','r','e','e'},
	};
	
	public Board(Card card) 
	{
		this.card = card;
		System.out.println("five");
	}

}
