package GameLogic;

import GameLogic.NetworkGame.GameOver;
import GameLogic.NetworkGame.Move;
import GameLogic.NetworkGame.Place;
import GameLogic.NetworkGame.Remove;

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
		});
	}
}
