package GameUI;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UIResourcesLoader {
	public final static int BLUE_PIECE = 0;
	public final static int GREEN_PIECE = 1;
	public final static int PURPLE_PIECE = 2;
	public final static int YELLOW_PIECE = 3;
	public final static int RED_PIECE = 4;
	public final static int NEW_GAME_BTN = 0;
	public final static int OPTIONS_BTN = 1;
	public final static int EXIT_BTN = 2;
	public final static int MAIN_MENU_NORMAL = 0;
	public final static int MAIN_MENU_HOVER = 1;
	public final static int MAIN_MENU_ACTIVE = 2;
	
	private static UIResourcesLoader instanceLoader;
	
	public BufferedImage game_background_1;
	public BufferedImage main_menu_background;
	public BufferedImage options_background;
	public BufferedImage about_background;
	public BufferedImage game_options_background;
	private Image[] v_unselectedPieces;
	private Image[] v_selectedPieces;
	private Image[] v_mainmenu_buttons_normal;
	private Image[] v_mainmenu_buttons_hover;
	private Image[] v_mainmenu_buttons_active;
	private Image[] v_about_buttons;
	
	private Coord[] v_mainmenu_buttons_coords;
	private Coord about_button_coord;
	
	private UIResourcesLoader() {
		initPieces();
		initMainMenuButtons();
		try {
			game_background_1 = ImageIO.read(new File("images/backgrounds/game_background_test.png"));
			main_menu_background = ImageIO.read(new File("images/backgrounds/main_menu_background.png"));
			options_background = ImageIO.read(new File("images/backgrounds/options_background.png"));
			about_background = ImageIO.read(new File("images/backgrounds/about_background.png"));
			game_options_background = ImageIO.read(new File("images/backgrounds/game_options.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
	}

	public static UIResourcesLoader getInstanceLoader() {
		if(instanceLoader == null) {
			instanceLoader = new UIResourcesLoader();
		}
		return instanceLoader;
	}
	
	private void initMainMenuButtons() {
		v_mainmenu_buttons_normal = new Image[3];
		v_mainmenu_buttons_hover = new Image[3];
		v_mainmenu_buttons_active = new Image[3];
		v_mainmenu_buttons_coords = new Coord[3];
		v_about_buttons = new Image[2];
		
		try {
			v_mainmenu_buttons_normal[NEW_GAME_BTN] = ImageIO.read(new File("images/buttons/new_game_normal.png"));
			v_mainmenu_buttons_normal[OPTIONS_BTN] = ImageIO.read(new File("images/buttons/options_normal.png"));
			v_mainmenu_buttons_normal[EXIT_BTN] = ImageIO.read(new File("images/buttons/exit_game_normal.png"));
			v_mainmenu_buttons_hover[NEW_GAME_BTN] = ImageIO.read(new File("images/buttons/new_game_hover.png"));
			v_mainmenu_buttons_hover[OPTIONS_BTN] = ImageIO.read(new File("images/buttons/options_hover.png"));
			v_mainmenu_buttons_hover[EXIT_BTN] = ImageIO.read(new File("images/buttons/exit_game_hover.png"));
			v_mainmenu_buttons_active[NEW_GAME_BTN] = ImageIO.read(new File("images/buttons/new_game_active.png"));
			v_mainmenu_buttons_active[OPTIONS_BTN] = ImageIO.read(new File("images/buttons/options_active.png"));
			v_mainmenu_buttons_active[EXIT_BTN] = ImageIO.read(new File("images/buttons/exit_game_active.png"));
			v_about_buttons[0] = ImageIO.read(new File("images/buttons/about_normal.png"));
			v_about_buttons[1] = ImageIO.read(new File("images/buttons/about_hover.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
		v_mainmenu_buttons_coords[NEW_GAME_BTN] = new Coord(159, 310);
		v_mainmenu_buttons_coords[OPTIONS_BTN] = new Coord(274, 310);
		v_mainmenu_buttons_coords[EXIT_BTN] = new Coord(389, 310);
		about_button_coord = new Coord(311, 160);
	}
	
	public Image[] getMainMenuButtons(int buttonsState) {
		switch (buttonsState) {
		case MAIN_MENU_NORMAL:
			return v_mainmenu_buttons_normal;
		case MAIN_MENU_HOVER:
			return v_mainmenu_buttons_hover;
		case MAIN_MENU_ACTIVE:
			return v_mainmenu_buttons_active;
		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public Coord[] getMainMenuButtonsCoord() {
		return v_mainmenu_buttons_coords;
	}
	
	public Coord getAboutButtonCoord() {
		return about_button_coord;
	}
	
	public Image[] getAboutButtons() {
		return v_about_buttons;
	}
	
	private void initPieces() {
		v_selectedPieces = new Image[5];
		v_unselectedPieces = new Image[5];
		
		try {
			v_unselectedPieces[BLUE_PIECE] = ImageIO.read(new File("images/pieces/blue.png"));
			v_unselectedPieces[GREEN_PIECE] = ImageIO.read(new File("images/pieces/green.png"));
			v_unselectedPieces[PURPLE_PIECE] = ImageIO.read(new File("images/pieces/purple.png"));
			v_unselectedPieces[YELLOW_PIECE] = ImageIO.read(new File("images/pieces/yellow.png"));
			v_unselectedPieces[RED_PIECE] = ImageIO.read(new File("images/pieces/red.png"));
			v_selectedPieces[BLUE_PIECE] = ImageIO.read(new File("images/pieces/blue_selected.png"));
			v_selectedPieces[GREEN_PIECE] = ImageIO.read(new File("images/pieces/green_selected.png"));
			v_selectedPieces[PURPLE_PIECE] = ImageIO.read(new File("images/pieces/purple_selected.png"));
			v_selectedPieces[YELLOW_PIECE] = ImageIO.read(new File("images/pieces/yellow_selected.png"));
			v_selectedPieces[RED_PIECE] = ImageIO.read(new File("images/pieces/red_selected.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Resources missing");
			System.exit(-1);
		}
	}
	
	public Image getSelectedPiece(int piece) {
		if(piece < BLUE_PIECE || piece > RED_PIECE) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return v_selectedPieces[piece];
	}
	
	public Image getUnselectedPiece(int piece) {
		if(piece < BLUE_PIECE || piece > RED_PIECE) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return v_unselectedPieces[piece];
	}
}
