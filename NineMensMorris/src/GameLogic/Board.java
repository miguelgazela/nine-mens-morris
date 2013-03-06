package GameLogic;

public class Board {
	public Position[] boardPositions;
	public Position[][] winningPositions;
	public int numberPiecesPlaced;
	
	public Board() {
		boardPositions = new Position[24];
		numberPiecesPlaced = 0;
		initBoard();
		initWinningPositions();
	}
	
	private void initBoard() {
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
	
	private void initWinningPositions() {
		winningPositions = new Position[16][3];
		
		//outer square
		winningPositions[0][0] = boardPositions[0];
		winningPositions[0][1] = boardPositions[1];
		winningPositions[0][2] = boardPositions[2];
		winningPositions[1][0] = boardPositions[0];
		winningPositions[1][1] = boardPositions[9];
		winningPositions[1][2] = boardPositions[21];
		winningPositions[2][0] = boardPositions[2];
		winningPositions[2][1] = boardPositions[14];
		winningPositions[2][2] = boardPositions[23];
		winningPositions[3][0] = boardPositions[21];
		winningPositions[3][1] = boardPositions[22];
		winningPositions[3][2] = boardPositions[23];
		//middle square
		winningPositions[4][0] = boardPositions[3];
		winningPositions[4][1] = boardPositions[4];
		winningPositions[4][2] = boardPositions[5];
		winningPositions[5][0] = boardPositions[3];
		winningPositions[5][1] = boardPositions[10];
		winningPositions[5][2] = boardPositions[18];
		winningPositions[6][0] = boardPositions[5];
		winningPositions[6][1] = boardPositions[13];
		winningPositions[6][2] = boardPositions[20];
		winningPositions[7][0] = boardPositions[18];
		winningPositions[7][1] = boardPositions[19];
		winningPositions[7][2] = boardPositions[20];
		//inner square
		winningPositions[8][0] = boardPositions[6];
		winningPositions[8][1] = boardPositions[7];
		winningPositions[8][2] = boardPositions[8];
		winningPositions[9][0] = boardPositions[6];
		winningPositions[9][1] = boardPositions[11];
		winningPositions[9][2] = boardPositions[15];
		winningPositions[10][0] = boardPositions[8];
		winningPositions[10][1] = boardPositions[12];
		winningPositions[10][2] = boardPositions[17];
		winningPositions[11][0] = boardPositions[15];
		winningPositions[11][1] = boardPositions[16];
		winningPositions[11][2] = boardPositions[17];
		//others
		winningPositions[12][0] = boardPositions[1];
		winningPositions[12][1] = boardPositions[4];
		winningPositions[12][2] = boardPositions[7];
		winningPositions[13][0] = boardPositions[9];
		winningPositions[13][1] = boardPositions[10];
		winningPositions[13][2] = boardPositions[11];
		winningPositions[14][0] = boardPositions[12];
		winningPositions[14][1] = boardPositions[13];
		winningPositions[14][2] = boardPositions[14];
		winningPositions[15][0] = boardPositions[16];
		winningPositions[15][1] = boardPositions[19];
		winningPositions[15][2] = boardPositions[22];
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
}
