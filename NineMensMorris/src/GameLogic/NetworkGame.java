package GameLogic;

import java.util.ArrayList;


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
			gameBoard.decNumPiecesOfPlayer(player);
			if(player == this.player.getPlayerToken()) {
				this.player.lowerNumPiecesOnBoard();
			}
			return true;
		}
		return false;
	}
	
	public void checkGameIsOver() {
		if((player.getNumPiecesOnBoard() == Game.MIN_NUM_PIECES)) {
			gameIsOver = true;
		}
	}
	
	public void updateGameWithOpponentMoves(ArrayList<Move> opponentMoves) throws GameException {
		for(Move move : opponentMoves) {
			if(move.typeOfMove == Move.PLACING) {
				placePieceOfPlayer(move.destIndex, (player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1));
			} else if(move.typeOfMove == Move.REMOVING) {
				removePiece(move.removePieceOnIndex, (player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_1 : Token.PLAYER_2));
			} else if(move.typeOfMove == Move.MOVING) {
				movePieceFromTo(move.srcIndex, move.destIndex, (player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1));
			}
		}
		opponentMoves.clear();
	}
}
