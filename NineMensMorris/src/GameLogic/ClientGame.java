package GameLogic;

import java.io.IOException;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientGame extends NetworkGame {
	Client client;
	
	public ClientGame() {
		client = new Client();
		client.start();
		NetworkGame.register(client);
		
		client.addListener(new Listener() {
			public void received(Connection c, Object object) {
				
				if(object instanceof JoinAck) {
					if(connectionEstablished) { //ignore if player is already connected to a game
						return;
					}
					otherSidePlayerName = ((JoinAck)object).nameofServerPlayer;
					connectionEstablished = true;
					setTurn(((JoinAck)object).clientPlayerGoesFirst);
					IdPlayerWhoGoesFirst = isThisPlayerTurn ? player.getPlayerId() : Player.PLAYER_2;
					logThisMessage("CLIENT RECEIVED ACK TO JOIN GAME");
				}
				
				if(object instanceof Place) {
					Place place = (Place)object;
					if(setPiece(place.boardIndex, place.playerId)) {
						if(!madeAMill(place.boardIndex, place.playerId)) {
							setTurn(true);
						}
					} else {
						logThisMessage("INVALID PLACE FROM THE SERVER");
						System.exit(-1); // TODO what to do in this situation? I think it indicates a problem of synchronization
					}
				}
				
				if(object instanceof Remove) {
					Remove remove = (Remove)object;
					if(removePiece(remove.boardIndex, player.getPlayerId())) {
						setTurn(true);
					} else {
						logThisMessage("INVALID REMOVE FROM THE SERVER");
						System.exit(-1); // TODO what to do in this situation? I think it indicates a problem of synchronization
					}
				}

				if(object instanceof Move) {
					Move move = (Move)object;
					movePieceFromTo(move.srcIndex, move.destIndex, move.playerId);
				}
				
				if(object instanceof GameOver) {
					logThisMessage("You've won! Congrats.");
					System.exit(-1); // TODO what to do here?
				}
			}
			
			public void connected(Connection connection) {
				logThisMessage("CLIENT CONNECTED TO SERVER");
				JoinGame request = new JoinGame();
				request.nameOfClientPlayer = player.getName();
				client.sendTCP(request);
				logThisMessage("CLIENT SENT A REQUEST TO JOIN GAME TO SERVER"); 
			}
		});
	}
	
	public void connectToServer(String host) throws IOException {
		client.connect(5000, host, NetworkGame.TPC_PORT);
	}

	@Override
	public boolean setPiece(int boardIndex) {
		if(setPiece(boardIndex, player.getPlayerId())) {
			Place place = new Place();
			place.boardIndex = boardIndex;
			place.playerId = player.getPlayerId();
			client.sendTCP(place);
			return true;
		}
		return false;
	}

	@Override
	public boolean removePiece(int boardIndex) {
		if(removePiece(boardIndex, Player.PLAYER_1)) {
			Remove remove = new Remove();
			remove.boardIndex = boardIndex;
			remove.playerId = player.getPlayerId();
			client.sendTCP(remove);
			return true;
		}
		return false;
	}
	
	@Override
	public void sendGameOver() {
		GameOver gameOver = new GameOver();
		client.sendTCP(gameOver);
	}
	
	@Override
	public void movePieceFromTo(int src, int dest) {
		movePieceFromTo(src, dest, player.getPlayerId());
		Move move = new Move();
		move.srcIndex = src;
		move.destIndex = dest;
		move.playerId = player.getPlayerId();
		client.sendTCP(move);
	}
}
