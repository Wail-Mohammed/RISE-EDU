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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;    // this is for the popup window
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import SystemManager.SystemManager;

/*
 * LoginPage is the first window the user sees when starting our RISE-EDU program. The main job of this class is very simple: show a login screen where the user
 * can type a username and password, and then decide which page to open next
 * (student page or admin page based on their role in credentials file).
 *
 * In other words, this class is mainly about the GUI layout and basic input
 * validation. The logic will in SystemManager。
 *
 * precondition:
 *   - The user should already have an account stored in the data files
 *     
 *
 * postcondition:
 *   - After entering valid credentials, the program will open
 *     either the AdminPageGUI or the StudentPageGUI, depending on the role in credentials file.
 */
public class LoginPage extends JFrame {

	// This field is something Java asks us to include since JFrame is Serializable. We are not really using it for anything in our project — it just prevents
	// a warning 
    private static final long serialVersionUID = 1L;

    // background color for the whole window
    // We use a light gray so the blue "Go" is more visible.
    private static final Color BACKGROUND = new Color(210, 210, 210);

    // main button
    // This is the color of our "Go" button
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    // We keep a reference to SystemManager so later we can call real login methods.
    private SystemManager systemManager;

    // Simple text fields for username and password input.
    // We use width 20 which is way enough for username/password length.
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);

    // The "Go" button that the user clicks when they are ready to log in.
    private JButton goButton = new JButton("Go");

    /*
     * Constructor for LoginPage.
     * Here we:
     *  - store the SystemManager reference,
     *  - call intializeSystem() so the backend side is ready,
     *  - and then set up the frame and event listeners.
     *
     * We also set the window title to "RISE-EDU Login" so the user knows
     * where they are in the application.
     */
    public LoginPage(SystemManager systemManager) {
        super("RISE-EDU Login");
        this.systemManager = systemManager;

        // In our design, SystemManager is responsible for reading data files
        // We call this here so that when we actually connect login logic,
        // the system is already initialized.
        this.systemManager.intializeSystem();

        initializeFrame();
        registerListeners();
    }

    /*
     * This method sets up the main window that the user will see.
     * We define:
     *  - window size,
     *  - center position on the screen,
     *  - close behavior,
     *  - background color,
     *  - and layout manager.
     *
     * Then we add our main content panel into the center.
     */
    private void initializeFrame() {
        setSize(900, 500);                 // make the window wide enough for standard layout
        setLocationRelativeTo(null);       // center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    /*
     * This method builds the main content panel of the login window.
     * We use a vertical BoxLayout to stack the elements:
     *   - title ("Login")
     *   - form panel (username + password)
     *   - button panel (Go button)
     * We also add some vertical spacing to make it look less squeezed.
     */
    private JPanel buildContent() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        // Add empty spacing from the window edges
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f)); // make the title bigger and bold
        title.setAlignmentX(CENTER_ALIGNMENT);

		// push content into the center and control the spacing between title, form, and button.
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(title);
        wrapper.add(Box.createRigidArea(new Dimension(0, 40)));
        wrapper.add(createFormPanel());
        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));
        wrapper.add(createButtonPanel());
        wrapper.add(Box.createVerticalGlue());
        return wrapper;
    }

    /*
     * This panel is the small form with the username and password labels/fields.
     * We use GridBagLayout so that:
     *   - the labels are on the left,
     *   - the text fields are on the right,
     *   - and everything stays nice 
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);   // spacing around each component
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // username text field.
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // password field.
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        return formPanel;
    }

    /*
     * This panel holds just the "Go" button.
     * We call stylePrimaryButton() first to make it look like a main action.
     */
    private JPanel createButtonPanel() {
        stylePrimaryButton(goButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(goButton);
        return buttonPanel;
    }

    /*
     * Helper method to style the main button.
     * We:
     *   - make it bigger,
     *   - apply our blue background color,
     *   - turn off the default focus border,
     *   - and set some padding so the text is not squeezed.
     *
     * This is mostly for the visual side
     */
    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(160, 60));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setOpaque(true);
    }

    /*
     * In this method we connect user actions to our login logic.
     * Both clicking the "Go" button and pressing Enter in the password field
     * will trigger the same ActionListener, which then calls handleLogin().
     *
     * This way, we don't duplicate code in multiple places, user can submit the form in multiple ways.
     */
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

    /*
     * This is the main logic that runs when the user tries to log in.
     *
     * Steps:
     *  1. Read the text from the username and password fields.
     *  2. Check that both are not empty (basic validation).
     *  3. For now, show an info message explaining that the real login
     *     will be added later.
     *  4. Use a very simple rule to decide which page to open:
     *        - if username is "admin", open admin menu;
     *        - otherwise, open student menu.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Basic validation: we do not allow empty username or password.
        // This avoids the user accidentally clicks "Go" without typing anything.
        if (username.isEmpty() || password.isEmpty()) {
            showValidationError("Please enter both username and password.");
            return;
        }

        // For now we just show a message and use a very simple rule:
        // if username is "admin" -> open admin menu, otherwise student menu.
        showInformation(
            "Login flow will be integrated with SystemManager in a later phase.\n" +
            "Currently this is just a demo using a simple role check."
        );

        if (username.equalsIgnoreCase("admin")) {
            openAdminMenu(username);
        } else {
            openStudentMenu(username);
        }
    }

    /*
     * Open the student menu window.
     */
    private void openStudentMenu(String username) {
        StudentPageGUI studentPage = new StudentPageGUI("Welcome " + username + "!");
        studentPage.setVisible(true);
    }

    /*
     * Open the admin menu window.
     */
    private void openAdminMenu(String username) {
        AdminPageGUI adminPage = new AdminPageGUI("Welcome " + username + "!", systemManager);
        adminPage.setVisible(true);
    }

    /*
     * Public method so other classes (like GUI.java) can start the login page.
     * We use "SwingUtilities.invokeLater because Swing likes all GUI updates
     * to happen on the Event Dispatch Thread(EDT)"". This is the  recommended by the Swing documentation.
     */
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    /*
     * Small helper to show an error message when validation fails.
     * Using "this" as the parent component keeps the dialog centered
     * relative to the login window.
     */
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    /*
     * Helper to show information to the user.
     * We use this for explaining that the login is still a demo version.
     */
    private void showInformation(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * Static helper method to create and show the login window.
     * This is mainly used by GUI.java so it doesn't have to know
     * all the details of how LoginPage is constructed.
     */
    public static void launch(SystemManager systemManager) {
        LoginPage loginPage = new LoginPage(systemManager);
        loginPage.start();
    }
}
