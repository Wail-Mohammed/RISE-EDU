package app.Client.GUImanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//gridLayout is the layout manager that display two columns and two rows of components for the user to enter username and password
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;    //this is for the popup window 
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import SystemManager.SystemManager;

/*


precondition:
    The user must have an existing account and is active.
    The user credentials are stored in a data file.

Postconditions
    directs to the student page or admin page base on the user role
 */

public class LoginPage extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND = new Color(210, 210, 210);
	private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);
	private final SystemManager systemManager;
	private final JTextField usernameField = new JTextField(20);
	private final JPasswordField passwordField = new JPasswordField(20);
	private final JButton goButton = new JButton("Go");
	
    //initializing the system manager
	public LoginPage(SystemManager systemManager) {
		super("RISE-EDU Login");
		this.systemManager = systemManager;
		this.systemManager.intializeSystem();
		initializeFrame();
		registerListeners();
	}
	
	//this is the frame setup so the user sees a centered window with our form and buttons
	private void initializeFrame() {
		setSize(900, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(BACKGROUND);
		getContentPane().setLayout(new BorderLayout());
		add(buildContent(), BorderLayout.CENTER);
	}
	
	//this is the panel that holds title, form, and button with proper spacing
	private JPanel buildContent() {
		JPanel wrapper = new JPanel();
		wrapper.setOpaque(false);
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
		wrapper.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
		
		JLabel title = new JLabel("Login", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		wrapper.add(Box.createVerticalGlue());
		wrapper.add(title);
		wrapper.add(Box.createRigidArea(new Dimension(0, 40)));
		wrapper.add(createFormPanel());
		wrapper.add(Box.createRigidArea(new Dimension(0, 30)));
		wrapper.add(createButtonPanel());
		wrapper.add(Box.createVerticalGlue());
		return wrapper;
	}
	
	//this is the form with username and password inputs
	private JPanel createFormPanel() {
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(12, 12, 12, 12);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Username:"), gbc);
		
		gbc.gridx = 1;
		formPanel.add(usernameField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(new JLabel("Password:"), gbc);
		
		gbc.gridx = 1;
		formPanel.add(passwordField, gbc);
		
		return formPanel;
	}
	
	//this is the JButtons
	private JPanel createButtonPanel() {
		stylePrimaryButton(goButton);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(goButton);
		return buttonPanel;
	}
	
	//this is a helper to give the pill style button requested
	private void stylePrimaryButton(JButton button) {
		button.setPreferredSize(new Dimension(160, 60));
		button.setBackground(PRIMARY_BUTTON);
		button.setForeground(Color.BLACK);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
		button.setOpaque(true);
	}
	
	//this is the method to connect user actions (button clicks, enter key) to our logic
	private void registerListeners() {
		ActionListener loginAction = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleLogin();
			}
		};
		goButton.addActionListener(loginAction);
		passwordField.addActionListener(loginAction);
	}
	
	//this is the main logic to validate credentials once the user hits Login
	private void handleLogin() {
		String username = usernameField.getText().trim();
		String password = new String(passwordField.getPassword());
		
        //this is the validation to check if the username and password are empty
		if (username.isEmpty() || password.isEmpty()) {
			showValidationError("Please enter both username and password.");
			return;
		}
		
        //this is the information to show to the user that the login flow will be integrated with SystemManager in a later step
		showInformation("Login flow will be integrated with SystemManager in a later phase.\nCurrent input recorded for demo purposes only.");
		
		if (username.equalsIgnoreCase("admin")) {
			openAdminMenu(username);
		} else {
			openStudentMenu(username);
		}
	}
	
	//this is how we demonstrate the student menu window based on role
	private void openStudentMenu(String username) {
		StudentPageGUI studentPage = new StudentPageGUI("Welcome " + username + "!");
		studentPage.setVisible(true);
	}
	
	//this is how we demonstrate the admin menu window based on role
	private void openAdminMenu(String username) {
		AdminPageGUI adminPage = new AdminPageGUI("Welcome " + username + "!");
		adminPage.setVisible(true);
	}
	
	//this is the public entry so other classes (GUI.java) can launch the login window
	public void start() {
		SwingUtilities.invokeLater(() -> setVisible(true));
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
	
}

