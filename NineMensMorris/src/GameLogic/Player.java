package GameLogic;

public abstract class Player {
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;
	protected String name;
	protected int score;
	protected int numPieces;
	
	protected Player() {
		System.out.println("Player constructor");
		score = 0;
		numPieces = 9;
	}
	
	protected Player(String name) {
		this();
		this.name = name;
	}
	
	protected String getName() {
		return name;
	}
	
	protected int getNumPieces() {
		return numPieces;
	}
}
