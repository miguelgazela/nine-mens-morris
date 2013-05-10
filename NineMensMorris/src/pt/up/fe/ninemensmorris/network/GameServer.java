package pt.up.fe.ninemensmorris.network;

import java.io.IOException;
import java.util.Random;

import pt.up.fe.ninemensmorris.logic.Game;
import pt.up.fe.ninemensmorris.logic.GameException;
import pt.up.fe.ninemensmorris.logic.Token;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer extends Network {
	private Server server;
	private Random random;
	private short numberOfConnectedGameClients;
	private Game validationGame;
	private Token currentPlayer;
	
	public GameServer() throws IOException {
		super();
		numberOfConnectedGameClients = 0;
		currentPlayer = Token.NO_PLAYER;
		validationGame = new Game();
		random = new Random();
		server = new Server() {
			protected Connection newConnection() {
				return new GameConnection();
			}
		};
		Network.register(server);
		
		server.addListener(new Listener() {
			public void received(Connection c, Object object) {
				
				if(object instanceof JoinGame) {
					if(connectionEstablished) { // ignore if there are already 2 GameClient connected
						logThisMessage("SERVER RECEIVED REQUEST TO JOIN GAME FROM A THIRD PLAYER. SENDING SERVER IS FULL ANSWER.");
						c.sendTCP(new FullServer());
						return;
					}
					
					// send an ackowledge for this GameClient request
					logThisMessage("SERVER RECEIVED REQUEST TO JOIN GAME FROM "+((JoinGame)object).playerToken+ " "+c.getRemoteAddressTCP());
					JoinAck ack = new JoinAck();
					ack.playerAcknowledged = ((JoinGame)object).playerToken;
					c.sendTCP(ack);
					
					// if there's 2 GameClient connected, start the game
					if(++numberOfConnectedGameClients == 2) {
						connectionEstablished = true;
						StartGame startGame = new StartGame();
						
						// determine who makes the first move
						short firstPlayer = (short) (random.nextInt(2) + 1);
						startGame.playerWhoPlaysFirst = (firstPlayer == 1 ? Token.PLAYER_1 : Token.PLAYER_2);
						currentPlayer = startGame.playerWhoPlaysFirst;
						
						server.sendToAllTCP(startGame); // warn both players
						
						logThisMessage("SERVER SENT START GAME WARNING. FIRST PLAYER IS "+ (startGame.playerWhoPlaysFirst == Token.PLAYER_1 ? "PLAYER 1" : "PLAYER 2"));
					}
				}
				
				if(object instanceof PiecePlacing) {
					ActionValidation actionValidation = new ActionValidation();
					Token player = ((PiecePlacing)object).player;
					int boardIndex = ((PiecePlacing)object).boardIndex;
					logThisMessage("SERVER RECEIVED A PIECE PLACING FROM PLAYER "+player);

					// validate move
					try {
						actionValidation.validAction = false;
						
						if(currentPlayer == player) {
							if(validationGame.placePieceOfPlayer(boardIndex, player)) {
								actionValidation.validAction = true;
								server.sendToAllTCP(object);
								
								logThisMessage("SERVER VALIDATED A PIECE PLACING FROM PLAYER "+player);

								if(!validationGame.madeAMill(boardIndex, player)) {
									logThisMessage("PLAYER DIDN'T MAKE A MILL WITH THE PREVIOUS MOVE");
									updateCurrentPlayer();
								}
							}
						}
					} catch (GameException e) { e.printStackTrace(); }

					c.sendTCP(actionValidation);
				}
				
				if(object instanceof PieceRemoving) {
					ActionValidation actionValidation = new ActionValidation();
					Token player = ((PieceRemoving)object).player;
					int boardIndex = ((PieceRemoving)object).boardIndex;
					
					// validate move
					try {
						actionValidation.validAction = false;

						if(currentPlayer != player) {
							if(validationGame.removePiece(boardIndex, player)) {
								actionValidation.validAction = true;
								server.sendToAllTCP(object);
								updateCurrentPlayer();
								
								logThisMessage("SERVER VALIDATED A REMOVE PIECE OF PLAYER "+player+" FROM INDEX "+boardIndex);
							}
						}
					} catch (GameException e) { e.printStackTrace(); }
					
					c.sendTCP(actionValidation);
				}
				
				if(object instanceof PieceMoving) {
					ActionValidation actionValidation = new ActionValidation();
					Token player = ((PieceMoving)object).player;
					int srcIndex = ((PieceMoving)object).srcIndex;
					int destIndex = ((PieceMoving)object).destIndex;
					
					// validate move
					try {
						actionValidation.validAction = false;
						
						if(currentPlayer == player) {
							if(validationGame.movePieceFromTo(srcIndex, destIndex, player) == Game.VALID_MOVE) {
								actionValidation.validAction = true;
								server.sendToAllTCP(object);
								
								logThisMessage("SERVER VALIDATED A PIECE MOVING FROM PLAYER "+player);

								if(!validationGame.madeAMill(destIndex, player)) {
									logThisMessage("PLAYER DIDN'T MAKE A MILL WITH THE PREVIOUS MOVE");
									updateCurrentPlayer();
								}
							}
						}
					} catch (GameException e) { e.printStackTrace(); }
					
					c.sendTCP(actionValidation);
				}
			}
			
			public void disconnected (Connection c) {
				logThisMessage("CLIENT HAS DISCONNECTED, STOPING SERVER!");
				server.stop();
				//System.exit(-1);
			}
		});
		server.bind(Network.TPC_PORT);
		server.start();
	}
	
	public void stop() {
		server.stop();
	}
	
	// This holds per connection state.
    static class GameConnection extends Connection {
    	public GameConnection() {
    		// TODO add something here?
    	}
    }
    
    private void updateCurrentPlayer() {
    	currentPlayer = (currentPlayer == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
    	ThisPlayerTurn tpt = new ThisPlayerTurn();
    	tpt.player = currentPlayer;
    	server.sendToAllTCP(tpt);
    }
}
