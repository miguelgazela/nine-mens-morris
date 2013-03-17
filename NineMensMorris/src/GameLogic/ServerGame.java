package GameLogic;

import java.io.IOException;
import java.util.Random;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerGame extends NetworkGame {
	private Server server;
	private Random random;
	
	public ServerGame() throws IOException {
		super();
		random = new Random();
		server = new Server() {
			protected Connection newConnection() {
				return new GameConnection(player.getPlayerId(),player.getName());
			}
		};
		NetworkGame.register(server);
		
		server.addListener(new Listener() {
			public void received(Connection c, Object object) {
				
				if(object instanceof JoinGame) {
					if(connectionEstablished) { //ignore if player is already connected
						return;
					}
					otherSidePlayerName = ((JoinGame)object).nameOfClientPlayer;
					connectionEstablished = true;
					logThisMessage("SERVER RECEIVED REQUEST TO JOIN GAME FROM "+((JoinGame)object).nameOfClientPlayer+ " "+c.getRemoteAddressTCP());
					
					JoinAck ack = new JoinAck();
					ack.nameofServerPlayer = player.getName();
					
					// determine who makes the first move and send ack
					int firstPlayerId = random.nextInt(2) + 1;
					ack.clientPlayerGoesFirst = (firstPlayerId == Player.PLAYER_1) ? false : true;
					IdPlayerWhoGoesFirst = firstPlayerId;
					
					c.sendTCP(ack);
					setTurn(!ack.clientPlayerGoesFirst);
					logThisMessage("Server goes first: "+isThisPlayerTurn);
					logThisMessage("SERVER SENT ACK TO JOIN GAME");
				}
				
				if(object instanceof Place) {
					Place place = (Place)object;
					if(setPiece(place.boardIndex, place.playerId)) {
						if(!madeAMill(place.boardIndex, place.playerId)) {
							setTurn(true);
						}
					} else {
						logThisMessage("INVALID PLACE FROM THE CLIENT");
						System.exit(-1); // TODO what to do in this situation? I think it indicates a problem of synchronization
					}
				}
				
				if(object instanceof Remove) {
					Remove remove = (Remove)object;
					if(removePiece(remove.boardIndex, player.getPlayerId())) {
						setTurn(true);
					} else {
						logThisMessage("INVALID REMOVE FROM THE CLIENT");
						System.exit(-1); // TODO what to do in this situation? I think it indicates a problem of synchronization
					}
					
				}
				
				if(object instanceof Move) {
					Move move = (Move)object;
					movePieceFromTo(move.srcIndex, move.destIndex, move.playerId);
					if(!madeAMill(move.destIndex, move.playerId)) {
						setTurn(true);
					}
				}
				
				if(object instanceof GameOver) {
					logThisMessage("You've won! Congrats.");
					System.exit(-1); // TODO what to do here?
				}
			}
			
			public void disconnected (Connection c) {
				logThisMessage("CLIENT DISCONNECTED");
				server.stop();
				System.exit(-1);
			}
		});
		server.bind(NetworkGame.TPC_PORT);
		server.start();
	}
	
	// This holds per connection state.
    static class GameConnection extends Connection {
    	int playerId;
    	String playerName;
    	public GameConnection(int pId, String pName) {
    		playerId = pId;
    		playerName = pName;
    	}
    }

	@Override
	public boolean setPiece(int boardIndex) {
		if(setPiece(boardIndex, player.getPlayerId())) {
			Place place = new Place();
			place.boardIndex = boardIndex;
			place.playerId = player.getPlayerId();
			server.sendToAllTCP(place);
			return true;
		}
		return false;
	}

	@Override
	public boolean removePiece(int boardIndex) {
		if(removePiece(boardIndex, Player.PLAYER_2)) {
			Remove remove = new Remove();
			remove.boardIndex = boardIndex;
			remove.playerId = player.getPlayerId();
			server.sendToAllTCP(remove);
			return true;
		}
		return false;
	}

	@Override
	public void sendGameOver() {
		GameOver gameOver = new GameOver();
		server.sendToAllTCP(gameOver);
	}

	@Override
	public void movePieceFromTo(int src, int dest) {
		movePieceFromTo(src, dest, player.getPlayerId());
		Move move = new Move();
		move.srcIndex = src;
		move.destIndex = dest;
		move.playerId = player.getPlayerId();
		server.sendToAllTCP(move);
	}
}
