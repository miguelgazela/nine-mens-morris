package GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import com.esotericsoftware.minlog.Log;

import GameUI.UIGameMenu;
import aurelienribon.slidinglayout.SLAnimator;

public class Main {
	public Game game;
	public BufferedReader input;
	
	public static void main(String []args) throws Exception {
		
		//SLAnimator.start();
		//new UIGameMenu();
		
		
		System.out.println("Nine Men's Morris starting...");
		Log.set(Log.LEVEL_INFO);
		Main maingame = new Main();
		maingame.input = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("(L)OCAL or (N)ETWORK?");
		String userInput = maingame.input.readLine();
		userInput = userInput.toUpperCase();
		
		if(userInput.compareTo("LOCAL") == 0 || userInput.compareTo("L") == 0) {
			maingame.createLocalGame(4);
		} else if(userInput.compareTo("NETWORK") == 0 || userInput.compareTo("N") == 0) {
			maingame.createNetworkGame();
		} else {
			System.out.println("UNKNOWN COMMAND");
			System.exit(-1);
		}
		
	}
	
	public void createLocalGame(int minimaxDepth) throws IOException, GameException {
		game = new LocalGame();
		System.out.println("Player 1: (H)UMAN or (C)PU?");
		String userInput = input.readLine();
		userInput = userInput.toUpperCase();
		Player p1 = null, p2 = null;
		
		if(userInput.compareTo("HUMAN") == 0 || userInput.compareTo("H") == 0) {
			p1 = new HumanPlayer("Miguel", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
		} else if(userInput.compareTo("CPU") == 0 || userInput.compareTo("C") == 0) {
			p1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, minimaxDepth);
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		System.out.println("Player 2: (H)UMAN or (C)PU?");
		userInput = input.readLine();
		userInput = userInput.toUpperCase();
		
		if(userInput.compareTo("HUMAN") == 0 || userInput.compareTo("H") == 0) {
			p2 = new HumanPlayer("Miguel", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
		} else if(userInput.compareTo("CPU") == 0 || userInput.compareTo("C") == 0) {
			p2 = new MinimaxIAPlayer(Token.PLAYER_2,Game.NUM_PIECES_PER_PLAYER, minimaxDepth);
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		((LocalGame)game).setPlayers(p1, p2);
		while(game.getCurrentGamePhase() == Game.PLACING_PHASE) {
			
			while(true) {
				Player p = ((LocalGame)game).getCurrentTurnPlayer();
				int boardIndex;
				
				if(p.isAI()) {
					long startTime = System.nanoTime();
					boardIndex = ((MinimaxIAPlayer)p).getIndexToPlacePiece(game.gameBoard);
					long endTime = System.nanoTime();
					game.printGameBoard();
					System.out.println("Number of moves: "+((MinimaxIAPlayer)p).numberOfMoves);
					System.out.println("Moves that removed: "+((MinimaxIAPlayer)p).movesThatRemove);
					System.out.println("It took: "+ (endTime - startTime)/1000000+" miliseconds");
					System.out.println(p.getName()+" placed piece on "+boardIndex);
					
				} else {
					game.printGameBoard();
					System.out.println(p.getName()+" place piece on: ");
					userInput = input.readLine();
					userInput = userInput.toUpperCase();
					boardIndex = Integer.parseInt(userInput);
				}
				
				if(game.placePieceOfPlayer(boardIndex, p.getPlayerToken())) {
					p.raiseNumPiecesOnBoard(); // TODO the game should do this
					
					if(game.madeAMill(boardIndex, p.getPlayerToken())) {
						Token opponentPlayer = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
						
						while(true) {
							if(p.isAI()){
								boardIndex = ((MinimaxIAPlayer)p).getIndexToRemovePieceOfOpponent(game.gameBoard);
								System.out.println(p.getName()+" removes opponent piece on "+boardIndex);
							} else {
								System.out.println("You made a mill. You can remove a piece of your oponent: ");
								userInput = input.readLine();
								userInput = userInput.toUpperCase();
								boardIndex = Integer.parseInt(userInput);
							}
							if(game.removePiece(boardIndex, opponentPlayer)) {
								break;
							} else {
								System.out.println("You can't remove a piece from there. Try again");
							}
						}
					}
					((LocalGame)game).updateCurrentTurnPlayer();
					break;
				} else {
					System.out.println("You can't place a piece there. Try again");
				}
			}
		}
		
		System.out.println("The pieces are all placed. Starting the fun part...");
		while(!game.isTheGameOver()) {
			
			while(true) {
				Player p = ((LocalGame)game).getCurrentTurnPlayer();
				int srcIndex, destIndex;
				Move move = null;
				
				if(p.isAI()) {
					long startTime = System.nanoTime();
					move = ((IAPlayer)p).getPieceMove(game.gameBoard, game.getCurrentGamePhase());
					long endTime = System.nanoTime();
					System.out.println("Number of moves: "+((MinimaxIAPlayer)p).numberOfMoves);
					System.out.println("Moves that removed: "+((MinimaxIAPlayer)p).movesThatRemove);
					System.out.println("It took: "+ (endTime - startTime)/1000000+" miliseconds");
					srcIndex = move.srcIndex;
					destIndex = move.destIndex;
					System.out.println(p.getName()+" moved piece from "+srcIndex+" to "+destIndex);
				} else {
					game.printGameBoard();
					System.out.println(p.getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
					userInput = input.readLine();
					userInput = userInput.toUpperCase();
					String[] positions = userInput.split(":");
					srcIndex = Integer.parseInt(positions[0]);
					destIndex = Integer.parseInt(positions[1]);
					System.out.println("Move piece from "+srcIndex+" to "+destIndex);
				}
				
				int result;
				if((result = game.movePieceFromTo(srcIndex, destIndex, p.getPlayerToken())) == Game.VALID_MOVE) {
					if(game.madeAMill(destIndex, p.getPlayerToken())) {
						Token opponentPlayerToken = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
						int boardIndex;
						
						while(true) {
							if(p.isAI()){
								boardIndex = move.removePieceOnIndex;
								System.out.println(p.getName()+" removes opponent piece on "+boardIndex);
							} else {
								System.out.println("You made a mill! You can remove a piece of your oponent: ");
								userInput = input.readLine();
								userInput = userInput.toUpperCase();
								boardIndex = Integer.parseInt(userInput);
							}
							if(game.removePiece(boardIndex, opponentPlayerToken)) {
								break;
							} else {
								System.out.println("It couldn't be done! Try again.");
							}
						}
					}
					
					
					if(game.isTheGameOver()) {
						game.printGameBoard();
						break;
					}
					((LocalGame)game).updateCurrentTurnPlayer();
				} else {
					System.out.println("Invalid move. Error code: "+result);
				}
			}
		}
	}
	
	public void createNetworkGame() throws IOException, InterruptedException, GameException {
		System.out.println("(S)ERVER or (C)LIENT?");
		String userInput = input.readLine();
		userInput = userInput.toUpperCase();
		
		NetworkGame game = new NetworkGame();
		GameServer gs = null;
		GameClient gc = null;
		
		if(userInput.compareTo("SERVER") == 0 || userInput.compareTo("S") == 0) {
			gs = new GameServer();
			gc = new GameClient(Token.PLAYER_1);
			
			// display IP addresses
			Enumeration<NetworkInterface> nets;
	        try {
	            nets = NetworkInterface.getNetworkInterfaces();
	            for (NetworkInterface netint : Collections.list(nets)) {
	                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	                    Log.info(inetAddress.toString());
	                }
	            }
	        } catch (SocketException e) { e.printStackTrace(); }
	        
		} else if(userInput.compareTo("CLIENT") == 0 || userInput.compareTo("C") == 0) {
			gc = new GameClient(Token.PLAYER_2);
		} else {
			Log.error("Unknown command. Closing program.");
			System.exit(-1);
		}
		
		Player p = null;
		
		// the player with the server is always PLAYER_1 (not necessarily the first one to play)
		if(gs != null) {
			p = new HumanPlayer("Miguel",Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
		} else {
			p = new HumanPlayer("Aida",Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER);
		}

		game.setPlayer(p);
		int numberTries = 3;			
	
		if(gs == null) { // this is only a client trying to connect
			while(true) {
				try {
					System.out.println("Connect to GameServer at IP address: ");
					userInput = input.readLine();
					gc.connectToServer(userInput);
					break;
				} catch (Exception e) {
					Log.info("No GameServer detected!");
					if(--numberTries == 0) {
						System.exit(-1);
					}
					Thread.sleep(6000);
					Log.info("Trying another connection.");
				}
			}
		} else { // this computer has the GameServer
			boolean firstTry = true;
			gc.connectToServer("localhost");
			
			while(true) {
				if(gs.hasConnectionEstablished()) {
					break;
				}
				if(firstTry) {
					Log.info("Server initialized. Waiting for connection from GameClient of this computer");
					firstTry = false;
				}
				Thread.sleep(100);
			}
		}

		while(gc.isWaitingForGameStart()) {
			System.out.println("Waiting for game to start");
			Thread.sleep(1000);
		}
		
		System.out.println("GAME IS ON!!!");
		if(gc.getPlayerThatPlaysFirst() == p.getPlayerToken()) {
			System.out.println("I'M THE ONE WHO PLAYS FIRST!");
			game.setTurn(true);
		}
		
		while(true) {
			if(game.isThisPlayerTurn()) {
				int boardIndex;
				Player player = game.getPlayer();
				
				// update game with opponent move(s)
				ArrayList<Move> opponentMoves = gc.getOpponentMoves();
				game.updateGameWithOpponentMoves(opponentMoves);
				
				// check if the other player played the last piece of the placing phase
				if(game.getCurrentGamePhase() != Game.PLACING_PHASE) {
					break;
				}
				
				// ask user input
				game.printGameBoard();
				System.out.println(player.getName()+" place piece on: ");
				boardIndex = Integer.parseInt(input.readLine());
				
				// validate placing with the server
				if(gc.validatePiecePlacing(boardIndex)) {
					
					// validate placing locally
					if(game.placePieceOfPlayer(boardIndex, player.getPlayerToken())) {
						
						if(game.madeAMill(boardIndex, player.getPlayerToken())) {
							while(true) {
								// ask for the index of the opponent piece
								System.out.println("You made a mill. You can remove a piece of your oponent. Remove piece at: ");
								boardIndex = Integer.parseInt(input.readLine());
								
								// validate removing with the server
								if(gc.validatePieceRemoving(boardIndex)) {
									
									// validate removing locally
									if(game.removePiece(boardIndex, (player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1))) {
										break;
									} else {
										System.out.println("You can't remove a piece from there. Try again");
									}
								}
							}
						}
						game.setTurn(false);
					}
				} else {
					System.out.println("The server has considered that move invalid. Try again");
				}
			}
			Thread.sleep(10);
			game.setTurn(gc.isThisPlayerTurn());
		}

		System.out.println("The pieces are all placed. Starting the fun part...");
		
		// getting the right player to make the first move
		if(game.playedFirst(gc.getPlayerThatPlaysFirst())) {
			game.setTurn(true);
		}
		
		while(!game.isTheGameOver()) {
			while(true) {
				if(game.isThisPlayerTurn()) {
					Player player = game.getPlayer();
					int srcIndex, destIndex;
					
					// update game with opponent move(s)
					ArrayList<Move> opponentMoves = gc.getOpponentMoves();
					game.updateGameWithOpponentMoves(opponentMoves);
					
					// ask user input
					game.printGameBoard();
					System.out.println(player.getName()+" it's your turn. Input PIECE_SRC_INDEX:PIECE_DEST_INDEX");
					String[] positions = input.readLine().split(":");
					srcIndex = Integer.parseInt(positions[0]);
					destIndex = Integer.parseInt(positions[1]);
					
					// validate move, locally and with the server
					int result;
					if(gc.validatePieceMoving(srcIndex, destIndex)) {
						if((result = game.movePieceFromTo(srcIndex, destIndex, player.getPlayerToken())) == Game.VALID_MOVE) {

							if(game.madeAMill(destIndex, player.getPlayerToken())) {
								while(true) {
									// ask for the index of the opponent piece
									System.out.println("You made a mill. You can remove a piece of your oponent. Remove piece at: ");
									int boardIndex = Integer.parseInt(input.readLine());

									// validate removing with the server
									if(gc.validatePieceRemoving(boardIndex)) {

										// validate removing locally
										if(game.removePiece(boardIndex, (player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1))) {
											break;
										} else {
											System.out.println("You can't remove a piece from there. Try again");
										}
									}
								}
							}
							// TODO game.checkGameIsOver();
							game.setTurn(false);
							break;
						} else {
							System.out.println("Invalid move. Error code: "+result);
						}
					}
				}
				Thread.sleep(10);
				game.setTurn(gc.isThisPlayerTurn());
			}
		}
	}
}
