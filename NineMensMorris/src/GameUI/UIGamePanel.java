package GameUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class UIGamePanel extends JPanel implements MouseMotionListener, MouseListener {
	
	private static final long serialVersionUID = 1123154693397356654L;
	private UIGame uiGame;
	private UIResourcesLoader uiResourcesLoader;
	private Graphics graphics;
	
	public UIGamePanel(UIGame uiGame) {
		this.uiGame = uiGame;
		uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
	}
	
	@Override
	public Dimension getPreferredSize() {
		BufferedImage bg = uiResourcesLoader.game_background_1;
		if (bg != null) {
			int width = bg.getWidth();
			int height = bg.getHeight();
			return new Dimension(width , height );
		}
		return super.getPreferredSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // clear off-screen bitmap
		graphics = g;
		BufferedImage bg = uiResourcesLoader.game_background_1;

		// draws the game elements
		if (bg != null) {
			graphics.drawImage(bg, 0, 0, this);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("X: "+e.getX()+" Y: "+e.getY());
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
