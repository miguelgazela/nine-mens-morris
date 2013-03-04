package GameLogic;

public abstract class Game {
	protected static final int MIN_NUM_PIECES = 2;
	protected static final int PLACING_PHASE = 1;
	protected static final int MOVING_PHASE = 2;
	protected static final int FLYING_PHASE = 3;
	
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
	
	public void setPositionAsPlayer(int index, int player) {
		gameBoard.boardPositions[index].playerOccupying = player;
		gameBoard.boardPositions[index].isOccupied = true;
		gameBoard.numberPiecesOnBoard++;
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
		gameBoard.boardPositions[dest].isOccupied = true;
		gameBoard.boardPositions[dest].playerOccupying = playerId; 
	}
	
	public boolean setPiece(int index, int playerId) {
		if(positionIsAvailable(index)) {
			setPositionAsPlayer(index, playerId);
			if(gameBoard.numberPiecesOnBoard == 18) {
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
					int thisRow = fromPlayerInRow(gameBoard.winningPositions[i], playerId);
					if(thisRow > maxPiecesInRow) {
						maxPiecesInRow = thisRow;
					}
				}
			}
		}
		return maxPiecesInRow == 3;
	}
	
	public boolean removePiece(int index, int playerId) {
		if(!positionIsAvailable(index) && positionHasPieceOfPlayer(index, playerId)) {
			gameBoard.boardPositions[index].playerOccupying = Position.NO_PLAYER;
			gameBoard.boardPositions[index].isOccupied = false;
			gameBoard.numberPiecesOnBoard--;
			return true;
		}
		return false;
	}
	
	private int fromPlayerInRow(Position[] pos, int playerId) {
		int counter = 0;
		for(int i = 0; i < pos.length; i++) {
			if(pos[i].playerOccupying == playerId) {
				counter++;
			}
		}
		return counter;
	}
	
	public void printGameBoard() { // TODO this won't be used with a GUI
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
}
