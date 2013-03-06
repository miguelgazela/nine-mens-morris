package GameLogic;

import java.io.IOException;
import java.net.InetAddress;

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
			
			public void connected(Connection connection) {
				System.out.println("CLIENT CONNECTED TO SERVER");
				JoinGame request = new JoinGame();
				request.nameOfClientPlayer = player.getName();
				client.sendTCP(request);
			}
		});
	}
	
	public void connectToServer(String host) throws IOException {
		client.connect(5000, host, NetworkGame.TPC_PORT);
	}
}
