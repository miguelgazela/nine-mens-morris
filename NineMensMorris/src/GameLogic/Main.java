package GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import GameLogic.IAPlayer.Move;
import GameUI.UIGameMenu;
import aurelienribon.slidinglayout.SLAnimator;

public class Main {
	public Game game;
	public BufferedReader input;
	
	public static void main(String []args) throws Exception {
		int minimaxDepth = -1;
		
		if(args.length == 0 || args.length > 1) {
			System.out.println("Usage: java Main <minimaxDepth>");
			System.exit(-1);
		} else {
			try {
				minimaxDepth = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Invalid depth");
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		/*
		SLAnimator.start();
		UIGameMenu uiGameMenu = new UIGameMenu();
		*/
		
		System.out.println("Nine Men's Morris starting...");
		Main maingame = new Main();
		maingame.input = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("(L)OCAL or (N)ETWORK?");
		String userInput = maingame.input.readLine();
		userInput = userInput.toUpperCase();
		
		if(userInput.compareTo("LOCAL") == 0 || userInput.compareTo("L") == 0) {
			maingame.createLocalGame(minimaxDepth);
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
			p1 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, minimaxDepth);
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
				
				if(p.isIA()) {
					long startTime = System.nanoTime();
					boardIndex = ((MinimaxIAPlayer)p).getIndexToPlacePiece(game.gameBoard);
					long endTime = System.nanoTime();
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
							if(p.isIA()){
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
		while(!game.gameIsOver()) {
			
			while(true) {
				Player p = ((LocalGame)game).getCurrentTurnPlayer();
				int initialIndex, finalIndex;
				Move move = null;
				
				if(p.isIA()) {
					long startTime = System.nanoTime();
					move = ((IAPlayer)p).getPieceMove(game.gameBoard, game.getCurrentGamePhase());
					long endTime = System.nanoTime();
					System.out.println("Number of moves: "+((MinimaxIAPlayer)p).numberOfMoves);
					System.out.println("Moves that removed: "+((MinimaxIAPlayer)p).movesThatRemove);
					System.out.println("It took: "+ (endTime - startTime)/1000000+" miliseconds");
					initialIndex = move.src;
					finalIndex = move.dest;
					System.out.println(p.getName()+" moved piece from "+initialIndex+" to "+finalIndex);
				} else {
					game.printGameBoard();
					System.out.println(p.getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
					userInput = input.readLine();
					userInput = userInput.toUpperCase();
					String[] positions = userInput.split(":");
					initialIndex = Integer.parseInt(positions[0]);
					finalIndex = Integer.parseInt(positions[1]);
					System.out.println("Move piece from "+initialIndex+" to "+finalIndex);
				}
				if(game.positionHasPieceOfPlayer(initialIndex, p.getPlayerToken())) {
					
					if(game.positionIsAvailable(finalIndex) && (game.validMove(initialIndex, finalIndex) || p.canItFly())) {
						game.movePieceFromTo(initialIndex, finalIndex, p.getPlayerToken());
						
						if(game.madeAMill(finalIndex, p.getPlayerToken())) {
							Token opponentPlayerToken = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
							int boardIndex;
							
							while(true) {
								if(p.isIA()){
									boardIndex = move.remove;
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
						game.checkGameIsOver();
						if(game.gameIsOver()) {
							game.printGameBoard();
							break;
						}
						((LocalGame)game).updateCurrentTurnPlayer();
					} else {
						System.out.println("That's not a valid move");
					}
				} else {
					System.out.println("No piece on that position or it isn't yours");
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
		} else if(userInput.compareTo("CLIENT") == 0 || userInput.compareTo("C") == 0) {
			gc = new GameClient(Token.PLAYER_2);
		} else {
			System.out.println("UNKNOWN COMMAND");
			System.exit(-1);
		}
		
		Player p = null;
		
		// the player with the server is always PLAYER_1 (not necessarily the first to play)
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
					System.out.println("Connect to GameServer at: ");
					userInput = input.readLine();
					gc.connectToServer(userInput);
					break;
				} catch (Exception e) {
					// TODO: handle exception
					System.out.print("NO SERVER DETECTED! ");
					if(--numberTries == 0) {
						System.exit(-1);
					}
					System.out.println("TRYING AGAIN.\n");
					Thread.sleep(10000);
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
					System.out.println("WAITING FOR GAMECLIENT...");
					firstTry = false;
				}
				Thread.sleep(100);
			}
		}

		while(gc.isWaitingForGameStart()) {
			// it should use a Runnable for this right?
			System.out.println("Waiting for game to start");
			Thread.sleep(5000);
		}
		
		System.out.println("GAME IS ON!!!");
		if(gc.getPlayerThatPlaysFirst() == p.getPlayerToken()) {
			System.out.println("I'M THE ONE WHO PLAYS FIRST!");
			game.setTurn(true);
		}
		
		while(true) {
			if(game.isThisPlayerTurn()) {
				
				// check if the other player played the last piece of the placing phase
				if(game.getCurrentGamePhase() != Game.PLACING_PHASE) {
					break;
				}
				
				int boardIndex;
				Player player = game.getPlayer();
				game.printGameBoard();
				
				// ask user input
				System.out.println(player.getName()+" place piece on: ");
				boardIndex = Integer.parseInt(input.readLine());
				
				// validate placing with the server
				if(gc.validatePiecePlacing(boardIndex)) {
					
					// validate placing locally
					if(game.placePieceOfPlayer(boardIndex, player.getPlayerToken())) {
						if(game.madeAMill(boardIndex, player.getPlayerToken())) {
							
						}
						game.setTurn(false);
					}
				} else {
					System.out.println("The server has considered that move invalid. Try again");
				}
				
			}
			Thread.sleep(10);
		}
		
		/*
		while(true) {
			if(game.isThisPlayerTurn()) {
				if(game.getCurrentGamePhase() != Game.PLACING_PHASE) {
					break;
				}
				Player player = game.getPlayer();
				int boardIndex;
				if(p.isIA()) {
					boardIndex = ((MinimaxIAPlayer)player).getIndexToPlacePiece(game.gameBoard);
					System.out.println(player.getName()+" placed piece on "+boardIndex);
				} else {
					game.printGameBoard();
					System.out.println(player.getName()+" place piece on: ");
					userInput = input.readLine();
					userInput = userInput.toUpperCase();
					boardIndex = Integer.parseInt(userInput);
				}

				if(game.setPiece(boardIndex)) { // if the place is valid
					if(game.madeAMill(boardIndex, player.getPlayerToken())) { // if has 3 in a row
						while(true) {
							if(player.isIA()){
								boardIndex = ((IAPlayer)player).getIndexToRemovePieceOfOpponent(game.gameBoard);
								System.out.println(player.getName()+" removes opponent piece on "+boardIndex);
							} else {
								System.out.println("You made a mill. You can remove a piece of your oponent: ");
								userInput = input.readLine();
								userInput = userInput.toUpperCase();
								boardIndex = Integer.parseInt(userInput);
							}
							if(game.removePiece(boardIndex)) { // if it removed an opponent piece
								break;
							} else {
								System.out.println("You can't remove a piece from there. Try again");
							}
						}
					}
					game.setTurn(false);
				}
			}
			Thread.sleep(10);
		}


		System.out.println("The pieces are all placed. Starting the fun part...");
		if(game.playedFirst()) {
			game.setTurn(true);
		}
		while(!game.gameIsOver()) {
			while(true) {
				
				if(game.isThisPlayerTurn()) {
					Player player = game.getPlayer();
					int initialIndex, finalIndex;
					
					if(player.isIA()) {
						Move move = ((IAPlayer)player).getPieceMove(game.gameBoard, game.getCurrentGamePhase());
						initialIndex = move.src;
						finalIndex = move.dest;
						System.out.println(player.getName()+" moved piece from "+initialIndex+" to "+finalIndex);
					} else {
						game.printGameBoard();
						System.out.println(player.getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
						userInput = input.readLine();
						userInput = userInput.toUpperCase();
						String[] positions = userInput.split(":");
						initialIndex = Integer.parseInt(positions[0]);
						finalIndex = Integer.parseInt(positions[1]);
						System.out.println("Move piece from "+initialIndex+" to "+finalIndex);
					}
					if(game.positionHasPieceOfPlayer(initialIndex, player.getPlayerToken())) {
						
						if(game.positionIsAvailable(finalIndex) && (game.validMove(initialIndex, finalIndex) || player.canItFly())) {
							game.movePieceFromTo(initialIndex, finalIndex);
							
							if(game.madeAMill(finalIndex, player.getPlayerToken())) {
								Token opponentPlayerToken = (player.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
								int boardIndex;
								
								while(true) {
									if(player.isIA()){
										boardIndex = ((IAPlayer)player).getIndexToRemovePieceOfOpponent(game.gameBoard);
										System.out.println(player.getName()+" removes opponent piece on "+boardIndex);
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
							game.checkGameIsOver();
							game.setTurn(false);
							break;
						} else {
							System.out.println("That's not a valid move");
						}
					} else {
						System.out.println("No piece on that position or it isn't yours");
					}
				}
				Thread.sleep(10);
			}
		}
		System.out.println("You lost!");
		game.sendGameOver();
		*/
	}
}
