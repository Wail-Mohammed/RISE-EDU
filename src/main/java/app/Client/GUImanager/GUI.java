package app.Client.GUImanager;

import SystemManager.SystemManager;

/*
 * This is the main class that starts the GUI for our RISE-EDU project.
 * Basically when we run the program, it creates a SystemManager
 * (which handles the actual logic on the server side),
 * and then opens the LoginPage window.
 *
 * From there the user can log in as a student or admin,
 * and LoginPage will redirect them to the correct GUI page.
 */
public final class GUI {

    // We keep the constructor private so nobody tries to create
    // an object of this class by accident. We only need the main method.
    private GUI() {}

    public static void main(String[] args) {
        // Create the SystemManager instance that will be passed to the GUI pages
        SystemManager systemManager = new SystemManager();

        // Start the login window. The login window decides where to go next.
        LoginPage.launch(systemManager);
    }
}
