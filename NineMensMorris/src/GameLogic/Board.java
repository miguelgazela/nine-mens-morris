package GameLogic;

public class Board {
	private static final int NUMBER_POSITIONS = 24;
	private Position[] boardPositions;
	private int numberPiecesOnBoard;
	
	public Board() {
		boardPositions = new Position[24];
		numberPiecesOnBoard = 0;
		initBoard();
	}
	
	public void initBoard() {
		for(int i = 0; i < 24; i++) {
			boardPositions[i] = new Position(i);
		}
		
		// outer square
		boardPositions[0].addAdjacentPositions(1,9);
		boardPositions[1].addAdjacentPositions(0,2,4);
		boardPositions[2].addAdjacentPositions(1,14);
		boardPositions[9].addAdjacentPositions(0,10,21);
		boardPositions[14].addAdjacentPositions(2,13,23);
		boardPositions[21].addAdjacentPositions(9,22);
		boardPositions[22].addAdjacentPositions(19,21,23);
		boardPositions[23].addAdjacentPositions(14,22);
		
		// middle square
		boardPositions[3].addAdjacentPositions(4,10);
		boardPositions[4].addAdjacentPositions(1,3,5,7);
		boardPositions[5].addAdjacentPositions(4,13);
		boardPositions[10].addAdjacentPositions(3,9,11,18);
		boardPositions[13].addAdjacentPositions(5,12,14,20);
		boardPositions[18].addAdjacentPositions(10,19);
		boardPositions[19].addAdjacentPositions(16,18,20,22);
		boardPositions[20].addAdjacentPositions(13,19);
		
		// inner square
		boardPositions[6].addAdjacentPositions(7,11);
		boardPositions[7].addAdjacentPositions(4,6,8);
		boardPositions[8].addAdjacentPositions(7,12);
		boardPositions[11].addAdjacentPositions(6,10,15);
		boardPositions[12].addAdjacentPositions(8,13,17);
		boardPositions[15].addAdjacentPositions(11,16);
		boardPositions[16].addAdjacentPositions(15,17,19);
		boardPositions[17].addAdjacentPositions(12,16);
	}
	
	public boolean validMove(int currentPositionIndex, int nextPositionIndex) {
		for(int i = 0; i < boardPositions[currentPositionIndex].adjacentPositions.length; i++) {
			if(boardPositions[currentPositionIndex].adjacentPositions[i] == nextPositionIndex) {
				return true;
			}
		}
		return false;
	}

	public boolean positionIsAvailable(int index) {
		return !boardPositions[index].isOccupied;
	}

	public void printBoard() {
		System.out.println(showPos(0)+" - - - - - "+showPos(1)+" - - - - - "+showPos(2));
		System.out.println("|           |           |");
		System.out.println("|     "+showPos(3)+" - - "+showPos(4)+" - - "+showPos(5)+"     |");
		System.out.println("|     |     |     |     |");
		System.out.println("|     | "+showPos(6)+" - "+showPos(7)+" - "+showPos(8)+" |     |" );
		System.out.println("|     | |       | |     |");
		System.out.println(showPos(9)+" - - "+showPos(10)+"-"+showPos(11)+"       "+showPos(12)+"-"+showPos(13)+" - - "+showPos(14));
		System.out.println("|     | |       | |     |");
		System.out.println("|     | "+showPos(15)+" - "+showPos(16)+" - "+showPos(17)+" |     |" );
		System.out.println("|     |     |     |     |");
		System.out.println("|     "+showPos(18)+" - - "+showPos(19)+" - - "+showPos(20)+"     |");
		System.out.println("|           |           |");
		System.out.println(showPos(21)+" - - - - - "+showPos(22)+" - - - - - "+showPos(23));
	}

	private String showPos(int i) {
		switch (boardPositions[i].playerOccupying) {
		case 1:
			return "X";
		case 2:
			return "O";
		default:
			return "*";
		}
	}

	public void setPositionAsPlayer(int index, int player) {
		boardPositions[index].playerOccupying = player;
		boardPositions[index].isOccupied = true;
		numberPiecesOnBoard++;
	}
	
	public int getNumberPiecesOnBoard() {
		return numberPiecesOnBoard;
	}
}
