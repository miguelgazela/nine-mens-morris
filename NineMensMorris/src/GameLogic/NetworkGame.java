package GameLogic;

import java.util.Calendar;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log.Logger;

public abstract class NetworkGame extends Game {
	
	static protected final int TPC_PORT = 54555;
	static protected final int UDP_PORT = 54777;
	
	protected Player player;
	protected String otherSidePlayerName;
	protected boolean isThisPlayerTurn;
	protected Calendar calendar;
	protected boolean connectionEstablished;
	protected Token playerWhoGoesFirst;
	
	public NetworkGame() {
		otherSidePlayerName = null;
		connectionEstablished = false;
		isThisPlayerTurn = false;
		playerWhoGoesFirst = Token.NO_PLAYER;
		calendar = Calendar.getInstance();
	}
	
	public void setPlayer(Player p) {
		player = p;
	}
	
	public void setTurn(boolean turn) {
		isThisPlayerTurn = turn;
	}
	
	public boolean isThisPlayerTurn() {
		return isThisPlayerTurn;
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
	
	public void logThisMessage(String message) {
		System.out.println("["+calendar.get(Calendar.HOUR_OF_DAY)+':'+calendar.get(Calendar.MINUTE)+':'+calendar.get(Calendar.SECOND)+"] "+message);
	}

	public boolean hasConnectionEstablished() {
		return connectionEstablished;
	}
	
	public abstract void sendGameOver();
	public abstract boolean setPiece(int boardIndex);
	public abstract boolean removePiece(int boardIndex);
	public abstract void movePieceFromTo(int src, int dest);
	
	// this registers objects that are going to be sent over the network
	static protected void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(JoinGame.class);
		kryo.register(JoinAck.class);
		kryo.register(Place.class);
		kryo.register(Remove.class);
		kryo.register(Move.class);
		kryo.register(GameOver.class);
	}
	
	static protected class JoinGame {
		public String nameOfClientPlayer;
	}
	
	static protected class JoinAck {
		public String nameofServerPlayer;
		public boolean clientPlayerGoesFirst;
	}

	static protected class Place {
		public int playerId, boardIndex;
	}
	
	static protected class ActionAck {
		public boolean validAction; // TODO for places, removes and moves? 
	}
	
	static protected class Remove {
		public int playerId, boardIndex;
	}
	
	static protected class Move {
		public int playerId, srcIndex, destIndex;
	}
	
	static protected class GameOver {}
}
