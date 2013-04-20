package GameLogic;

import java.io.IOException;
import java.util.Random;

import org.objenesis.instantiator.basic.NewInstanceInstantiator;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer extends Network {
	private Server server;
	private Random random;
	private short numberOfConnectedGameClients;
	private Board gameBoard;
	private int gamePhase;
	private Token currentPlayer;
	
	public GameServer() throws IOException {
		super();
		numberOfConnectedGameClients = 0;
		currentPlayer = Token.NO_PLAYER;
		gameBoard = new Board();
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
					
					// validate move
					try {
						if(setPiece(((PiecePlacing)object).player, ((PiecePlacing)object).boardIndex)) {
							actionValidation.validAction = true;
						} else {
							actionValidation.validAction = false;
						}
					} catch (GameException e) {
						e.printStackTrace();
					}
					c.sendTCP(actionValidation);
				}

				/*
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
				*/
			}
			
			public void disconnected (Connection c) {
				logThisMessage("CLIENT HAS DISCONNECTED, STOPING SERVER!");
				server.stop();
				System.exit(-1);
			}
		});
		server.bind(Network.TPC_PORT);
		server.start();
	}
	
	// This holds per connection state.
    static class GameConnection extends Connection {
    	public GameConnection() {
    		// TODO add something here?
    	}
    }

	public boolean setPiece(Token player, int boardIndex) throws GameException {
		if(player == currentPlayer) {
			if(gameBoard.positionIsAvailable(boardIndex)) {
				gameBoard.getPosition(boardIndex).setAsOccupied(player);
				gameBoard.incNumPiecesOfPlayer(player);
				if(gameBoard.incNumTotalPiecesPlaced() == (Game.NUM_PIECES_PER_PLAYER * 2)) {
					gamePhase = Game.MOVING_PHASE;
				}
				return true;
			}
		}
		return false;
	}

	/*
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
	*/
}
