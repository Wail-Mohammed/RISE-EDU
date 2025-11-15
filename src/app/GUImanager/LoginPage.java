package app.GUImanager;

import java.awt.GridLayout;
//gridLayout is the layout manager that display two columns and two rows of components for the user to enter username and password


import javax.swing.JLabel;
import javax.swing.JOptionPane;    //this is for the popup window 
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SystemManager.SystemManager;

/*


precondition:
    The user must have an existing account and is active.
    The user credentials are stored in a data file.

Postconditions
    directs to the student page or admin page base on the user role
 */

public class LoginPage {
	
	private final SystemManager systemManager;
	
    //initializing the system manager
	public LoginPage(SystemManager systemManager) {
		this.systemManager = systemManager;
		this.systemManager.intializeSystem();
	}
	
	public void start() {
		boolean continueLogin = true; //this is for the loop to continue until the user logs in
		
        //this is the loop to continue until the user logs in
		while (continueLogin) {
			LoginForm form = createLoginForm();
			int selection = JOptionPane.showConfirmDialog(null, form.panel, "RISE-EDU Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (selection != JOptionPane.OK_OPTION) {
				continueLogin = confirmExit();
				continue;
			}
			
			String username = form.usernameField.getText().trim();
			String password = new String(form.passwordField.getPassword());
			
            //this is the validation to check if the username and password are empty
			if (username.isEmpty() || password.isEmpty()) {
				showValidationError("Please enter both username and password.");
				continue;
			}
			
            //this is the information to show to the user that the login flow will be integrated with SystemManager in a later step
			showInformation("Login flow will be integrated with SystemManager in a later phase.\nCurrent input recorded for demo purposes only.");
			continueLogin = false;
		}
	}
	
    //this is the function to create the login form
	private LoginForm createLoginForm() {
		JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
		JTextField usernameField = new JTextField(15);
		JPasswordField passwordField = new JPasswordField(15);
		
		panel.add(new JLabel("Username:"));
		panel.add(usernameField);
		panel.add(new JLabel("Password:"));
		panel.add(passwordField);
		
		return new LoginForm(panel, usernameField, passwordField);
	}
	//this is the function to confirm if the user wants to cancel the login
	private boolean confirmExit() {
		int result = JOptionPane.showConfirmDialog(null, "Do you want to cancel the login?", "Cancel Login", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.NO_OPTION;
	}
	
	private void showValidationError(String message) {
		JOptionPane.showMessageDialog(null, message, "Login Error", JOptionPane.ERROR_MESSAGE);
	}
	
    
	private void showInformation(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}
	//this is the function to launch the login page
	public static void launch(SystemManager systemManager) {
		LoginPage loginPage = new LoginPage(systemManager);
		loginPage.start();
	}
	
    //this is the record to store the login form
	private record LoginForm(JPanel panel, JTextField usernameField, JPasswordField passwordField) {}
}