package GameLogic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class Main {
	public Game game;
	
	public static void main(String []args) throws Exception {
		System.out.println("Nine Men's Morris starting...");
		Main maingame = new Main();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("LOCAL or NETWORK?");
		String userInput = input.readLine();
		
		if(userInput.compareTo("LOCAL") == 0) {
			maingame.game = new LocalGame();
		} else if(userInput.compareTo("NETWORK") == 0) {
			maingame.game = new NetworkGame();
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		System.out.println("Player 1: HUMAN or CPU?");
		userInput = input.readLine();
		Player p1 = null, p2 = null;
		
		if(userInput.compareTo("HUMAN") == 0) {
			p1 = new HumanPlayer("Souto");
		} else if(userInput.compareTo("CPU") == 0) {
			p1 = new IAPlayer();
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		
		System.out.println("Player 2: HUMAN or CPU?");
		userInput = input.readLine();
		
		if(userInput.compareTo("HUMAN") == 0) {
			p2 = new HumanPlayer("C‹o");
		} else if(userInput.compareTo("CPU") == 0) {
			p2 = new IAPlayer();
		} else {
			System.out.println("Command unknown");
			System.exit(-1);
		}
		maingame.game.setPlayers(p1, p2);
		
		while(maingame.game.inPlacingPhase()) {
			while(true) {
				System.out.println(maingame.game.getCurrentTurnPlayer().getName()+" place a piece on: ");
				userInput = input.readLine();
				int boardIndex = Integer.parseInt(userInput);
				if(maingame.game.setPiece(boardIndex)) {
					maingame.game.updateCurrentTurnPlayer();
					break;
				}
			}
			maingame.game.printGameBoard();
		}
		System.out.println("Starting the game");
		while(!maingame.game.gameIsOver()) {
			while(true) {
				System.out.println(maingame.game.getCurrentTurnPlayer().getName()+" it's your turn. Input PIECE_POS:PIECE_DEST");
				userInput = input.readLine();
				String[] positions = userInput.split(":");
				int initialIndex = Integer.parseInt(positions[0]);
				int finalIndex = Integer.parseInt(positions[1]);
				System.out.println(initialIndex+" : "+finalIndex);
			}
		}
	}
}
