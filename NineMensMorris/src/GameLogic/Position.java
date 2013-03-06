package GameLogic;

public class Position {
	public static final int NO_PLAYER = -1;
	public boolean isOccupied;
	public int position;
	public int playerOccupying;
	public int[] adjacentPositions;
	int nothing;
	
	public Position(int position) {
		isOccupied = false;
		this.position = position;
		playerOccupying = Position.NO_PLAYER;
	}
	
	public void addAdjacentPositions(int pos1, int pos2) {
		adjacentPositions = new int[2];
		adjacentPositions[0] = pos1;
		adjacentPositions[1] = pos2;
	}
	
	public void addAdjacentPositions(int pos1, int pos2, int pos3) {
		adjacentPositions = new int[3];
		adjacentPositions[0] = pos1;
		adjacentPositions[1] = pos2;
		adjacentPositions[2] = pos3;
	}
	
	public void addAdjacentPositions(int pos1, int pos2, int pos3, int pos4) {
		adjacentPositions = new int[4];
		adjacentPositions[0] = pos1;
		adjacentPositions[1] = pos2;
		adjacentPositions[2] = pos3;
		adjacentPositions[3] = pos4;
	}
	
	
}
