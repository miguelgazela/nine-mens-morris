package GameLogic;

public class Position {
	public boolean isOccupied;
	public int position;
	public int playerOccupying;
	public int[] adjacentPositions;
	
	public Position(int position) {
		isOccupied = false;
		this.position = position;
		playerOccupying = -1;
		adjacentPositions = new int[4];
	}
	
	public void addAdjacentPositions(int pos1, int pos2) {
		adjacentPositions[0] = pos1;
		adjacentPositions[1] = pos2;
		adjacentPositions[2] = -1;
		adjacentPositions[3] = -1;
	}
	
	public void addAdjacentPositions(int pos1, int pos2, int pos3) {
		adjacentPositions[0] = pos1;
		adjacentPositions[1] = pos2;
		adjacentPositions[2] = pos3;
		adjacentPositions[3] = -1;
	}
	
	public void addAdjacentPositions(int pos1, int pos2, int pos3, int pos4) {
		adjacentPositions[0] = pos1;
		adjacentPositions[1] = pos2;
		adjacentPositions[2] = pos3;
		adjacentPositions[3] = pos4;
	}
	
	
}
