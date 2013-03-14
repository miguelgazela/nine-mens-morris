package GameLogic;

public abstract class Game {
	protected static final int MIN_NUM_PIECES = 2;
	public static final int PLACING_PHASE = 1;
	public static final int MOVING_PHASE = 2;
	public static final int FLYING_PHASE = 3;
	
	protected Board gameBoard;
	protected int gamePhase;
	protected boolean gameIsOver;
	
	public Game() {
		gameBoard = new Board();
		gamePhase = Game.PLACING_PHASE;
		gameIsOver = false;
	}
	
	public int getGamePhase() {
		return gamePhase;
	}
	
	public void setPositionAsPlayer(int index, int playerId) {
		gameBoard.boardPositions[index].playerOccupying = playerId;
		gameBoard.boardPositions[index].isOccupied = true;
	}
	
	public boolean positionIsAvailable(int index) {
		return !gameBoard.boardPositions[index].isOccupied;
	}
	
	public boolean validMove(int currentPositionIndex, int nextPositionIndex) {
		for(int i = 0; i < gameBoard.boardPositions[currentPositionIndex].adjacentPositions.length; i++) {
			if(gameBoard.boardPositions[currentPositionIndex].adjacentPositions[i] == nextPositionIndex) {
				return true;
			}
		}
		return false;
	}
	
	public void movePieceFromTo(int src, int dest, int playerId) {
		gameBoard.boardPositions[src].isOccupied = false;
		gameBoard.boardPositions[src].playerOccupying = -1;
		setPositionAsPlayer(dest, playerId);
	}
	
	public boolean setPiece(int boardIndex, int playerId) {
		if(positionIsAvailable(boardIndex)) {
			setPositionAsPlayer(boardIndex, playerId);
			if(++gameBoard.numberPiecesPlaced == 18) {
				gamePhase = Game.MOVING_PHASE;
			}
			return true;
		}
		return false;
	}
	
	public boolean madeAMill(int dest, int playerId) {
		int maxPiecesInRow = 0;
		for(int i = 0; i < gameBoard.winningPositions.length; i++) {
			for(int j = 0; j < gameBoard.winningPositions[i].length; j++) {
				if(gameBoard.winningPositions[i][j].position == dest) {
					int thisRow = numPiecesFromPlayerInRow(gameBoard.winningPositions[i], playerId);
					if(thisRow > maxPiecesInRow) {
						maxPiecesInRow = thisRow;
					}
				}
			}
		}
		return maxPiecesInRow == 3;
	}
	
	private int numPiecesFromPlayerInRow(Position[] pos, int playerId) {
		int counter = 0;
		for(int i = 0; i < pos.length; i++) {
			if(pos[i].playerOccupying == playerId) {
				counter++;
			}
		}
		return counter;
	}
	
	public void printGameBoard() {
		gameBoard.printBoard();
	}

	public boolean gameIsOver() {
		return gameIsOver;
	}

	public boolean positionHasPieceOfPlayer(int index, int playerId) {
		Position pos = gameBoard.boardPositions[index];
		if(pos.isOccupied) {
			if(pos.playerOccupying == playerId) {
				return true;
			}
		}
		return false;
	}
	
	public abstract boolean removePiece(int index, int playerId);
	public abstract void checkGameIsOver();
}
