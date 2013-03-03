package GameLogic;

public abstract class Player {
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;
	protected String name;
	protected int score;
	private int numPieces;
	private int playerId;
	
	protected Player() {
		//System.out.println("Player constructor");
		score = 0;
		numPieces = 9;
	}
	
	protected Player(int playerId) {
		this();
		this.playerId = playerId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumPieces() {
		return numPieces;
	}
	
	public int getPlayerId() {
		return playerId;
	}
}
