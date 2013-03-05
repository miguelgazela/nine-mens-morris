package GameLogic;

public class LocalGame extends Game {
	private Player player1;
	private Player player2;
	private Player currentTurnPlayer;
	
	public LocalGame() {
		super();
		System.out.println("Creating LocalGame.");
	}
	
	public void setPlayers(Player p1, Player p2) {
		player1 = p1;
		player2 = p2;
		currentTurnPlayer = player1;
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
	
	public boolean removePiece(int index, int playerId) {
		if(!positionIsAvailable(index) && positionHasPieceOfPlayer(index, playerId)) {
			gameBoard.boardPositions[index].playerOccupying = Position.NO_PLAYER;
			gameBoard.boardPositions[index].isOccupied = false;
			Player p = currentTurnPlayer.equals(player1) ? player2 : player1;
			p.removePiece();
			return true;
		}
		return false;
	}
	
	public void checkGameIsOver() {
		if((player1.getNumPieces() == Game.MIN_NUM_PIECES) || (player2.getNumPieces() == Game.MIN_NUM_PIECES)) {
			gameIsOver = true;
		}
	}
}
