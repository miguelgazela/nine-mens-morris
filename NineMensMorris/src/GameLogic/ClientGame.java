package GameLogic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

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
					if(otherPlayerName != null) {
						return;
					}
					otherPlayerName = ((JoinAck)object).nameofServerPlayer;
					logThisMessage("CLIENT RECEIVED ACK TO JOIN GAME");
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
		
		// Open a window to prevent the client from stopping immediately.
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
                public void windowClosed (WindowEvent evt) {
                        client.stop();
                }
        });
        frame.getContentPane().add(new JLabel("Close to stop the client."));
        frame.setSize(220, 60);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
}
