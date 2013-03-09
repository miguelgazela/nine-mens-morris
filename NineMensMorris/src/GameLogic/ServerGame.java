package GameLogic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerGame extends NetworkGame {
	private Server server;
	
	public ServerGame() throws IOException {
		super();
		server = new Server() {
			protected Connection newConnection() {
				return new GameConnection(player.getPlayerId(),player.getName());
			}
		};
		NetworkGame.register(server);
		
		server.addListener(new Listener() {
			public void received(Connection c, Object object) {
				
				if(object instanceof JoinGame) {
					if(otherPlayerName != null) { //ignore if player is already connected
						return;
					}
					otherPlayerName = ((JoinGame)object).nameOfClientPlayer; 
					logThisMessage("SERVER RECEIVED REQUEST TO JOIN GAME FROM "+((JoinGame)object).nameOfClientPlayer);
					JoinAck ack = new JoinAck();
					ack.nameofServerPlayer = player.getName();
					c.sendTCP(ack);
					logThisMessage("SERVER SENT ACK TO JOIN GAME");
				}
				
				if(object instanceof Place) {
					
				}
				
				if(object instanceof Remove) {
					
				}
				
				if(object instanceof Move) {

				}
				
				if(object instanceof GameOver) {

				}
			}
			
			public void disconnected (Connection c) {
				logThisMessage("CLIENT DISCONNECTED");
				server.stop();
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
}
