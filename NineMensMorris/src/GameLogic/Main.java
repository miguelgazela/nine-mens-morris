package GameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;

public class Main {
	public Game game;
	public BufferedReader input;
	
	public void createLocalGame() throws IOException {
		game = new LocalGame();
		System.out.println("Player 1: HUMAN or CPU?");
		String userInput = input.readLine();
		Player p1 = null, p2 = null;
		
		if(userInput.compareTo("HUMAN") == 0) {
			p1 = new HumanPlayer("Souto",Player.PLAYER_1);
		} else if(userInput.compareTo("CPU") == 0) {
			p1 = new IAPlayer(Player.PLAYER_1);
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		System.out.println("Player 2: HUMAN or CPU?");
		userInput = input.readLine();
		
		if(userInput.compareTo("HUMAN") == 0) {
			p2 = new HumanPlayer("Miguel", Player.PLAYER_2);
		} else if(userInput.compareTo("CPU") == 0) {
			p2 = new IAPlayer(Player.PLAYER_2);
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		((LocalGame)game).setPlayers(p1, p2);
		while(game.inPlacingPhase()) {
			while(true) {
				int player = ((LocalGame)game).getCurrentTurnPlayer().getPlayerId();
				System.out.println(((LocalGame)game).getCurrentTurnPlayer().getName()+" place a piece on: ");
				userInput = input.readLine();
				int boardIndex = Integer.parseInt(userInput);
				if(game.setPiece(boardIndex, player)) {
					((LocalGame)game).updateCurrentTurnPlayer();
					break;
				}
			}
			game.printGameBoard();
		}
		
		System.out.println("Pieces are all placed. Starting the fun part...");
		while(!game.gameIsOver()) {
			while(true) {
				Player p = ((LocalGame)game).getCurrentTurnPlayer();
				System.out.println(p.getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
				userInput = input.readLine();
				String[] positions = userInput.split(":");
				int initialIndex = Integer.parseInt(positions[0]);
				int finalIndex = Integer.parseInt(positions[1]);
				System.out.println(initialIndex+" : "+finalIndex);
				if(game.positionHasPieceOfPlayer(initialIndex, p.getPlayerId())) {
					if(game.validMove(initialIndex, finalIndex)) {
						if(game.positionIsAvailable(finalIndex) && game.validMove(initialIndex, finalIndex)) {
							game.movePieceFromTo(initialIndex, finalIndex, p.getPlayerId());
							((LocalGame)game).updateCurrentTurnPlayer();
							game.printGameBoard();
							((LocalGame)game).checkGameIsOver();
							if(game.gameIsOver()) {
								break;
							}
						} else {
							System.out.println("That's not a valid move");
						}
					}
				} else {
					System.out.println("No piece on that position or it isn't yours");
				}
			}
		}
	}
	
	public void createNetworkGame() {
		game = new NetworkGame();
	}
	
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
			//maingame.game = new NetworkGame();
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
	}
}
