package pt.up.fe.ninemensmorris.network;

import java.util.Calendar;

import pt.up.fe.ninemensmorris.logic.Token;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public abstract class Network {
	static protected final int TPC_PORT = 54555;
	static protected final int UDP_PORT = 54777;
	
	protected boolean connectionEstablished;
	protected Calendar calendar;
	
	protected Network() {
		connectionEstablished = false;
		calendar = Calendar.getInstance();
	}
	
	public boolean hasConnectionEstablished() {
		return connectionEstablished;
	}
	
	public void logThisMessage(String message) {
		System.out.println("["+calendar.get(Calendar.HOUR_OF_DAY)+':'+calendar.get(Calendar.MINUTE)+':'+calendar.get(Calendar.SECOND)+"] "+message);
	}
	
	static protected void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(JoinGame.class);
		kryo.register(JoinAck.class);
		kryo.register(PiecePlacing.class);
		kryo.register(PieceRemoving.class);
		kryo.register(PieceMoving.class);
		kryo.register(Token.class);
		kryo.register(StartGame.class);
		kryo.register(FullServer.class);
		kryo.register(ActionValidation.class);
		kryo.register(ThisPlayerTurn.class);
	}
	
	static protected class JoinGame {
		public Token playerToken;
	}
	
	static protected class JoinAck {
		public Token playerAcknowledged;
	}
	
	static protected class FullServer {
		
	}
	
	static protected class StartGame {
		public Token playerWhoPlaysFirst;
	}

	static protected class PiecePlacing {
		public Token player;
		public int boardIndex;
	}
	
	static protected class PieceRemoving {
		public Token player;
		public int boardIndex;
	}
	
	static protected class PieceMoving {
		public Token player;
		public int srcIndex, destIndex;
	}
	
	static protected class ActionValidation {
		public boolean validAction;
	}
	
	static protected class ThisPlayerTurn {
		public Token player;
	}
}
