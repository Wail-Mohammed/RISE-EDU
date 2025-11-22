package app.Client.GUImanager;

import javax.swing.SwingUtilities;

import app.Client.Client;

/*
 * This is the main class that starts the GUI for our RISE-EDU project. Basically when we run the program, it creates a SystemManager
 * and then opens the LoginPage window.
 *
 * From there the user can log in as a student or admin base on their role in credentials file, and LoginPage will redirect them to the correct GUI page.
 */
public final class GUI {
    private GUI() {}

    public static void main(String[] args) {
    
        Client client = new Client();

        SwingUtilities.invokeLater(() -> {
            LoginPage.launch(client);
        });
    }
}
