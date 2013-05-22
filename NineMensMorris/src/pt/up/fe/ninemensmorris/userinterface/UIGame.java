package pt.up.fe.ninemensmorris.userinterface;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pt.up.fe.ninemensmorris.logic.Board;
import pt.up.fe.ninemensmorris.logic.Game;
import pt.up.fe.ninemensmorris.logic.GameException;
import pt.up.fe.ninemensmorris.logic.HumanPlayer;
import pt.up.fe.ninemensmorris.logic.IAPlayer;
import pt.up.fe.ninemensmorris.logic.LocalGame;
import pt.up.fe.ninemensmorris.logic.MinimaxIAPlayer;
import pt.up.fe.ninemensmorris.logic.Move;
import pt.up.fe.ninemensmorris.logic.NetworkGame;
import pt.up.fe.ninemensmorris.logic.Player;
import pt.up.fe.ninemensmorris.logic.RandomIAPlayer;
import pt.up.fe.ninemensmorris.logic.Token;
import pt.up.fe.ninemensmorris.network.GameClient;
import pt.up.fe.ninemensmorris.network.GameServer;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;

import com.esotericsoftware.minlog.Log;


public class UIGame extends JFrame {
	private static final long serialVersionUID = -5256114500541984237L;
	private SLPanel panel;
	private final UIMainMenuPanel uiMainMenuPanel;
	private UISettingsPanel uiSettingsPanel;
	private UIAboutPanel uiAboutPanel;
	private UINewGamePanel uiNewGamePanel;
	private UIDevTeamPanel uiDevTeamPanel;
	private UIGamePanel uiGamePanel;
	private SLConfig mainCfg, SettingsCfg, AboutCfg, NewGameCfg, DevTeamCfg, GameCfg;
	protected MenuState currentMenuState;
	
	public UIGame() {
		super("Nine Men's Morris - by Afonso Caldas & Miguel Oliveira");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new SLPanel();
		getContentPane().add(panel);
		currentMenuState = MenuState.Main; 
		
		// defining the game menu panel
		uiMainMenuPanel = new UIMainMenuPanel();
		uiMainMenuPanel.addMouseMotionListener(uiMainMenuPanel);
		uiMainMenuPanel.addMouseListener(uiMainMenuPanel);
		uiMainMenuPanel.addKeyListener(uiMainMenuPanel);
		
		// defining the settings panel
		uiSettingsPanel = new UISettingsPanel();
		uiSettingsPanel.addMouseMotionListener(uiSettingsPanel);
		uiSettingsPanel.addMouseListener(uiSettingsPanel);
		
		// defining the game rules panel
		uiAboutPanel = new UIAboutPanel();
		uiAboutPanel.addMouseListener(uiAboutPanel);
		
		// defining the dev. team panel
		uiDevTeamPanel = new UIDevTeamPanel();
		uiDevTeamPanel.addMouseListener(uiDevTeamPanel);
		
		// defining the new game panel
		uiNewGamePanel = new UINewGamePanel();
		uiNewGamePanel.addMouseListener(uiNewGamePanel);
		
		// defining the game panel
		uiGamePanel = new UIGamePanel();
		uiGamePanel.addMouseListener(uiGamePanel);
		
		initiatePanelConfigurations();
		
		setSize(1280, 742);
		//setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initiatePanelConfigurations() {
		mainCfg = new SLConfig(panel)
		.row(1f).col(1f)
		.place(0, 0, uiMainMenuPanel);

		SettingsCfg = new SLConfig(panel)
		.row(1f).col(1f).col(2f)
		.place(0, 0, uiSettingsPanel)
		.place(0, 1, uiMainMenuPanel);

		AboutCfg = new SLConfig(panel)
		.row(1f).col(1f)
		.place(0, 0, uiAboutPanel);
		
		DevTeamCfg = new SLConfig(panel)
		.row(1f).col(1f)
		.place(0, 0, uiDevTeamPanel);

		NewGameCfg = new SLConfig(panel)
		.row(1f).col(2f).col(1f)
		.place(0, 0, uiMainMenuPanel)
		.place(0, 1, uiNewGamePanel);
		
		GameCfg = new SLConfig(panel)
		.row(1f).col(1f)
		.place(0, 0, uiGamePanel);

		panel.setTweenManager(SLAnimator.createTweenManager());
		panel.initialize(mainCfg);
	}

	private class UISettingsPanel extends JPanel implements MouseListener, MouseMotionListener {
		private static final long serialVersionUID = -2881193099945335066L;
		
		private UIResourcesLoader uiResourcesLoader;
		
		static final public int VERY_EASY = 0;
		static final public int EASY = 1;
		static final public int NORMAL = 2;
		static final public int HARD = 3;
		static final public int VERY_HARD = 4;
		
		private Graphics graphics;
		private BufferedImage background;
		private Image gameLevelCheck;
		private int gameLevelCPU1 = UISettingsPanel.NORMAL;
		private int gameLevelCPU2 = UISettingsPanel.NORMAL;
		
		public UISettingsPanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.settings_bg;
			gameLevelCheck = uiResourcesLoader.gameLevelCheck.image;
		}
		
		@Override
		public Dimension getPreferredSize() {
			if (background != null) {
				int width = background.getWidth();
				int height = background.getHeight();
				return new Dimension(width, height);
			}
			return super.getPreferredSize();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(graphics = g); // clear off-screen bitmap

			// draws the game elements
			if (background != null) {
				graphics.drawImage(background, 0, 0, this);
				
				// draw check in the correct position
				Coord c = uiResourcesLoader.gameLevelCheckCoords[gameLevelCPU1];
				graphics.drawImage(gameLevelCheck, c.x, c.y, this);
				
				c = uiResourcesLoader.gameLevelCheckCoords[gameLevelCPU2+5];
				graphics.drawImage(gameLevelCheck, c.x, c.y, this);
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			if(currentMenuState == MenuState.Settings) {
				
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(currentMenuState == MenuState.Settings) {
				int x = e.getX();
				int y = e.getY();
//				System.out.println("X: "+x+" Y: "+y);
				int CPU = 0, levelAI = -1;
				
				if(x > 348 && y > 643 && x < 398 && y < 690) { // back to main menu
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(mainCfg, 1f)
						//.setStartSide(SLSide.RIGHT, uiGameMenuPanel)
						.setEndSide(SLSide.LEFT, uiSettingsPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.Main;
						}}))
						.play();
					}}.run();
				} else if(x >= 37 && x <= 82) { // possible CPU1
					CPU = 1;
				} else if(x >= 344 && x <= 389) { // possible CPU2
					CPU = 2;
				}
				
				if(CPU != 0) {
					if(y >= 264 && y <= 309) { // very easy
						levelAI = UISettingsPanel.VERY_EASY;
					} else if(y >= 325 && y <= 370) { // easy
						levelAI = UISettingsPanel.EASY;
					} else if(y >= 386 && y <= 431) { // normal
						levelAI = UISettingsPanel.NORMAL;
					} else if(y >= 447 && y <= 492) { // hard
						levelAI = UISettingsPanel.HARD;
					} else if(y >= 508 && y <= 553) { // very hard
						levelAI = UISettingsPanel.VERY_HARD;
					}
					
					if(levelAI != -1) {
						if(CPU == 1) {
							gameLevelCPU1 = levelAI;
						} else {
							gameLevelCPU2 = levelAI;
						}
					}
				}
				repaint();
			}
		}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
	}
	
	private class UIAboutPanel extends JPanel implements MouseListener {

		private static final long serialVersionUID = -7973208111694509132L;
		private UIResourcesLoader uiResourcesLoader;
		private BufferedImage background;
		private Graphics graphics;
		
		public UIAboutPanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.about_bg;
		}
		
		@Override
		public Dimension getPreferredSize() {
			if (background != null) {
				int width = background.getWidth();
				int height = background.getHeight();
				return new Dimension(width, height);
			}
			return super.getPreferredSize();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(graphics = g); // clear off-screen bitmap
			if (background != null) {
				graphics.drawImage(background, 0, 0, this);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			if(x > 1200 && y > 34 && x < 1249 && y < 80) {
				new Runnable() {@Override public void run() {
					panel.createTransition()
					.push(new SLKeyframe(mainCfg, 2f)
					.setStartSide(SLSide.TOP, uiMainMenuPanel)
					.setEndSide(SLSide.BOTTOM, uiAboutPanel)
					.setCallback(new SLKeyframe.Callback() {@Override public void done() {
						currentMenuState = MenuState.Main;
					}}))
					.play();
				}}.run();
			}
		}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
	}
	
	private class UIDevTeamPanel extends JPanel implements MouseListener {
		private static final long serialVersionUID = 5264009616917087268L;
		private UIResourcesLoader uiResourcesLoader;
		private BufferedImage background;
		private Graphics graphics;
		
		public UIDevTeamPanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.devteam_bg;
		}
		
		@Override
		public Dimension getPreferredSize() {
			if (background != null) {
				int width = background.getWidth();
				int height = background.getHeight();
				return new Dimension(width, height);
			}
			return super.getPreferredSize();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(graphics = g); // clear off-screen bitmap
			if (background != null) {
				graphics.drawImage(background, 0, 0, this);
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
//			System.out.println("X: "+x+" Y: "+y);

			if(x > 1200 && y > 642 && x < 1249 && y < 689) { // getting back to main menu
				new Runnable() {@Override public void run() {
					panel.createTransition()
					.push(new SLKeyframe(mainCfg, 2f)
					.setStartSide(SLSide.BOTTOM, uiMainMenuPanel)
					.setEndSide(SLSide.TOP, uiDevTeamPanel)
					.setCallback(new SLKeyframe.Callback() {@Override public void done() {
						currentMenuState = MenuState.Main;
					}}))
					.play();
				}}.run();
			} else if(x > 292 && y > 460 && x < 431 && y < 495) { // sifeup miguel
				WebPage.open("http://sigarra.up.pt/feup/pt/fest_geral.cursos_list?pv_num_unico=200700604");
			} else if(x > 850 && y > 460 && x < 989 && y < 495) { // sifeup afonso
				WebPage.open("http://sigarra.up.pt/feup/pt/fest_geral.cursos_list?pv_num_unico=201009023");
			}
		}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
	}
	
	private class UINewGamePanel extends JPanel implements MouseListener, MouseMotionListener {

		private static final long serialVersionUID = 7380205004739956983L;
		private UIResourcesLoader uiResourcesLoader;
		private Graphics graphics;
		private BufferedImage background;
		public int game_type = -1;
		public int players_type = -1;
		public int network_type = -1;
		private boolean readyToPlayLocal = false;
		private boolean readyToPlayNetwork = false;
		
		public UINewGamePanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.newgame_bg;
		}
		
		@Override
		public Dimension getPreferredSize() {
			if (background != null) {
				int width = background.getWidth();
				int height = background.getHeight();
				return new Dimension(width, height);
			}
			return super.getPreferredSize();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(graphics = g); // clear off-screen bitmap

			// draws the game elements
			if (background != null) {
				graphics.drawImage(background, 0, 0, this);
				
				// draw game type
				if(game_type != -1) {
					Coord c = uiResourcesLoader.new_game_btns_coords[game_type];
					Image selected_btn = uiResourcesLoader.getSelectedNewGameBtn(game_type);
					graphics.drawImage(selected_btn, c.x, c.y, this);
					
					// check which buttons it should draw
					if(game_type == UIResourcesLoader.LOCAL_GAME) {
						c = uiResourcesLoader.new_game_btns_coords[UIResourcesLoader.HUM_HUM_GAME];
						Image btn = uiResourcesLoader.getUnselectedNewGameBtn(UIResourcesLoader.HUM_HUM_GAME);
						graphics.drawImage(btn, c.x, c.y, this);
						
						c = uiResourcesLoader.new_game_btns_coords[UIResourcesLoader.HUM_CPU_GAME];
						btn = uiResourcesLoader.getUnselectedNewGameBtn(UIResourcesLoader.HUM_CPU_GAME);
						graphics.drawImage(btn, c.x, c.y, this);
						
						c = uiResourcesLoader.new_game_btns_coords[UIResourcesLoader.CPU_CPU_GAME];
						btn = uiResourcesLoader.getUnselectedNewGameBtn(UIResourcesLoader.CPU_CPU_GAME);
						graphics.drawImage(btn, c.x, c.y, this);
						
						// if a button is pressed, draw it selected
						if(players_type != -1) {
							c = uiResourcesLoader.new_game_btns_coords[players_type];
							selected_btn = uiResourcesLoader.getSelectedNewGameBtn(players_type);
							graphics.drawImage(selected_btn, c.x, c.y, this);
						}
						
						// if the type of players is chosen, it can start a new game
						if(readyToPlayLocal) {
							drawStartGameBtn();
						}
						
					} else if(game_type == UIResourcesLoader.NETWORK_GAME) {
						c = uiResourcesLoader.new_game_btns_coords[UIResourcesLoader.SERVER_GAME];
						Image btn = uiResourcesLoader.getUnselectedNewGameBtn(UIResourcesLoader.SERVER_GAME);
						graphics.drawImage(btn, c.x, c.y, this);
						
						c = uiResourcesLoader.new_game_btns_coords[UIResourcesLoader.CLIENT_GAME];
						btn = uiResourcesLoader.getUnselectedNewGameBtn(UIResourcesLoader.CLIENT_GAME);
						graphics.drawImage(btn, c.x, c.y, this);
						
						// if a button is pressed, draw it selected
						if(network_type != -1) {
							c = uiResourcesLoader.new_game_btns_coords[network_type];
							selected_btn = uiResourcesLoader.getSelectedNewGameBtn(network_type);
							graphics.drawImage(selected_btn, c.x, c.y, this);
						}
						
						// if the type of players is chosen, it can start a new game
						if(readyToPlayNetwork) {
							drawStartGameBtn();
						}
					}
				}
			}
		}
		
		private void drawStartGameBtn() {
			GameImage startGameBtn = uiResourcesLoader.startGameBtn;
			graphics.drawImage(startGameBtn.image, startGameBtn.coord.x, startGameBtn.coord.y, this);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(currentMenuState == MenuState.NewGame) {
				int x = e.getX();
				int y = e.getY();
				//System.out.println("X: "+x+" Y: "+y);
				
				if(x > 30 && y > 643 && x < 79 && y < 690) { // back to main menu
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(mainCfg, 1f)
						//.setStartSide(SLSide.RIGHT, uiNewGamePanel)
						.setEndSide(SLSide.RIGHT, uiNewGamePanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.Main;
						}}))
						.play();
					}}.run();
				} else if(x >= 373 && y >= 646 && x <= 417 && y <= 691) { // start game
					boolean startGame = false;
					if(game_type == UIResourcesLoader.LOCAL_GAME && readyToPlayLocal) {
						startGame = true;
					} else if(game_type == UIResourcesLoader.NETWORK_GAME && readyToPlayNetwork) {
						startGame = true;
					}
					if(startGame) {
						uiGamePanel.clearPossibleGame();
						if(game_type == UIResourcesLoader.LOCAL_GAME) {
							uiGamePanel.startGame();
						}
						new Runnable() {@Override public void run() {
							panel.createTransition()
							.push(new SLKeyframe(GameCfg, 1f)
							.setStartSide(SLSide.RIGHT, uiGamePanel)
							.setEndSide(SLSide.LEFT, uiMainMenuPanel)
							.setEndSide(SLSide.RIGHT, uiNewGamePanel)
							.setDelay(1.0f, uiNewGamePanel)
							.setDelay(0.3f, uiMainMenuPanel)
							.setCallback(new SLKeyframe.Callback() {@Override public void done() {
								currentMenuState = MenuState.Game;
								if(game_type == UIResourcesLoader.NETWORK_GAME) {
									uiGamePanel.startGame();
								}
							}}))
							.play();
						}}.run();
					}
				} else {
					Coord[] coords = uiResourcesLoader.new_game_btns_coords;
					for(int i = 0; i < coords.length; i++) {
						Coord c = coords[i];
						if(x >= c.x && y >= c.y && x < (c.x + 100) && y < (c.y + 100)) { // a btn was pressed
							if(i <= UIResourcesLoader.NETWORK_GAME) { // choosing type of game
								game_type = i;
							} else if(i > UIResourcesLoader.CLIENT_GAME && i <= UIResourcesLoader.CPU_CPU_GAME) { // choosing type of player
								if(game_type == UIResourcesLoader.LOCAL_GAME) {
									readyToPlayLocal = true;
									players_type = i;
								}
							} else { // choosing type of network
								if(game_type == UIResourcesLoader.NETWORK_GAME) {
									readyToPlayNetwork = true;
									network_type = i;
								}
							}
							repaint();
							break;
						}
					}
				}
			}
		}

		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void mouseDragged(MouseEvent e) {}
		@Override public void mouseMoved(MouseEvent e) {}
	}
	
	private class UIGamePanel extends JPanel implements MouseListener {

		private static final long serialVersionUID = -7973208111694509132L;
		private UIResourcesLoader uiResourcesLoader;
		private BufferedImage background;
		private Graphics graphics;
		public boolean hasGameRunning = false;
		private boolean millWasMade = false;
		private boolean waitingForAI = false;
		private boolean gameIsOver = false;
		private boolean waitingForConnection = true;
		private boolean showingResetWarning = false;
		private String winner = "";
		private GameServer gServer = null;
		private GameClient gClient = null;
		private Token[] boardPositions;
		private int selectedPiece = -1;
		private int game_type = -1;
		private Game game;
		private Image turnPlayer = null;
		private Thread bgGameCheckThread;
		
		public UIGamePanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.game_bg;
		}
		
		private class LocalGameRunnable implements Runnable {
			@Override
			public void run() {
				while(true) {
					if(waitingForAI) {
						makeAiMove();
					} else {
						try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
					}
				}
			}
		}
		
		private class NetworkGameRunnable implements Runnable {
			@Override
			public void run() {
				while(true) {
					if(!((NetworkGame)game).isThisPlayerTurn()) {
						//System.out.println("IT'S NOT MY TURN");
						if(gClient.isThisPlayerTurn()) {
							
							// update game with opponent moves
							try {
								Log.info("Updating game with opponent moves");
								ArrayList<Move> opponentMoves = gClient.getOpponentMoves();
								((NetworkGame)game).updateGameWithOpponentMoves(opponentMoves);
								((NetworkGame)game).setTurn(true);
								turnPlayer = uiResourcesLoader.getPlayerTurn(game.getPlayer().getPlayerToken());
								
								if(game.getCurrentGamePhase() != Game.PLACING_PHASE && game.isTheGameOver()) {
									Token opp = (game.getPlayer().getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
									Log.info("Game Over! "+opp+" won!");
									gameIsOver = true;
									winner = (opp == Token.PLAYER_1) ? "p1" : "p2";
								}
								repaint();
								gClient.stop();
								if(gServer != null) {
									gServer.stop();
								}
							} catch (GameException e) {
								e.printStackTrace();
								System.exit(-1);
							}
						}
					}
					try { Thread.sleep(100); } catch (InterruptedException e1) { e1.printStackTrace(); }
				}
			}
		}
		
		public void startGame() {
			hasGameRunning = true;
			
			// this is used due to some graphics problems of panel transitions of local games with AI
			boardPositions = new Token[Board.NUM_POSITIONS_OF_BOARD];
			for(int i = 0; i < boardPositions.length; i++) {
				boardPositions[i] = Token.NO_PLAYER; 
			}
			
			try {
				if(uiNewGamePanel.game_type == UIResourcesLoader.LOCAL_GAME) {
					game_type = UIResourcesLoader.LOCAL_GAME;
					createLocalGame();
				} else if(uiNewGamePanel.game_type == UIResourcesLoader.NETWORK_GAME) {
					game_type = UIResourcesLoader.NETWORK_GAME;
					createNetworkGame();
				}
			} catch(GameException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			repaint();
		}
		
		public void clearPossibleGame() {
			hasGameRunning = false;
			millWasMade = false;
			waitingForAI = false;
			gameIsOver = false;
			waitingForConnection = true;
			showingResetWarning = false;
			winner = "";
			gServer = null;
			gClient = null;
			boardPositions = new Token[Board.NUM_POSITIONS_OF_BOARD];
			for(int i = 0; i < boardPositions.length; i++) {
				boardPositions[i] = Token.NO_PLAYER; 
			}
			selectedPiece = -1;
			game_type = -1;
			game = null;
			turnPlayer = null;
			if(bgGameCheckThread != null) {
				bgGameCheckThread.stop(); // it's not a problem for our game
			}
		}

		private void createLocalGame() throws GameException {
			game = new LocalGame();
			Player p1 = null, p2 = null;
			
			if(uiNewGamePanel.players_type == UIResourcesLoader.HUM_HUM_GAME) {
				p1 = new HumanPlayer("Player 1", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
				p2 = new HumanPlayer("Player 2", Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER);
			} else if(uiNewGamePanel.players_type == UIResourcesLoader.HUM_CPU_GAME) {
				System.out.println("Creating new HUM-CPU game!");
				p1 = new HumanPlayer("Player 1", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
				p2 = createAIPlayer(Token.PLAYER_2, uiSettingsPanel.gameLevelCPU1);
			} else if(uiNewGamePanel.players_type == UIResourcesLoader.CPU_CPU_GAME) {
				p1 = createAIPlayer(Token.PLAYER_1, uiSettingsPanel.gameLevelCPU1);
				p2 = createAIPlayer(Token.PLAYER_2, uiSettingsPanel.gameLevelCPU2);
			}
			((LocalGame)game).setPlayers(p1, p2);
			turnPlayer = uiResourcesLoader.getPlayerTurn(p1.getPlayerToken());
			
			System.out.println("Starting a new game");
			if(p1.isAI()) {
				waitingForAI = true;
			}
			
			bgGameCheckThread = new Thread(new LocalGameRunnable());
			bgGameCheckThread.start();
		}
		
		private IAPlayer createAIPlayer(Token player, int playerLevel) {
			try {
				if(playerLevel == UISettingsPanel.VERY_EASY) {
					return new RandomIAPlayer(player, Game.NUM_PIECES_PER_PLAYER);
				} else {
					return new MinimaxIAPlayer(player, Game.NUM_PIECES_PER_PLAYER, playerLevel+2); // TODO temporary
				}
			} catch(GameException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			return null;
		}
		
		private void createNetworkGame() throws GameException, IOException {
			game = new NetworkGame();
			Player p = null;
			
			if(uiNewGamePanel.network_type == UIResourcesLoader.SERVER_GAME) {
				gServer = new GameServer();
				gClient = new GameClient(Token.PLAYER_1);
				p = new HumanPlayer("Player1", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
				
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
				
			} else if(uiNewGamePanel.network_type == UIResourcesLoader.CLIENT_GAME) {
				gClient = new GameClient(Token.PLAYER_2);
				p = new HumanPlayer("Player2", Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER);
			}
			
			((NetworkGame)game).setPlayer(p);
			int numberTries = 3;
			
			if(gServer == null) { // this is only a client trying to connect
				boolean backMenu = false;
				while(true) {
					String ip = JOptionPane.showInputDialog(null, "Connect to GameServer at IP address:","localhost");
					try {
						if(ip != null) {
							gClient.connectToServer(ip);
							break;
						}
						else {
							backMenu = true;
							break;
						}
					} catch (Exception e){
						Log.info("No GameServer detected!");
						if(--numberTries == 0) {
							Log.info("No more tries. Going back to main menu.");
							backMenu = true;
							break;
						}
						try { Thread.sleep(50); } catch (InterruptedException e1) { e1.printStackTrace(); }
						Log.info("Trying another connection.");
					}
				}
				if(backMenu) {
					gClient.stop();
					clearPossibleGame();
					goBackToMenu(); // TODO not working
					return;
				}
			} else { // this computer has the GameServer
				gClient.connectToServer("localhost");
				
				while(true) {
					if(gServer.hasConnectionEstablished()){
						break;
					}
					if(numberTries-- == 15) {
						Log.info("Server initialized. Waiting for connection from GameClient of this computer");
					}
					try { Thread.sleep(50); } catch (InterruptedException e1) { e1.printStackTrace(); }
				}
			}
			
			while(gClient.isWaitingForGameStart()) {
				Log.info("Waiting for game to start");
				try { Thread.sleep(200); } catch (InterruptedException e1) { e1.printStackTrace(); }
			}
			
			turnPlayer = uiResourcesLoader.getPlayerTurn(gClient.getPlayerThatPlaysFirst());
			waitingForConnection = false;
			repaint();
			
			Log.info("Game is starting");
			if(gClient.getPlayerThatPlaysFirst() == p.getPlayerToken()) {
				Log.info("I'm the one who plays first");
				((NetworkGame)game).setTurn(true);
			}
			
			bgGameCheckThread = new Thread(new NetworkGameRunnable());
			bgGameCheckThread.start();
		}
		
		private void makeAiMove() {
			Player p = game.getPlayer();
			int indexToTest = -1;
			
			try {
				if(game.getCurrentGamePhase() == Game.PLACING_PHASE) {
					int boardIndex = indexToTest = ((IAPlayer)p).getIndexToPlacePiece(game.getGameBoard());
					if(game.placePieceOfPlayer(boardIndex, p.getPlayerToken())) {
						p.raiseNumPiecesOnBoard();
						boardPositions[boardIndex] = p.getPlayerToken();
						repaint();
						Log.info(p.getName()+" placed piece on "+boardIndex);
					}
				} else {
					Move move = ((IAPlayer)p).getPieceMove(game.getGameBoard(), game.getCurrentGamePhase());
					if(game.movePieceFromTo(move.srcIndex, (indexToTest = move.destIndex), p.getPlayerToken()) == Game.VALID_MOVE) {
						boardPositions[move.srcIndex] = Token.NO_PLAYER;
						boardPositions[move.destIndex] = p.getPlayerToken();
						Log.info(p.getName()+" moved piece from "+move.srcIndex+" to " + move.destIndex);
					}
				}
				
				if(game.madeAMill(indexToTest, p.getPlayerToken())) {
					Token opponentPlayer = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;
					int boardIndex = ((IAPlayer)p).getIndexToRemovePieceOfOpponent(game.getGameBoard());
					if(game.removePiece(boardIndex, opponentPlayer)) {
						boardPositions[boardIndex] = Token.NO_PLAYER;
						Log.info(p.getPlayerToken()+" removes opponent piece on board index: "+boardIndex);
					}
				}
				waitingForAI = false;
				if(game.getCurrentGamePhase() != Game.PLACING_PHASE && game.isTheGameOver()) {
					Log.info("Game Over! "+p.getPlayerToken()+" won");
					gameIsOver = true;
					winner = (p.getPlayerToken() == Token.PLAYER_1) ? "p1" : "p2";
				} else {
					updateLocalGameTurn();
				}
				repaint();

			} catch(GameException e){
				e.printStackTrace();
				System.exit(-1);
			}
		}
		
		private void updateLocalGameTurn() throws GameException {
			((LocalGame)game).updateCurrentTurnPlayer();
			turnPlayer = uiResourcesLoader.getPlayerTurn(game.getPlayer().getPlayerToken());
			waitingForAI = game.getPlayer().isAI();
			repaint();
		}
		
		@Override
		public Dimension getPreferredSize() {
			if (background != null) {
				int width = background.getWidth();
				int height = background.getHeight();
				return new Dimension(width, height);
			}
			return super.getPreferredSize();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(graphics = g); // clear off-screen bitmap
			if (background != null) {
				graphics.drawImage(background, 0, 0, this);
				
				if(uiNewGamePanel.game_type == UIResourcesLoader.NETWORK_GAME && waitingForConnection) {
					GameImage waitConnection = uiResourcesLoader.waitingForConnection;
					graphics.drawImage(waitConnection.image, waitConnection.coord.x, waitConnection.coord.y, this);
				}

				if(hasGameRunning) {
					try {
						for(int i = 0; i < boardPositions.length; i++) {
							if(game_type == UIResourcesLoader.LOCAL_GAME) {
								if(boardPositions[i] != Token.NO_PLAYER) {
									Coord c = uiResourcesLoader.board_positions_coords[i];
									Image piece = uiResourcesLoader.getUnselectedPiece(boardPositions[i]);
									graphics.drawImage(piece, c.x, c.y, this);
									if(i == selectedPiece) {
										piece = uiResourcesLoader.getSelectedPiece(boardPositions[i]);
										graphics.drawImage(piece, c.x - 15, c.y - 15, this);
									}
								}
							} else if(game_type == UIResourcesLoader.NETWORK_GAME) {
								Token playerPos = game.getPlayerInBoardPosition(i);
								if(playerPos != Token.NO_PLAYER) {
									Coord c = uiResourcesLoader.board_positions_coords[i];
									Image piece = uiResourcesLoader.getUnselectedPiece(playerPos);
									graphics.drawImage(piece, c.x, c.y, this);
									if(i == selectedPiece) {
										piece = uiResourcesLoader.getSelectedPiece(playerPos);
										graphics.drawImage(piece, c.x - 15, c.y - 15, this);
									}
								}
							}
						}
						
						if(turnPlayer != null) {
							Coord coord = uiResourcesLoader.turn_coord;
							graphics.drawImage(turnPlayer, coord.x, coord.y, this);
						}
						
						// draw game phase
						Coord coord = uiResourcesLoader.game_phase_coord;
						Image str = uiResourcesLoader.getGamePhaseStr(game.getCurrentGamePhase());
						graphics.drawImage(str,coord.x,coord.y,this);
						
						if(waitingForAI) {
							graphics.drawImage(uiResourcesLoader.getGameStatus("waitingAI"), uiResourcesLoader.game_status_coord.x, uiResourcesLoader.game_status_coord.y, this);
						}
						
						
						// draw game status
						Image status = null;
						if(gameIsOver) {
							status = uiResourcesLoader.getGameStatus(winner);
							graphics.drawImage(status, uiResourcesLoader.game_status_coord.x, uiResourcesLoader.game_status_coord.y, this);
						} else if(game_type == UIResourcesLoader.NETWORK_GAME
							|| (game_type == UIResourcesLoader.LOCAL_GAME 
								&& (uiNewGamePanel.players_type == UIResourcesLoader.HUM_HUM_GAME
									|| (uiNewGamePanel.players_type == UIResourcesLoader.HUM_CPU_GAME && game.getPlayer().getPlayerToken() == Token.PLAYER_1)))) {
							
							if(millWasMade) {
								status = uiResourcesLoader.getGameStatus("remove");
							} else if(selectedPiece != -1) {
								if(game.getPlayer().canItFly()) {
									status = uiResourcesLoader.getGameStatus("fly");
								} else {
									status = uiResourcesLoader.getGameStatus("move");
								}
							} else if(game.getCurrentGamePhase() == Game.PLACING_PHASE) {
								status = uiResourcesLoader.getGameStatus("place");
							} else if(game.getCurrentGamePhase() == Game.MOVING_PHASE || game.getCurrentGamePhase() == Game.FLYING_PHASE) {
								status = uiResourcesLoader.getGameStatus("select");
							}
							graphics.drawImage(status, uiResourcesLoader.game_status_coord.x, uiResourcesLoader.game_status_coord.y, this);
						}
									
						// draw the you string
						if(game_type == UIResourcesLoader.NETWORK_GAME) {
							Coord c = uiResourcesLoader.getPlayerYouStrCoord(game.getPlayer().getPlayerToken());
							graphics.drawImage(uiResourcesLoader.youStr.image, c.x, c.y, this);
						} else if(uiNewGamePanel.players_type == UIResourcesLoader.HUM_CPU_GAME) {
							Coord c = uiResourcesLoader.getPlayerYouStrCoord(Token.PLAYER_1);
							graphics.drawImage(uiResourcesLoader.youStr.image, c.x, c.y, this);
						}
						
						if(showingResetWarning) {
							GameImage reset = uiResourcesLoader.confirmReset;
							graphics.drawImage(reset.image, reset.coord.x, reset.coord.y, this);
						}
						
					} catch (GameException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}

		private void goBackToMenu() {
			new Runnable() {@Override public void run() {
				panel.createTransition()
				.push(new SLKeyframe(mainCfg, 2f)
				.setStartSide(SLSide.LEFT, uiMainMenuPanel)
				.setEndSide(SLSide.RIGHT, uiGamePanel)
				.setCallback(new SLKeyframe.Callback() {@Override public void done() {
					currentMenuState = MenuState.Main;
				}}))
				.play();
			}}.run();
		}
		
		private void movingPhase(int boardIndex, Player player) throws GameException {
			boolean invalidMove = false;
			
			// first click selects a piece, the next one selects destination
			if(selectedPiece == -1) {
				if(game.positionHasPieceOfPlayer(boardIndex, player.getPlayerToken())) {
					selectedPiece = boardIndex;
					repaint();
				} else {
					Log.info(player.getPlayerToken()+" doesn't have a piece on board index: "+boardIndex);
				}
			} else { // a piece is selected, we just need a valid destination
				if(selectedPiece == boardIndex) { // unselect piece
					Log.info("Piece on board index: "+boardIndex+" was unselected");
					selectedPiece = -1;
					repaint();
				} else {
					if(game_type == UIResourcesLoader.LOCAL_GAME || (gClient != null && gClient.validatePieceMoving(selectedPiece, boardIndex))) {
						if(game.movePieceFromTo(selectedPiece, boardIndex, player.getPlayerToken()) == Game.VALID_MOVE) {
							if(game_type == UIResourcesLoader.LOCAL_GAME) {
								boardPositions[boardIndex] = player.getPlayerToken();
								boardPositions[selectedPiece] = Token.NO_PLAYER;
							}
							selectedPiece = -1;
							repaint();
							
							if(game.madeAMill(boardIndex, player.getPlayerToken())) {
								millWasMade = true;
							} else {
								if(game.getCurrentGamePhase() != Game.PLACING_PHASE && game.isTheGameOver()) {
									Log.info("Game Over! "+player.getPlayerToken()+" won");
									gameIsOver = true;
									winner = (player.getPlayerToken() == Token.PLAYER_1) ? "p1" : "p2";
								}
								if(game_type == UIResourcesLoader.LOCAL_GAME) {
									updateLocalGameTurn();
								} else {
									Log.info("Entering here!"); // TODO REMOVE
									((NetworkGame)game).setTurn(false);
									turnPlayer = uiResourcesLoader.getPlayerTurn(player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
								}
							}	
						} else {
							invalidMove = true;
						}
					} else {
						invalidMove = true;
					}
				}
			}
			
			if(invalidMove) {
				if(game.positionHasPieceOfPlayer(boardIndex, player.getPlayerToken())) {
					selectedPiece = boardIndex;
					repaint();
				} else {
					Log.info("Invalid move");
				}
			}
		}
		
		private void resetGame() {
			if(game_type == UIResourcesLoader.LOCAL_GAME) {
				clearPossibleGame();
				try {
					hasGameRunning = true;
					game_type = UIResourcesLoader.LOCAL_GAME;
					createLocalGame();
				} catch (GameException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			System.out.println("X: "+x+" Y: "+y);

			if(x >= 14 && y >= 668 && x <= 58 && y <= 709) { // return to main menu
				goBackToMenu();
			} else if(x >= 1225 && y >= 664 && x <= 1266 && y <= 708) { // reset game
				if(!gameIsOver) {
					showingResetWarning = true;
				} else {
					resetGame();
				}
				repaint();
			} else {
				if(!gameIsOver) {
					try {
						if(!showingResetWarning) {
							Coord[] board_positions = uiResourcesLoader.board_positions_coords;

							for(int i = 0; i < board_positions.length; i++) {
								Coord coord = board_positions[i];
								if(x >= coord.x && y >= coord.y && x <= (coord.x + 32) && y <= (coord.y + 32)) { // player has clicked in a board position
									Log.info("Clicked in a board position: "+i);
									Player p = game.getPlayer();

									// it's waiting for a piece removal
									if(millWasMade) { 
										Token oppToken = (p.getPlayerToken() == Token.PLAYER_1) ? Token.PLAYER_2 : Token.PLAYER_1;

										if(game_type == UIResourcesLoader.LOCAL_GAME) {
											if(game.removePiece(i, oppToken)) {
												boardPositions[i] = Token.NO_PLAYER; 
												millWasMade = false;
												if(game.getCurrentGamePhase() != Game.PLACING_PHASE && game.isTheGameOver()) {
													Log.info("Game Over! "+p.getPlayerToken()+" won");
													gameIsOver = true;
													winner = (p.getPlayerToken() == Token.PLAYER_1) ? "p1" : "p2";
												}
												updateLocalGameTurn();
											} else {
												Log.info("You can't remove a piece from there. Try again");
											}
										} else if(game_type == UIResourcesLoader.NETWORK_GAME) {
											if(gClient.validatePieceRemoving(i)) {
												if(game.removePiece(i, oppToken)) {
													millWasMade = false;
													if(game.getCurrentGamePhase() != Game.PLACING_PHASE && game.isTheGameOver()) {
														Log.info("Game Over! You won");
														gameIsOver = true;
														winner =  (p.getPlayerToken() == Token.PLAYER_1) ? "p1" : "p2";
													}
													((NetworkGame)game).setTurn(false);
													turnPlayer = uiResourcesLoader.getPlayerTurn(p.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
												} else {
													Log.info("You can't remove a piece from there. Try again");
												}
											} else {
												Log.info("The server has considered that move invalid. Try again");
											}
										}
										repaint();
									} else if(game_type == UIResourcesLoader.LOCAL_GAME) {
										if(!waitingForAI) {
											if(game.getCurrentGamePhase() == Game.PLACING_PHASE) {
												if(game.placePieceOfPlayer(i, p.getPlayerToken())) {
													boardPositions[i] = p.getPlayerToken();
													p.raiseNumPiecesOnBoard();

													if(game.madeAMill(i, p.getPlayerToken())) {
														millWasMade = true; // needs to wait for at least another click
														repaint();
													} else {
														updateLocalGameTurn();
													}
												} else {
													System.out.println("You can't place a piece there. Try again");
												}
											} else if(game.getCurrentGamePhase() == Game.MOVING_PHASE || game.getCurrentGamePhase() == Game.FLYING_PHASE) {
												movingPhase(i, p);
											}
										}
									} else if(game_type == UIResourcesLoader.NETWORK_GAME) {
										if(((NetworkGame)game).isThisPlayerTurn()) {
											Player player = game.getPlayer();

											if(game.getCurrentGamePhase() == Game.PLACING_PHASE) {
												if(gClient.validatePiecePlacing(i)) { // validate placing with the server
													if(game.placePieceOfPlayer(i, player.getPlayerToken())) {
														if(game.madeAMill(i, player.getPlayerToken())) {
															millWasMade = true;
														} else {
															((NetworkGame)game).setTurn(false);
															turnPlayer = uiResourcesLoader.getPlayerTurn(player.getPlayerToken() == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1);
														}
														repaint();
													} else {
														Log.warn("The placing was considered valid with the server, but not locally");
													}
												} else {
													Log.info("The server has considered that move invalid. Try again");
												}
											} else if(game.getCurrentGamePhase() == Game.MOVING_PHASE || game.getCurrentGamePhase() == Game.FLYING_PHASE) {
												movingPhase(i, player);
											}
										}
									}
									break;
								}
							}
						} else { // it's showing the reset warning
							if(x >= 499 && y >= 407 && x <= 593 && y <= 446) { // yes
								resetGame();
								repaint();
							} else if(x >= 686 && y >= 407 && x <= 780 && y <= 446) {
								showingResetWarning = false;
								repaint();
							}
						}
					} catch(GameException exc){
						exc.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
	}
	
	private class UIMainMenuPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
		
		private static final long serialVersionUID = -1237601154927560866L;
		private UIResourcesLoader uiResourcesLoader;
		private Graphics graphics;
		private BufferedImage background;
		
		public UIMainMenuPanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.mainmenu_bg;
		}
		
		@Override
		public Dimension getPreferredSize() {
			if (background != null) {
				int width = background.getWidth();
				int height = background.getHeight();
				return new Dimension(width, height);
			}
			return super.getPreferredSize();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(graphics = g); // clear off-screen bitmap
						
			// draws the game elements
			if (background != null) {
				graphics.drawImage(background, 0, 0, this);
				
				if(uiGamePanel.hasGameRunning) {
					GameImage returnToGameBtn = uiResourcesLoader.returnToGameBtn;
					graphics.drawImage(returnToGameBtn.image, returnToGameBtn.coord.x, returnToGameBtn.coord.y, this);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(currentMenuState == MenuState.Main) {
				//int x = e.getX();
				//int y = e.getY();
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(currentMenuState == MenuState.Main) {
				int x = e.getX();
				int y = e.getY();
//				System.out.println("X: "+x+" Y: "+y);

				if (y > 189 && y < 327 && x > 397 && x < 716) { // new game
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(NewGameCfg, 1f)
						.setStartSide(SLSide.RIGHT, uiNewGamePanel)
						//.setEndSide(SLSide.LEFT, uiMainMenuPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.NewGame;
						}}))
						.play();
					}}.run();
				} else if (x > 101 && y > 485 && x < 238 && y < 622) { // settings
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(SettingsCfg, 1f)
						.setStartSide(SLSide.LEFT, uiSettingsPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.Settings;
						}}))
						.play();
					}}.run();
				} else if(x > 249 && y > 485 && x < 386 && y < 622) { // source code
					WebPage.open("https://bitbucket.org/miguelgazela/ninemensmorris");
				}else if (y > 485 && y < 622 && x > 397 && x < 716) { // exit game
					System.exit(0);
				} else if (x > 397 && x < 551 && y > 337 && y < 474) { // about info
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(AboutCfg, 2f)
						.setStartSide(SLSide.BOTTOM, uiAboutPanel)
						.setEndSide(SLSide.TOP, uiMainMenuPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.About;
						}}))
						.play();
					}}.run();
				}
				else if (y > 337 && y < 474 && x > 562 && x < 716) { // dev. team
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(DevTeamCfg, 2f)
						.setStartSide(SLSide.TOP, uiDevTeamPanel)
						.setEndSide(SLSide.BOTTOM, uiMainMenuPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.DevTeam;
						}}))
						.play();
					}}.run();
				} else if(x >= 1225 && y >= 668 && x <= 1267 && y <= 711) { // return to game
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(GameCfg, 2f)
						.setStartSide(SLSide.RIGHT, uiGamePanel)
						.setEndSide(SLSide.LEFT, uiMainMenuPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.Game;
						}}))
						.play();
					}}.run();
				}
			}
		}

		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void keyTyped(KeyEvent e) {}
		@Override public void keyPressed(KeyEvent e) {}
		@Override public void keyReleased(KeyEvent e) {}
		@Override public void mouseDragged(MouseEvent e) {}
	}
}
