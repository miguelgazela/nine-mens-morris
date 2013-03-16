package GameUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UIResourcesLoader {
	private static UIResourcesLoader instanceLoader;
	
	// main background images
	public BufferedImage game_background_1;
	
	private UIResourcesLoader() {
		try {
			game_background_1 = ImageIO.read(new File("images/backgrounds/game_background_test.png"));
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
}
