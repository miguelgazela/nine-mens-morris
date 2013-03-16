package GameUI;

public class UIResourcesLoader {
	private static UIResourcesLoader instanceLoader;
	
	private UIResourcesLoader() {
		
	}
	
	public static UIResourcesLoader getInstanceLoader() {
		if(instanceLoader == null) {
			instanceLoader = new UIResourcesLoader();
		}
		return instanceLoader;
	}
}
