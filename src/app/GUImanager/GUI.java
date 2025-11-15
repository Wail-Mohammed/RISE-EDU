package app.GUImanager;

import SystemManager.SystemManager;

public final class GUI {

	private GUI() {
	}
	
	public static void main(String[] args) {
		SystemManager systemManager = new SystemManager();
		LoginPage.launch(systemManager);
	}
}