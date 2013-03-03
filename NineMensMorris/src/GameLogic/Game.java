package GameLogic;

public abstract class Game {
	protected static final int MIN_NUM_PIECES = 2;
	protected Board gameBoard;
	//protected Player player1;
	//protected Player player2;
	protected boolean placingPhase;
	protected boolean gameIsOver;
	//protected Player currentTurnPlayer;
	
	public Game() {
		gameBoard = new Board();
		placingPhase = true;
		gameIsOver = false;
	}
	
	/*
	public void setPlayers(Player p1, Player p2) {
		System.out.println("Setting game players");
		player1 = p1;
		player2 = p2;
		currentTurnPlayer = player1;
	}
	*/
	
	public boolean inPlacingPhase() {
		return placingPhase;
	}
	
	/*
	public Player getCurrentTurnPlayer() {
		return currentTurnPlayer;
	}
	
	public void updateCurrentTurnPlayer() {
		if(currentTurnPlayer.equals(player1)) {
			currentTurnPlayer = player2;
		} else {
			currentTurnPlayer = player1;
		}
	}
	*/
	
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
				placingPhase = false;
			}
			return true;
		}
		return false;
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
