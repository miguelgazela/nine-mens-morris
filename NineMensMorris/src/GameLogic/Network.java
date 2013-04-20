package GameLogic;

import java.util.Calendar;

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
		kryo.register(Place.class);
		kryo.register(Remove.class);
		kryo.register(Move.class);
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
}
