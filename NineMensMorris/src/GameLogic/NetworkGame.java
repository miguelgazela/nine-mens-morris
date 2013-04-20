package GameLogic;

import java.util.Calendar;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log.Logger;

public class NetworkGame extends Game {
	
	protected Player player;
	//protected String opponentPlayerName;
	protected boolean thisPlayerTurn;
	protected Token playerWhoGoesFirst;
	
	public NetworkGame() {
		//opponentPlayerName = null;
		thisPlayerTurn = false;
		playerWhoGoesFirst = Token.NO_PLAYER;
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
	
	public boolean playedFirst() {
		return (player.getPlayerToken() == playerWhoGoesFirst);
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
