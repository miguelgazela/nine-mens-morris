package GameUI;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import GameLogic.GameException;
import GameLogic.Token;

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
	
	public Image game_level_check;
	public Image start_game;
	public Image return_game;
	
	public Coord[] game_level_check_coords;
	public Coord[] new_game_btns_coords;
	public Coord[] board_positions_coords;
	public Coord start_game_btn_coord;
	public Coord return_game_btn_coord;
	public Coord turn_coord;
	
	private Image[] v_unselectedNewGameBtn;
	private Image[] v_selectedNewGameBtn;
	private Image[] v_unselectedPieces;
	private Image[] v_selectedPieces;
	private Image[] v_turns;
	
	
	private UIResourcesLoader() {
		initPieces();
		initImages();
		initBtns();
		initCoords();
		
		try {
			mainmenu_bg = ImageIO.read(new File("images/backgrounds/mainmenu_bg.png"));
			settings_bg = ImageIO.read(new File("images/backgrounds/settings_bg.png"));
			about_bg = ImageIO.read(new File("images/backgrounds/about.png"));
			devteam_bg = ImageIO.read(new File("images/backgrounds/devteam.png"));
			newgame_bg = ImageIO.read(new File("images/backgrounds/newgame_bg.png"));
			game_level_check = ImageIO.read(new File("images/buttons/check.png"));
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
	
	private void initImages() {
		try {
			v_turns = new Image[2];
			v_turns[0] = ImageIO.read(new File("images/pieces/turnP1.png"));
			v_turns[1] = ImageIO.read(new File("images/pieces/turnP2.png"));
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
			start_game = ImageIO.read(new File("images/buttons/start_game.png"));
			return_game = ImageIO.read(new File("images/buttons/return_game.png"));
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
	}
	
	private void initCoords() {
		game_level_check_coords = new Coord[5];
		int x = 70;
		
		game_level_check_coords[0] = new Coord(x, 210);
		game_level_check_coords[1] = new Coord(x, 283);
		game_level_check_coords[2] = new Coord(x, 356);
		game_level_check_coords[3] = new Coord(x, 429);
		game_level_check_coords[4] = new Coord(x, 502);
		
		new_game_btns_coords = new Coord[7];
		new_game_btns_coords[0] = new Coord(106, 177);
		new_game_btns_coords[1] = new Coord(221, 177);
		new_game_btns_coords[2] = new Coord(106, 292);
		new_game_btns_coords[3] = new Coord(221, 292);
		new_game_btns_coords[4] = new Coord(106, 407);
		new_game_btns_coords[5] = new Coord(221, 407);
		new_game_btns_coords[6] = new Coord(221, 522);
		
		start_game_btn_coord = new Coord(272, 640);
		return_game_btn_coord = new Coord(993, 663);
		
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
	
	public Image getPlayerTurn(Token player) throws GameException {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException("Invalid Token to get turn player");
		}
		if(player == Token.PLAYER_1) {
			return v_turns[0];
		} else {
			return v_turns[1];
		}
	}
	
	public Image getUnselectedPiece(Token player) throws GameException {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException("Invalid Token to get unselected piece");
		}
		if(player == Token.PLAYER_1) {
			return v_unselectedPieces[0];
		} else {
			return v_unselectedPieces[1];
		}
	}
	
	public Image getSelectedPiece(Token player) throws GameException {
		if(player != Token.PLAYER_1 && player != Token.PLAYER_2) {
			throw new GameException("Invalid Token to get selected piece");
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
