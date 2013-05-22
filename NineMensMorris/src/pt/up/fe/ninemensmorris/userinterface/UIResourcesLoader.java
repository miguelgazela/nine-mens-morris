package pt.up.fe.ninemensmorris.userinterface;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pt.up.fe.ninemensmorris.logic.Game;
import pt.up.fe.ninemensmorris.logic.GameException;
import pt.up.fe.ninemensmorris.logic.Token;

public class UIResourcesLoader {
	
	static public final int LOCAL_GAME = 0;
	static public final int NETWORK_GAME = 1;
	static public final int SERVER_GAME = 2;
	static public final int CLIENT_GAME = 3;
	static public final int HUM_HUM_GAME = 4;
	static public final int HUM_CPU_GAME = 5;
	static public final int CPU_CPU_GAME = 6;
	
	private static UIResourcesLoader instanceLoader;
	
	public BufferedImage mainmenu_bg;
	public BufferedImage settings_bg;
	public BufferedImage about_bg;
	public BufferedImage devteam_bg;
	public BufferedImage newgame_bg;
	public BufferedImage game_bg;
	
	public Coord[] gameLevelCheckCoords;
	public Coord[] new_game_btns_coords;
	public Coord[] board_positions_coords;
	private Coord[] youStrCoords;
	
	public Coord turn_coord;
	public Coord game_phase_coord;
	public Coord game_status_coord;
	
	private Image[] v_unselectedNewGameBtn;
	private Image[] v_selectedNewGameBtn;
	private Image[] v_unselectedPieces;
	private Image[] v_selectedPieces;
	private Image[] v_turns;
	private Image[] v_gamePhases;
	private Image[] v_gameStatus;
	
	public GameImage waitingForConnection, startGameBtn, gameLevelCheck, returnToGameBtn, youStr;
	public GameImage confirmReset;
	
	private UIResourcesLoader() {
		initPieces();
		initImages();
		initBtns();
		initStrings();
		initCoords();
		
		// initialize backgrounds
		try {
			mainmenu_bg = ImageIO.read(new File("images/backgrounds/mainmenu_bg.png"));
			settings_bg = ImageIO.read(new File("images/backgrounds/settings_bg.png"));
			about_bg = ImageIO.read(new File("images/backgrounds/about.png"));
			devteam_bg = ImageIO.read(new File("images/backgrounds/devteam.png"));
			newgame_bg = ImageIO.read(new File("images/backgrounds/newgame_bg.png"));
			game_bg = ImageIO.read(new File("images/backgrounds/game.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Background Resources Missing");
			System.exit(-1);
		}
	}

	public static UIResourcesLoader getInstanceLoader() {
		if(instanceLoader == null) {
			instanceLoader = new UIResourcesLoader();
		}
		return instanceLoader;
	}
	
	private void initStrings() {
		try {
			youStr = new GameImage("images/strings/you.png");
			
			v_gamePhases = new Image[3];
			v_gamePhases[0] = ImageIO.read(new File("images/strings/placing.png"));
			v_gamePhases[1] = ImageIO.read(new File("images/strings/moving.png"));
			v_gamePhases[2] = ImageIO.read(new File("images/strings/flying.png"));
			
			v_gameStatus = new Image[9];
			v_gameStatus[0] = ImageIO.read(new File("images/strings/placePiece.png"));
			v_gameStatus[1] = ImageIO.read(new File("images/strings/selectPiece.png"));
			v_gameStatus[2] = ImageIO.read(new File("images/strings/movePiece.png"));
			v_gameStatus[3] = ImageIO.read(new File("images/strings/removePiece.png"));
			v_gameStatus[4] = ImageIO.read(new File("images/strings/flyPiece.png"));
			v_gameStatus[5] = ImageIO.read(new File("images/strings/waitingAI.png"));
			v_gameStatus[6] = ImageIO.read(new File("images/strings/p1Won.png"));
			v_gameStatus[7] = ImageIO.read(new File("images/strings/p2Won.png"));
			v_gameStatus[8] = ImageIO.read(new File("images/strings/draw.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Background Resources Missing");
			System.exit(-1);
		}
	}
	
	private void initImages() {
		try {
			v_turns = new Image[2];
			v_turns[0] = ImageIO.read(new File("images/pieces/turnP1.png"));
			v_turns[1] = ImageIO.read(new File("images/pieces/turnP2.png"));
			gameLevelCheck = new GameImage("images/buttons/check.png");
			waitingForConnection = new GameImage("images/backgrounds/waitingConnection.png", 0, 0);
			confirmReset = new GameImage("images/backgrounds/confirmReset.png", 0, 0);
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
	}
	
	private void initBtns() {
		v_unselectedNewGameBtn = new Image[7];
		v_selectedNewGameBtn = new Image[7];
		
		try {
			v_unselectedNewGameBtn[LOCAL_GAME] = ImageIO.read(new File("images/buttons/local_uns.png"));
			v_unselectedNewGameBtn[NETWORK_GAME] = ImageIO.read(new File("images/buttons/network_uns.png"));
			v_unselectedNewGameBtn[SERVER_GAME] = ImageIO.read(new File("images/buttons/server_uns.png"));
			v_unselectedNewGameBtn[CLIENT_GAME] = ImageIO.read(new File("images/buttons/client_uns.png"));
			v_unselectedNewGameBtn[HUM_HUM_GAME] = ImageIO.read(new File("images/buttons/h-h_uns.png"));
			v_unselectedNewGameBtn[HUM_CPU_GAME] = ImageIO.read(new File("images/buttons/h-c_uns.png"));
			v_unselectedNewGameBtn[CPU_CPU_GAME] = ImageIO.read(new File("images/buttons/c-c_uns.png"));
			v_selectedNewGameBtn[LOCAL_GAME] = ImageIO.read(new File("images/buttons/local_sel.png"));
			v_selectedNewGameBtn[NETWORK_GAME] = ImageIO.read(new File("images/buttons/network_sel.png"));
			v_selectedNewGameBtn[SERVER_GAME] = ImageIO.read(new File("images/buttons/server_sel.png"));
			v_selectedNewGameBtn[CLIENT_GAME] = ImageIO.read(new File("images/buttons/client_sel.png"));
			v_selectedNewGameBtn[HUM_HUM_GAME] = ImageIO.read(new File("images/buttons/h-h_sel.png"));
			v_selectedNewGameBtn[HUM_CPU_GAME] = ImageIO.read(new File("images/buttons/h-c_sel.png"));
			v_selectedNewGameBtn[CPU_CPU_GAME] = ImageIO.read(new File("images/buttons/c-c_sel.png"));
			
			returnToGameBtn = new GameImage("images/buttons/return_game.png", 993, 663);
			startGameBtn = new GameImage("images/buttons/start_game.png", 272, 640);
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
	}
	
	private void initCoords() {
		int x = 37;
		gameLevelCheckCoords = new Coord[10];
		gameLevelCheckCoords[0] = new Coord(x, 264);
		gameLevelCheckCoords[1] = new Coord(x, 325);
		gameLevelCheckCoords[2] = new Coord(x, 386);
		gameLevelCheckCoords[3] = new Coord(x, 447);
		gameLevelCheckCoords[4] = new Coord(x, 508);
		x = 344;
		gameLevelCheckCoords[5] = new Coord(x, 264);
		gameLevelCheckCoords[6] = new Coord(x, 325);
		gameLevelCheckCoords[7] = new Coord(x, 386);
		gameLevelCheckCoords[8] = new Coord(x, 447);
		gameLevelCheckCoords[9] = new Coord(x, 508);
		
		new_game_btns_coords = new Coord[7];
		new_game_btns_coords[0] = new Coord(106, 177);
		new_game_btns_coords[1] = new Coord(221, 177);
		new_game_btns_coords[2] = new Coord(106, 292);
		new_game_btns_coords[3] = new Coord(221, 292);
		new_game_btns_coords[4] = new Coord(106, 407);
		new_game_btns_coords[5] = new Coord(221, 407);
		new_game_btns_coords[6] = new Coord(221, 522);
		
		board_positions_coords = new Coord[24];
		board_positions_coords[0] = new Coord(379,66);
		board_positions_coords[1] = new Coord(624,66);
		board_positions_coords[2] = new Coord(870,66);
		board_positions_coords[3] = new Coord(451,138);
		board_positions_coords[4] = new Coord(624,138);
		board_positions_coords[5] = new Coord(798,138);
		board_positions_coords[6] = new Coord(525,212);
		board_positions_coords[7] = new Coord(624,212);
		board_positions_coords[8] = new Coord(724,212);
		board_positions_coords[9] = new Coord(379,310);
		board_positions_coords[10] = new Coord(451,310);
		board_positions_coords[11] = new Coord(525,310);
		board_positions_coords[12] = new Coord(724,310);
		board_positions_coords[13] = new Coord(798,310);
		board_positions_coords[14] = new Coord(870,310);
		board_positions_coords[15] = new Coord(525,411);
		board_positions_coords[16] = new Coord(624,411);
		board_positions_coords[17] = new Coord(724,411);
		board_positions_coords[18] = new Coord(451,485);
		board_positions_coords[19] = new Coord(624,485);
		board_positions_coords[20] = new Coord(798,485);
		board_positions_coords[21] = new Coord(379,557);
		board_positions_coords[22] = new Coord(624,557);
		board_positions_coords[23] = new Coord(870,557);
		
		turn_coord = new Coord(272, 671);
		
		youStrCoords = new Coord[2];
		youStrCoords[0] = new Coord(210, 312);
		youStrCoords[1] = new Coord(1011, 312);
		
		game_phase_coord = new Coord(545, 11);
		
		game_status_coord = new Coord(363, 668);
	}
	
	private void initPieces() {
		try {
			v_unselectedPieces = new Image[2];
			v_selectedPieces = new Image[2];
			
			v_unselectedPieces[0] = ImageIO.read(new File("images/pieces/pieceP1_uns.png"));
			v_unselectedPieces[1] = ImageIO.read(new File("images/pieces/pieceP2_uns.png"));
			v_selectedPieces[0] = ImageIO.read(new File("images/pieces/pieceP1_sel.png"));
			v_selectedPieces[1] = ImageIO.read(new File("images/pieces/pieceP2_sel.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
	}
	
	public Image getGameStatus(String str) {
		switch (str) {
		case "place":
			return v_gameStatus[0];
		case "select":
			return v_gameStatus[1];
		case "move":
			return v_gameStatus[2];
		case "remove":
			return v_gameStatus[3];
		case "fly":
			return v_gameStatus[4];
		case "waitingAI":
			return v_gameStatus[5];
		case "p1":
			return v_gameStatus[6];
		case "p2":
			return v_gameStatus[7];
		case "draw":
			return v_gameStatus[8];
		default:
			return null;
		}
	}
	
	public Image getGamePhaseStr(int gamePhase) throws GameException {
		if(gamePhase != Game.PLACING_PHASE && gamePhase != Game.MOVING_PHASE && gamePhase != Game.FLYING_PHASE) {
			throw new GameException("Invalid game phase: "+gamePhase);
		}
		if(gamePhase == Game.PLACING_PHASE) {
			return v_gamePhases[0];
		} else if(gamePhase == Game.MOVING_PHASE) {
			return v_gamePhases[1];
		} else {
			return v_gamePhases[2];
		}
	}
	
	public Coord getPlayerYouStrCoord(Token player) throws GameException {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException("Invalid Token to get turn player: "+player);
		}
		if(player == Token.PLAYER_1) {
			return youStrCoords[0];
		} else {
			return youStrCoords[1];
		}
	}
	
	public Image getPlayerTurn(Token player) throws GameException {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException("Invalid Token to get turn player: "+player);
		}
		if(player == Token.PLAYER_1) {
			return v_turns[0];
		} else {
			return v_turns[1];
		}
	}
	
	public Image getUnselectedPiece(Token player) {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			return null;
		}
		if(player == Token.PLAYER_1) {
			return v_unselectedPieces[0];
		} else {
			return v_unselectedPieces[1];
		}
	}
	
	public Image getSelectedPiece(Token player) throws GameException {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException("Invalid Token to get selected piece: "+player);
		}
		if(player == Token.PLAYER_1) {
			return v_selectedPieces[0];
		} else {
			return v_selectedPieces[1];
		}
	}
	
	public Image getUnselectedNewGameBtn(int btn) {
		if(btn < LOCAL_GAME || btn > CPU_CPU_GAME) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return v_unselectedNewGameBtn[btn];
	}
	
	public Image getSelectedNewGameBtn(int btn) {
		if(btn < LOCAL_GAME || btn > CPU_CPU_GAME) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return v_selectedNewGameBtn[btn];
	}
}
