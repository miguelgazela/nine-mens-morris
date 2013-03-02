package GameLogic;

public abstract class Game {
	private static final int MIN_NUM_PIECES = 2;
	protected Board gameBoard;
	protected Player player1;
	protected Player player2;
	protected boolean placingPhase;
	protected boolean gameIsOver;
	protected Player currentTurnPlayer;
	
	public Game() {
		gameBoard = new Board();
		placingPhase = true;
		gameIsOver = false;
	}
	
	public void setPlayers(Player p1, Player p2) {
		System.out.println("Setting game players");
		player1 = p1;
		player2 = p2;
		currentTurnPlayer = player1;
	}
	
	public boolean inPlacingPhase() {
		return placingPhase;
	}
	
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
	
	public void movePieceFromTo(int src, int dest) {
		int player = (currentTurnPlayer.equals(player1)) ? Player.PLAYER_1 : Player.PLAYER_2;
		gameBoard.boardPositions[src].isOccupied = false;
		gameBoard.boardPositions[src].playerOccupying = -1;
		gameBoard.boardPositions[dest].isOccupied = true;
		gameBoard.boardPositions[dest].playerOccupying = player; 
	}
	
	public boolean setPiece(int index) {
		if(positionIsAvailable(index)) {
			int player = currentTurnPlayer.equals(player1) ? Player.PLAYER_1 : Player.PLAYER_2;
			setPositionAsPlayer(index, player);
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
	
	public void checkGameIsOver() {
		if(player1.getNumPieces() == MIN_NUM_PIECES || player2.getNumPieces() == MIN_NUM_PIECES) {
			gameIsOver = true;
		}
	}

	public boolean gameIsOver() {
		return gameIsOver;
	}

	public boolean positionHasPieceOfPlayer(int index) {
		Position pos = gameBoard.boardPositions[index];
		if(pos.isOccupied) {
			int player = pos.playerOccupying;
			if( (player == Player.PLAYER_1 && currentTurnPlayer.equals(player1)) ||
					player == Player.PLAYER_2 && currentTurnPlayer.equals(player2)) {
				return true;
			}
		}
		return false;
	}
}
