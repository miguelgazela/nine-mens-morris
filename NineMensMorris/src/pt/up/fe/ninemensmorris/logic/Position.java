package pt.up.fe.ninemensmorris.logic;

public class Position {
	
	private boolean isOccupied;
	private int positionIndex;
	private Token playerOccupying;
	private int[] adjacentPositionsIndexes;
	
	public Position(int position) {
		isOccupied = false;
		this.positionIndex = position;
		playerOccupying = Token.NO_PLAYER;
	}

	public boolean isOccupied() {
		return isOccupied;
	}
	
	public int getPositionIndex() {
		return positionIndex;
	}
	
	public Token getPlayerOccupyingIt() {
		return playerOccupying;
	}

	public void setAsOccupied(Token player) {
		isOccupied = true;
		playerOccupying = player;
	}
	
	/**
	 * Clears a position and returns the token of the player that was there
	 * @return
	 */
	public Token setAsUnoccupied() {
		isOccupied = false;
		Token oldPlayer = playerOccupying;
		playerOccupying = Token.NO_PLAYER;
		return oldPlayer;
	}
	
	public void addAdjacentPositionsIndexes(int pos1, int pos2) {
		adjacentPositionsIndexes = new int[2];
		adjacentPositionsIndexes[0] = pos1;
		adjacentPositionsIndexes[1] = pos2;
	}
	
	public void addAdjacentPositionsIndexes(int pos1, int pos2, int pos3) {
		adjacentPositionsIndexes = new int[3];
		adjacentPositionsIndexes[0] = pos1;
		adjacentPositionsIndexes[1] = pos2;
		adjacentPositionsIndexes[2] = pos3;
	}
	
	public void addAdjacentPositionsIndexes(int pos1, int pos2, int pos3, int pos4) {
		adjacentPositionsIndexes = new int[4];
		adjacentPositionsIndexes[0] = pos1;
		adjacentPositionsIndexes[1] = pos2;
		adjacentPositionsIndexes[2] = pos3;
		adjacentPositionsIndexes[3] = pos4;
	}
	
	public int[] getAdjacentPositionsIndexes() {
		return adjacentPositionsIndexes;
	}
	
	public boolean isAdjacentToThis(int posIndex) {
		for(int i = 0; i < adjacentPositionsIndexes.length; i++) {
			if(adjacentPositionsIndexes[i]== posIndex) {
				return true;
			}
		}
		return false;
	}
}
