package GameUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.slidinglayout.SLSide;


public class UIGameMenu extends JFrame {
	private static final long serialVersionUID = -5256114500541984237L;
	private SLPanel panel;
	private final UIGameMenuPanel uiGameMenuPanel;
	private UIOptionsPanel uiOptionsPanel;
	private UIAboutPanel uiAboutPanel;
	private SLConfig mainCfg, OptionsCfg, AboutCfg;
	
	public UIGameMenu() {
		super("Nine Men's Morris - by Afonso Caldas & Miguel Oliveira");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new SLPanel();
		getContentPane().add(panel);
		
		uiGameMenuPanel = new UIGameMenuPanel(this);
		uiGameMenuPanel.addMouseMotionListener(uiGameMenuPanel);
		uiGameMenuPanel.addMouseListener(uiGameMenuPanel);
		uiGameMenuPanel.addKeyListener(uiGameMenuPanel);
		uiOptionsPanel = new UIOptionsPanel();
		uiOptionsPanel.addMouseMotionListener(uiOptionsPanel);
		uiOptionsPanel.addMouseListener(uiOptionsPanel);
		uiAboutPanel = new UIAboutPanel();
		uiAboutPanel.addMouseListener(uiAboutPanel);
		
		mainCfg = new SLConfig(panel)
			.row(1f).col(1f)
			.place(0, 0, uiGameMenuPanel);
		
		OptionsCfg = new SLConfig(panel)
			.row(1f).col(1f)
			.place(0, 0, uiOptionsPanel);
		
		AboutCfg = new SLConfig(panel)
			.row(1f).col(1f)
			.place(0, 0, uiAboutPanel);
		
		panel.setTweenManager(SLAnimator.createTweenManager());
		panel.initialize(mainCfg);
		
		setSize(640, 480);
		setUndecorated(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private class UIOptionsPanel extends JPanel implements MouseListener, MouseMotionListener {
		private UIResourcesLoader uiResourcesLoader;
		private Graphics graphics;
		private BufferedImage background;
		
		public UIOptionsPanel() {
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.options_background;
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
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			new Runnable() {@Override public void run() {
				panel.createTransition()
					.push(new SLKeyframe(mainCfg, 2f)
						.setStartSide(SLSide.RIGHT, uiGameMenuPanel)
						.setEndSide(SLSide.LEFT, uiOptionsPanel)
						.setCallback(new SLKeyframe.Callback() {@Override public void done() {
							//p5.setAction(p5Action);
							//enableActions();
						}}))
					.play();
			}}.run();
		}
		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
	
	private class UIAboutPanel extends JPanel implements MouseListener {
		private UIResourcesLoader uirl;
		private BufferedImage background;
		private Graphics graphics;
		
		public UIAboutPanel() {
			uirl = UIResourcesLoader.getInstanceLoader();
			background = uirl.about_background;
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
			new Runnable() {@Override public void run() {
				panel.createTransition()
					.push(new SLKeyframe(mainCfg, 2f)
						.setStartSide(SLSide.TOP, uiGameMenuPanel)
						.setEndSide(SLSide.BOTTOM, uiAboutPanel))
					.play();
			}}.run();
		}
		@Override public void mousePressed(MouseEvent e) {}
		@Override public void mouseReleased(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
	}
	
	private class UIGameMenuPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
		
		private static final long serialVersionUID = -1237601154927560866L;
		private UIGameMenu uiGameMenu;
		private UIResourcesLoader uiResourcesLoader;
		private Graphics graphics;
		private BufferedImage background;
		private Image[] buttons_normal, buttons_hover, buttons_active, displayed_buttons;
		private Coord[] buttons_coords;
		private Image[] about_buttons;
		private Coord aboutButtonCoord;
		boolean changedButton;
		boolean pressedButton;
		int changedButtonIndex;
		
		public UIGameMenuPanel(UIGameMenu uiGameMenu) {
			this.uiGameMenu = uiGameMenu;
			uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
			background = uiResourcesLoader.main_menu_background;
			buttons_normal = uiResourcesLoader.getMainMenuButtons(UIResourcesLoader.MAIN_MENU_NORMAL);
			buttons_hover = uiResourcesLoader.getMainMenuButtons(UIResourcesLoader.MAIN_MENU_HOVER);
			buttons_active = uiResourcesLoader.getMainMenuButtons(UIResourcesLoader.MAIN_MENU_ACTIVE);
			buttons_coords = uiResourcesLoader.getMainMenuButtonsCoord();
			displayed_buttons = new Image[3];
			displayed_buttons[0] = buttons_normal[uiResourcesLoader.NEW_GAME_BTN];
			displayed_buttons[1] = buttons_normal[uiResourcesLoader.OPTIONS_BTN];
			displayed_buttons[2] = buttons_normal[uiResourcesLoader.EXIT_BTN];
			about_buttons = uiResourcesLoader.getAboutButtons();
			aboutButtonCoord = uiResourcesLoader.getAboutButtonCoord();
			changedButton = false;
			pressedButton = false;
			changedButtonIndex = -1;
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
				graphics.drawImage(about_buttons[0], aboutButtonCoord.x, aboutButtonCoord.y, this);
				// draw buttons
				for(int i = 0; i < displayed_buttons.length; i++) {
					graphics.drawImage(displayed_buttons[i], buttons_coords[i].x, buttons_coords[i].y, this);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			boolean mustRepaint = false;
			
			if(y > 314 && y < 343 && x > 163 && x < (163+85)) { // new game button
				mustRepaint = changeButtonToHover(uiResourcesLoader.NEW_GAME_BTN);
			} else if (y > 314 && y < 343 && x > 278 && x < (278+85)) { // options button
				mustRepaint = changeButtonToHover(uiResourcesLoader.OPTIONS_BTN);
			} else if (y > 314 && y < 343 && x > 393 && x < (393+85)) { // exit button
				mustRepaint = changeButtonToHover(uiResourcesLoader.EXIT_BTN);
			} else {
				if(changedButton) {
					displayed_buttons[changedButtonIndex] = buttons_normal[changedButtonIndex];
					changedButton = false;
					changedButtonIndex = -1;
					mustRepaint = true;
				}
			}
			if(mustRepaint) {
				repaint();
			}
		}
		
		private boolean changeButtonToHover(int buttonIndex) {
			if(changedButtonIndex != buttonIndex || pressedButton) {
				displayed_buttons[buttonIndex] = buttons_hover[buttonIndex];
				changedButton = true;
				changedButtonIndex = buttonIndex;
				return true;
			}
			return false;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			if (y > 314 && y < 343 && x > 278 && x < (278+85)) { // options
				new Runnable() {@Override public void run() {
					panel.createTransition()
						.push(new SLKeyframe(OptionsCfg, 2f)
							.setStartSide(SLSide.LEFT, uiOptionsPanel)
							.setEndSide(SLSide.RIGHT, uiGameMenuPanel)
							.setCallback(new SLKeyframe.Callback() {@Override public void done() {
								changedButton = false;
								changedButtonIndex = -1;
								displayed_buttons[uiResourcesLoader.OPTIONS_BTN] = buttons_normal[uiResourcesLoader.OPTIONS_BTN];
							}}))
						.play();
				}}.run();
			} else if (y > 314 && y < 343 && x > 393 && x < (393+85)) { // exit game
				System.exit(0);
			} else if (x > 311 && x < (311+20) && y > 160 && y < (160+26)) {
				new Runnable() {@Override public void run() {
					panel.createTransition()
						.push(new SLKeyframe(AboutCfg, 2f)
							.setStartSide(SLSide.BOTTOM, uiAboutPanel)
							.setEndSide(SLSide.TOP, uiGameMenuPanel))
						.play();
				}}.run();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			boolean mustRepaint = false;

			if(y > 314 && y < 343 && x > 163 && x < (163+85)) { // new game button
				mustRepaint = changeButtonToActive(uiResourcesLoader.NEW_GAME_BTN);
			} else if (y > 314 && y < 343 && x > 278 && x < (278+85)) { // options button
				mustRepaint = changeButtonToActive(uiResourcesLoader.OPTIONS_BTN);
			} else if (y > 314 && y < 343 && x > 393 && x < (393+85)) { // exit button
				mustRepaint = changeButtonToActive(uiResourcesLoader.EXIT_BTN);
			} else {
				if(changedButton) {
					displayed_buttons[changedButtonIndex] = buttons_normal[changedButtonIndex];
					changedButton = false;
					pressedButton = false;
					changedButtonIndex = -1;
					mustRepaint = true;
				}
			}
			if(mustRepaint) {
				repaint();
			}
		}
		
		private boolean changeButtonToActive(int buttonIndex) {
			if(changedButtonIndex == buttonIndex) {
				displayed_buttons[buttonIndex] = buttons_active[buttonIndex];
				changedButton = true;
				pressedButton = true;
				changedButtonIndex = buttonIndex;
				return true;
			}
			return false;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			if(pressedButton) {
				displayed_buttons[changedButtonIndex] = buttons_normal[changedButtonIndex];
				changedButton = false;
				pressedButton = false;
				changedButtonIndex = -1;
			}

			if(y > 314 && y < 343 && x > 163 && x < (163+85)) { // new game button
				changeButtonToHover(uiResourcesLoader.NEW_GAME_BTN);
			} else if (y > 314 && y < 343 && x > 278 && x < (278+85)) { // options button
				changeButtonToHover(uiResourcesLoader.OPTIONS_BTN);
			} else if (y > 314 && y < 343 && x > 393 && x < (393+85)) { // exit button
				changeButtonToHover(uiResourcesLoader.EXIT_BTN);
			} 
			repaint();
		}

		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void keyTyped(KeyEvent e) {}
		@Override public void keyPressed(KeyEvent e) {}
		@Override public void keyReleased(KeyEvent e) {}
		@Override public void mouseDragged(MouseEvent e) {}
	}
}
