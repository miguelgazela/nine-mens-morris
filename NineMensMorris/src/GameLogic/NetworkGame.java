package GameLogic;


public class NetworkGame extends Game {
	
	protected Player player;
	protected boolean thisPlayerTurn;
	
	public NetworkGame() {
		thisPlayerTurn = false;
	}
	
	public void setPlayer(Player p) {
		player = p;
	}
	
	public void setTurn(boolean turn) {
		thisPlayerTurn = turn;
	}
	
	public boolean isThisPlayerTurn() {
		return thisPlayerTurn;
	}
	
	public boolean playedFirst(Token playerThatPlayedFirst) {
		return (player.getPlayerToken() == playerThatPlayedFirst);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public boolean removePiece(int boardIndex, Token player) throws GameException {
		if(!gameBoard.positionIsAvailable(boardIndex) && positionHasPieceOfPlayer(boardIndex, player)) {
			gameBoard.getPosition(boardIndex).setAsUnoccupied();
			this.player.lowerNumPiecesOnBoard();
			return true;
		}
		return false;
	}
	
	public void checkGameIsOver() {
		if((player.getNumPiecesOnBoard() == Game.MIN_NUM_PIECES)) {
			gameIsOver = true;
		}
	}
}
