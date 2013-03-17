package GameUI;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class UIResourcesLoader {
	public static int BLUE_PIECE = 0;
	public static int GREEN_PIECE = 1;
	public static int PURPLE_PIECE = 2;
	public static int YELLOW_PIECE = 3;
	public static int RED_PIECE = 4;
	
	private static UIResourcesLoader instanceLoader;
	
	public BufferedImage game_background_1;
	public BufferedImage main_menu_background;
	private Image[] v_unselectedPieces;
	private Image[] v_selectedPieces; 
	
	private UIResourcesLoader() {
		initPieces();
		try {
			game_background_1 = ImageIO.read(new File("images/backgrounds/game_background_test.png"));
			main_menu_background = ImageIO.read(new File("images/backgrounds/main_menu_background.png"));
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
