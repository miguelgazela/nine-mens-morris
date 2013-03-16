package GameLogic;

public abstract class Player {
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;
	
	protected String name;
	protected int score;
	protected int numPieces;
	protected int playerId;
	protected boolean canFly;
	
	protected Player() {
		score = 0;
		numPieces = 9;
		canFly = false;
	}
	
	protected Player(int playerId) throws InvalidPlayerId {
		this();
		if(playerId < PLAYER_1 || playerId > PLAYER_2) {
			throw new InvalidPlayerId();
		} else {
			this.playerId = playerId;
		}
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
	
	public int removePiece() {
		if(--numPieces == 3) {
			canFly = true;
		}
		return numPieces;
	}
	
	public boolean canItFly() {
		return canFly;
	}
	
	public abstract boolean isIA();
	
	// Exceptions
	public class InvalidPlayerId extends Exception {}
}
