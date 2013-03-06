package GameLogic;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerGame extends NetworkGame {
	private Server server;
	
	public ServerGame() throws IOException {
		super();
		server = new Server() {
			protected Connection newConnection() {
				return new GameConnection();
			}
		};
		NetworkGame.register(server);
		server.addListener(new Listener() {
			public void received(Connection c, Object object) {				
				if(object instanceof JoinGame) {
					//ignore if connection is already established
					if(connectionEstablished) {
						return;
					}
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
				System.out.println("CLIENT DISCONNECTED");
			}
		});
		server.bind(NetworkGame.port);
		server.start();
	}
	
	// This holds per connection state.
    static class GameConnection extends Connection {
    	
    }
}
