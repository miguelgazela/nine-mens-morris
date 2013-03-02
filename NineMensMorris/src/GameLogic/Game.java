package GameLogic;

public abstract class Game {
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
	
	public boolean validPlacing(int index) {
		return gameBoard.positionIsAvailable(index);
	}
	
	public boolean setPiece(int index) {
		if(gameBoard.positionIsAvailable(index)) {
			int player = currentTurnPlayer.equals(player1) ? Player.PLAYER_1 : Player.PLAYER_2;
			gameBoard.setPositionAsPlayer(index, player);
			if(gameBoard.getNumberPiecesOnBoard() == 18) {
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
}
