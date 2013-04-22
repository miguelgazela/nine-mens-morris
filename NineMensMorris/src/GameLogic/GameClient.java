package GameLogic;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.border.Border;

import GameLogic.Network.PiecePlacing;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class GameClient extends Network {
	private Client client;
	private Token playerToken;
	private Token playerThatPlaysFirst;
	private boolean waitingForGameToStart;
	private boolean joinAcknowledged;
	private boolean waitingForServerResponse;
	private boolean responseFromServer;
	private boolean thisPlayerTurn;
	private ArrayList<Move> opponentMoves;
	
	public GameClient(Token player) throws GameException {
		
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException(""+getClass().getName()+" - Invalid Player Token");
		}
		
		waitingForGameToStart = true;
		waitingForServerResponse = false;
		joinAcknowledged = false;
		responseFromServer = false;
		thisPlayerTurn = false;
		playerThatPlaysFirst = Token.NO_PLAYER;
		playerToken = player;
		opponentMoves = new ArrayList<Move>();
		
		client = new Client();
		client.start();
		Network.register(client);
		
		client.addListener(new Listener() {
			public void received(Connection c, Object object) {
				
				if(object instanceof JoinAck) {
					if(joinAcknowledged) { // ignore if GameClient has already joined a game
						return;
					}
					joinAcknowledged = true;
					Log.info("GameClient join request was acknowledged by GameServer. Waiting for other player.");
					//logThisMessage("GAMECLIENT RECEIVED ACK TO JOIN GAME FROM GAMESERVER. WAITING FOR GAME TO START");
				}

				if(object instanceof FullServer) {
					logThisMessage("CANNOT JOIN GAME, SERVER IS FULL");
					System.exit(-1); // TODO exit or warn GUI?
				}
				
				if(object instanceof StartGame) {
					logThisMessage("GAMECLIENT HAS RECEIVED CONFIRMATION FOR GAME START FROM THE SERVER");
					waitingForGameToStart = false;
					playerThatPlaysFirst = ((StartGame)object).playerWhoPlaysFirst;
					thisPlayerTurn = (playerToken == playerThatPlaysFirst);
				}
				
				if(object instanceof ActionValidation) {
					logThisMessage("RECEIVED ACTION VALIDATION");
					responseFromServer = ((ActionValidation)object).validAction;
					waitingForServerResponse = false;
				}
				
				if(object instanceof PiecePlacing) {
					Token player = ((PiecePlacing)object).player;
					int boardIndex = ((PiecePlacing)object).boardIndex;
					
					// if it's a move from the opponent, add it to the list
					if(player != playerToken) {
						try {
							opponentMoves.add(new Move(-1, boardIndex, -1, Move.PLACING));
						} catch (GameException e) { e.printStackTrace(); }
						logThisMessage("RECEIVED PIECE PLACING FROM OPPONENT");
					}
				}
				
				if(object instanceof PieceRemoving) {
					Token player = ((PieceRemoving)object).player;
					int boardIndex = ((PieceRemoving)object).boardIndex;
					
					// if it's a move from the opponent, add it to the list
					if(player == playerToken) {
						try {
							opponentMoves.add(new Move(-1, -1, boardIndex, Move.REMOVING));
						} catch (GameException e) { e.printStackTrace(); }
						logThisMessage("RECEIVED PIECE REMOVING FROM OPPONENT");
					}
				}
				
				if(object instanceof PieceMoving) {
					Token player = ((PieceMoving)object).player;
					int srcIndex = ((PieceMoving)object).srcIndex;
					int destIndex = ((PieceMoving)object).destIndex;
					
					// if it's a move from the opponent, add it to the list
					if(player != playerToken) {
						try {
							opponentMoves.add(new Move(srcIndex, destIndex, -1, Move.MOVING));
						} catch (GameException e) { e.printStackTrace(); }
						logThisMessage("RECEIVED PIECE MOVING FROM OPPONENT");
					}
				}
				
				if(object instanceof ThisPlayerTurn) {
					thisPlayerTurn = (playerToken == ((ThisPlayerTurn)object).player);
					if(thisPlayerTurn) {
						logThisMessage("IT'S MY TURN NOW!");
					}
				}
				
				/*
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
				*/
			}
			
			public void connected(Connection connection) {
				logThisMessage("THIS CLIENT IS CONNECTED TO SERVER");
				connectionEstablished = true;
				
				// sending join game request
				JoinGame request = new JoinGame();
				request.playerToken = playerToken;
				client.sendTCP(request);
				
				logThisMessage("CLIENT SENT A REQUEST TO JOIN GAME TO SERVER"); 
			}
		});
	}
	
	public void connectToServer(String host) throws IOException {
		client.connect(5000, host, Network.TPC_PORT);
	}
	
	public boolean isWaitingForGameStart() {
		return waitingForGameToStart;
	}
	
	public boolean isThisPlayerTurn() {
		return thisPlayerTurn;
	}
	
	public Token getPlayerThatPlaysFirst() {
		return playerThatPlaysFirst;
	}

	/**
	 * 
	 * @param boardIndex
	 * @return
	 */
	public boolean validatePiecePlacing(int boardIndex) {
		PiecePlacing piecePlacing = new PiecePlacing();
		piecePlacing.player = playerToken;
		piecePlacing.boardIndex = boardIndex;
		
		waitingForServerResponse = true;
		client.sendTCP(piecePlacing);
		waitForServerResponse();
		
		boolean temp = responseFromServer;
		responseFromServer = false;
		return temp;
	}
	
	/**
	 * 
	 * @param boardIndex
	 * @return
	 */
	public boolean validatePieceRemoving(int boardIndex) {
		PieceRemoving pieceRemoving = new PieceRemoving();
		pieceRemoving.player = (playerToken == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
		pieceRemoving.boardIndex = boardIndex;
		
		waitingForServerResponse = true;
		client.sendTCP(pieceRemoving);
		waitForServerResponse();
		
		boolean temp = responseFromServer;
		responseFromServer = false;
		return temp;
	}
	
	/**
	 * 
	 * @param srcIndex
	 * @param destIndex
	 * @return
	 */
	public boolean validatePieceMoving(int srcIndex, int destIndex) {
		PieceMoving pieceMoving = new PieceMoving();
		pieceMoving.player = playerToken;
		pieceMoving.srcIndex = srcIndex;
		pieceMoving.destIndex = destIndex;
		
		waitingForServerResponse = true;
		client.sendTCP(pieceMoving);
		waitForServerResponse();
		
		boolean temp = responseFromServer;
		responseFromServer = false;
		return temp;
	}
	
	/**
	 * 
	 */
	private void waitForServerResponse() {
		while(waitingForServerResponse) {
			logThisMessage("GAMECLIENT WAITING FOR VALIDATION FROM GAMESERVER");
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Move> getOpponentMoves() {
		return opponentMoves;
	}

	/*
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
	
	public void movePieceFromTo(int src, int dest) {
		movePieceFromTo(src, dest, player.getPlayerId());
		Move move = new Move();
		move.srcIndex = src;
		move.destIndex = dest;
		move.playerId = player.getPlayerId();
		client.sendTCP(move);
	}
	
	public abstract void sendGameOver();
	public abstract boolean setPiece(int boardIndex);
	public abstract boolean removePiece(int boardIndex);
	public abstract void movePieceFromTo(int src, int dest);
	*/
}
