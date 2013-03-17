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


public class UIGameMenu extends JFrame {
	private UIResourcesLoader uiResourcesLoader;
	private UIGameMenuPanel uiGameMenuPanel;
	
	public UIGameMenu() {
		super("Nine Men's Morris - by Afonso Caldas & Miguel Oliveira");
		uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		uiGameMenuPanel = new UIGameMenuPanel(this);
		uiGameMenuPanel.addMouseMotionListener(uiGameMenuPanel);
		uiGameMenuPanel.addMouseListener(uiGameMenuPanel);
		uiGameMenuPanel.addKeyListener(uiGameMenuPanel);
		getContentPane().add(uiGameMenuPanel);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}

// TODO pass this to a different file??
class UIGameMenuPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	private static final long serialVersionUID = -1237601154927560866L;
	private UIGameMenu uiGameMenu;
	private UIResourcesLoader uiResourcesLoader;
	private Graphics graphics;
	private BufferedImage background;
	
	private Image some;
	private boolean selected = false;
	
	public UIGameMenuPanel(UIGameMenu uiGameMenu) {
		this.uiGameMenu = uiGameMenu;
		uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
		background = uiResourcesLoader.main_menu_background;
		some = uiResourcesLoader.getUnselectedPiece(UIResourcesLoader.BLUE_PIECE);
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
			graphics.drawImage(some, 100,100,this);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getX() > 100 && e.getY() > 100 && e.getX() < 164 && e.getY() < 164) {
			some = uiResourcesLoader.getSelectedPiece(UIResourcesLoader.BLUE_PIECE);
			selected = true;
			repaint();
		} else if (selected) {
			some = uiResourcesLoader.getUnselectedPiece(UIResourcesLoader.BLUE_PIECE);
			selected = false;
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("HERE HERE");
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("HERE");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Pressed key: "+e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
