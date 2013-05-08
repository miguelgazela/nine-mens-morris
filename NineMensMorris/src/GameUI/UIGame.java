package GameUI;

import javax.swing.JFrame;
import GameLogic.Game;

public class UIGame extends JFrame {
	private Game ninemensmorris;
	private UIResourcesLoader uiResourcesLoader;
	private UIGamePanel uiGamePanel;
	
	public UIGame() {
		super("Nine Men's Morris - by Afonso Caldas & Miguel Oliveira");
		uiResourcesLoader = UIResourcesLoader.getInstanceLoader();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		uiGamePanel = new UIGamePanel(this);
		uiGamePanel.addMouseMotionListener(uiGamePanel);
		uiGamePanel.addMouseListener(uiGamePanel);
		getContentPane().add(uiGamePanel);
		pack();
		setResizable(false);
	}
}
