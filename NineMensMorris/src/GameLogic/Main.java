package GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;

import GameLogic.IAPlayer.Move;

public class Main {
	public Game game;
	public BufferedReader input;
	public int nothing;
	
	public static void main(String []args) throws Exception {
		System.out.println("Nine Men's Morris starting...");
		Main maingame = new Main();
		maingame.input = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("LOCAL or NETWORK?");
		String userInput = maingame.input.readLine();
		
		if(userInput.compareTo("LOCAL") == 0) {
			maingame.createLocalGame();
		} else if(userInput.compareTo("NETWORK") == 0) {
			maingame.createNetworkGame();
		} else {
			System.out.println("UNKNOWN COMMAND");
			System.exit(-1);
		}
	}
	
	public void createLocalGame() throws IOException {
		game = new LocalGame();
		System.out.println("Player 1: HUMAN or CPU?");
		String userInput = input.readLine();
		Player p1 = null, p2 = null;
		
		if(userInput.compareTo("HUMAN") == 0) {
			p1 = new HumanPlayer("Souto",Player.PLAYER_1);
		} else if(userInput.compareTo("CPU") == 0) {
			p1 = new RandomIAPlayer(Player.PLAYER_1);
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		System.out.println("Player 2: HUMAN or CPU?");
		userInput = input.readLine();
		
		if(userInput.compareTo("HUMAN") == 0) {
			p2 = new HumanPlayer("Miguel", Player.PLAYER_2);
		} else if(userInput.compareTo("CPU") == 0) {
			p2 = new RandomIAPlayer(Player.PLAYER_2);
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		((LocalGame)game).setPlayers(p1, p2);
		while(game.getGamePhase() == Game.PLACING_PHASE) {
			while(true) {
				Player p = ((LocalGame)game).getCurrentTurnPlayer();
				int boardIndex;
				if(p.isIA()) {
					boardIndex = ((IAPlayer)p).getIndexToPlacePiece(game.gameBoard);
					System.out.println(p.getName()+" placed piece on "+boardIndex);
				} else {
					game.printGameBoard();
					System.out.println(((LocalGame)game).getCurrentTurnPlayer().getName()+" place piece on: ");
					userInput = input.readLine();
					boardIndex = Integer.parseInt(userInput);
				}
				if(game.setPiece(boardIndex, p.getPlayerId())) {
					if(game.madeAMill(boardIndex, p.getPlayerId())) {
						int otherPlayerId = (p.getPlayerId() == Player.PLAYER_1) ? Player.PLAYER_2 : Player.PLAYER_1;
						while(true) {
							if(p.isIA()){
								boardIndex = ((IAPlayer)p).getIndexToRemovePieceOfOpponent(game.gameBoard);
								System.out.println(p.getName()+" removes opponent piece on "+boardIndex);
							} else {
								System.out.println("You made a mill. You can remove a piece of your oponent: ");
								userInput = input.readLine();
								boardIndex = Integer.parseInt(userInput);
							}
							if(game.removePiece(boardIndex, otherPlayerId)) {
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
		int numMoves = 0;
		while(!game.gameIsOver()) {
			while(true) {
				Player p = ((LocalGame)game).getCurrentTurnPlayer();
				int initialIndex, finalIndex;
				if(p.isIA()) {
					Move move = ((IAPlayer)p).getPieceMove(game.gameBoard);
					initialIndex = move.src;
					finalIndex = move.dest;
					System.out.println(p.getName()+" moved piece from "+initialIndex+" to "+finalIndex);
				} else {
					game.printGameBoard();
					System.out.println(p.getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
					userInput = input.readLine();
					String[] positions = userInput.split(":");
					initialIndex = Integer.parseInt(positions[0]);
					finalIndex = Integer.parseInt(positions[1]);
					System.out.println("Move piece from "+initialIndex+" to "+finalIndex);
				}
				if(game.positionHasPieceOfPlayer(initialIndex, p.getPlayerId())) {
					if(game.positionIsAvailable(finalIndex) && (game.validMove(initialIndex, finalIndex) || p.canItFly())) {
						game.movePieceFromTo(initialIndex, finalIndex, p.getPlayerId());
						numMoves++;
						if(game.madeAMill(finalIndex, p.getPlayerId())) {
							int otherPlayerId = (p.getPlayerId() == Player.PLAYER_1) ? Player.PLAYER_2 : Player.PLAYER_1;
							int boardIndex;
							while(true) {
								if(p.isIA()){
									boardIndex = ((IAPlayer)p).getIndexToRemovePieceOfOpponent(game.gameBoard);
									System.out.println(p.getName()+" removes opponent piece on "+boardIndex);
								} else {
									System.out.println("You made a mill! You can remove a piece of your oponent: ");
									userInput = input.readLine();
									boardIndex = Integer.parseInt(userInput);
								}
								if(game.removePiece(boardIndex, otherPlayerId)) {
									break;
								} else {
									System.out.println("It couldn't be done! Try again.");
								}
							}
						}
						((LocalGame)game).checkGameIsOver();
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
		System.out.println("Num moves: "+numMoves);
	}
	
	public void createNetworkGame() {
		game = new NetworkGame();
	}
}
