package GameUI;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GameLogic.Board;
import GameLogic.Game;
import GameLogic.GameException;
import GameLogic.HumanPlayer;
import GameLogic.LocalGame;
import GameLogic.MinimaxIAPlayer;
import GameLogic.NetworkGame;
import GameLogic.Player;
import GameLogic.Token;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;


public class UIGameMenu extends JFrame {
	private static final long serialVersionUID = -5256114500541984237L;
	private SLPanel panel;
	private final UIMainMenuPanel uiMainMenuPanel;
	private UISettingsPanel uiSettingsPanel;
	private UIAboutPanel uiAboutPanel;
	private UINewGamePanel uiNewGamePanel;
	private UIDevTeamPanel uiDevTeamPanel;
	private UIGamePanel uiGamePanel;
	private SLConfig mainCfg, SettingsCfg, AboutCfg, NewGameCfg, DevTeamCfg, GameCfg;
	private MenuState currentMenuState;
	
	public UIGameMenu() {
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
		
		setSize(1280, 720);
		setUndecorated(true);
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

	private enum MenuState {
		Main, About, Settings, DevTeam, NewGame, Game;
	}
	
	private void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	private void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
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
		private Image game_level_check;
		private int game_level = UISettingsPanel.NORMAL;
		
		public UISettingsPanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.settings_bg;
			game_level_check = uiResourcesLoader.game_level_check;
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
				Coord c = uiResourcesLoader.game_level_check_coords[game_level];
				graphics.drawImage(game_level_check, c.x, c.y, this);
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
				
				if(x > 348 && y > 643 && x < 398 && y < 690) {
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
				} else if(x >= 70 && y >= 210 && x <= 127 && y <= 267) { // very easy
					game_level = UISettingsPanel.VERY_EASY;
				} else if(x >= 70 && y >= 283 && x <= 127 && y <= 340) { // easy
					game_level = UISettingsPanel.EASY;
				} else if(x >= 70 && y >= 356 && x <= 127 && y <= 413) { // normal
					game_level = UISettingsPanel.NORMAL;
				} else if(x >= 70 && y >= 429 && x <= 127 && y <= 486) { // hard
					game_level = UISettingsPanel.HARD;
				} else if(x >= 70 && y >= 502 && x <= 127 && y <= 559) { // very hard
					game_level = UISettingsPanel.VERY_HARD;
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

			if(x > 1200 && y > 642 && x < 1249 && y < 689) {
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
			Coord c = uiResourcesLoader.start_game_btn_coord;
			Image btn = uiResourcesLoader.start_game;
			graphics.drawImage(btn, c.x, c.y, this);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(currentMenuState == MenuState.NewGame) {
				int x = e.getX();
				int y = e.getY();
				System.out.println("X: "+x+" Y: "+y);
				
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
								uiGamePanel.startGame();
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
		private Game game;
		
		public UIGamePanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.game_bg;
		}
		
		public void startGame() {
			hasGameRunning = true;
			try {
				if(uiNewGamePanel.game_type == UIResourcesLoader.LOCAL_GAME) {
					createLocalGame();
				} else if(uiNewGamePanel.game_type == UIResourcesLoader.NETWORK_GAME) {
					createNetworkGame();
				}
			} catch(GameException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		private void createLocalGame() throws GameException {
			game = new LocalGame();
			Player p1 = null, p2 = null;
			
			if(uiNewGamePanel.players_type == UIResourcesLoader.HUM_HUM_GAME) {
				p1 = new HumanPlayer("Player 1", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
				p2 = new HumanPlayer("Player 2", Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER);
			} else if(uiNewGamePanel.players_type == UIResourcesLoader.HUM_CPU_GAME) {
				p1 = new HumanPlayer("Player 1", Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER);
				p2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 4); // TODO: has to see the level of the difficulty
			} else if(uiNewGamePanel.players_type == UIResourcesLoader.CPU_CPU_GAME) {
				p1 = new MinimaxIAPlayer(Token.PLAYER_1, Game.NUM_PIECES_PER_PLAYER, 4);
				p2 = new MinimaxIAPlayer(Token.PLAYER_2, Game.NUM_PIECES_PER_PLAYER, 4);
			}
			((LocalGame)game).setPlayers(p1, p2);
		}
		
		private void createNetworkGame() {
			game = new NetworkGame();
			System.out.println("Class of game: "+game.getClass());
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
				
				for(int i = 0; i < 24; i++) {
					Coord c = uiResourcesLoader.board_positions_coords[i];
					try {
						graphics.drawImage(uiResourcesLoader.getUnselectedPiece(Token.PLAYER_1), c.x, c.y, this);
					} catch (GameException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			System.out.println("X: "+x+" Y: "+y);

			if(x >= 14 && y >= 668 && x <= 58 && y <= 709) {
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
					Image btn = uiResourcesLoader.return_game;
					Coord coord = uiResourcesLoader.return_game_btn_coord;
					graphics.drawImage(btn, coord.x, coord.y, this);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if(currentMenuState == MenuState.Main) {
				int x = e.getX();
				int y = e.getY();
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
				} else if (y > 485 && y < 622 && x > 101 && x < 386) { // settings
					new Runnable() {@Override public void run() {
						panel.createTransition()
						.push(new SLKeyframe(SettingsCfg, 1f)
						.setStartSide(SLSide.LEFT, uiSettingsPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							currentMenuState = MenuState.Settings;
						}}))
						.play();
					}}.run();
				} else if (y > 485 && y < 622 && x > 397 && x < 716) { // exit game
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

		@Override
		public void mousePressed(MouseEvent e) {
			if(currentMenuState == MenuState.Main) {
				int x = e.getX();
				int y = e.getY();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(currentMenuState == MenuState.Main) {
				int x = e.getX();
				int y = e.getY();
			}
		}

		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void keyTyped(KeyEvent e) {}
		@Override public void keyPressed(KeyEvent e) {}
		@Override public void keyReleased(KeyEvent e) {}
		@Override public void mouseDragged(MouseEvent e) {}
	}
}
