package GameLogic;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkGame extends Game {
	
	static protected final int port = 54555;
	protected Player player;
	protected boolean connectionEstablished;
	protected String otherPlayerName;
	
	public NetworkGame() {
		connectionEstablished = false;
	}
	
	// this registers objects that are going to be sent over the network
	static protected void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(JoinGame.class);
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
	}

	static protected class Place {
		public int playerId, boardIndex;
	}
	
	static protected class Remove {
		public int playerId, boardIndex;
	}
	
	static protected class Move {
		public int playerId, srcIndex, destIndex;
	}
	
	static protected class GameOver {}
	
	@Override
	public boolean removePiece(int index, int playerId) {
		// TODO Auto-generated method stub
		return false;
	}

}
