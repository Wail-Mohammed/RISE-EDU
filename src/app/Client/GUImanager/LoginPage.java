package app.Client.GUImanager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import app.Client.Client;
import app.Shared.Message;
import app.Shared.Status;
import app.Shared.MessageType;
import app.Shared.UserType;

public class LoginPage extends JFrame {

	private static final Color BACKGROUND = new Color(210, 210, 210);
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    private Client client;

    //UI Components
    private JTextField ipField = new JTextField("localhost", 20);
    private JTextField uniField = new JTextField("RISE-EDU", 20);
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton goButton = new JButton("Go");
    //Grouping by usertype
    private JRadioButton studentRadio = new JRadioButton("Student");
    private JRadioButton adminRadio = new JRadioButton("Admin");
    private ButtonGroup userTypeGroup = new ButtonGroup();

    public LoginPage(Client client) {
        super("RISE-EDU Login");
        this.client = client;
        initializeFrame();
        registerListeners();
    }

    private void initializeFrame() {
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // separate panel for the radio buttons
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.setOpaque(false);
        
        userTypeGroup.add(studentRadio);
        userTypeGroup.add(adminRadio);
        studentRadio.setSelected(true);
       
        studentRadio.setOpaque(false);
        adminRadio.setOpaque(false);

        radioPanel.add(studentRadio);
        radioPanel.add(adminRadio);
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        wrapper.add(radioPanel);
        wrapper.add(Box.createVerticalStrut(10));

        wrapper.add(Box.createVerticalGlue());
        wrapper.add(title);
        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));
        wrapper.add(createFormPanel());
        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));
        wrapper.add(createButtonPanel());
        wrapper.add(Box.createVerticalGlue());
        return wrapper;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ip address
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Server IP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ipField, gbc);
        
        // University name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("University: "), gbc);
        gbc.gridx = 1;
        formPanel.add(uniField, gbc);

        //username
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        //password
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        stylePrimaryButton(goButton);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        JButton createUniButton = new JButton("New Uni");
        createUniButton.setBackground(new Color(100, 200, 100));
        createUniButton.setPreferredSize(new Dimension(160, 50));
        createUniButton.setFocusPainted(false);

        createUniButton.addActionListener(e -> handleCreateUniversity());
        buttonPanel.add(createUniButton);
        buttonPanel.add(goButton);
        
        return buttonPanel;
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(160, 50));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setOpaque(true);
    }

    private void registerListeners() {
        ActionListener loginAction = e -> handleLogin();
        goButton.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
    }

    private void handleLogin() {
        
        String ip = ipField.getText().trim(); 
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String uniName = uniField.getText().trim();

        if (ip.isEmpty()) ip = "localhost"; // Default

        if (username.isEmpty() || password.isEmpty()) {
            showValidationError("Please enter username and password.");
            return;
        }
        //to connect to the server
        try {
        	if (client == null) client = new Client();
            // We connect on demand when user click Login.
//            client.connect(ip, 9898);
        	client.connect("76.132.181.252", 9999);
	
            Message response = sendLoginRequest(username, password, uniName);

            if (response != null && response.getStatus() == Status.SUCCESS) {
                dispose();
                
                UserType userType = response.getUserType();
                if (userType == UserType.ADMIN) {
                    openAdminMenu(username);
                } else {
                    openStudentMenu(username);
                }
            } else {
                String error = (response != null) ? response.getText() : "Unknown error";
                showValidationError("Login Failed: " + error);
                client.disconnect();
            }

        } catch (IOException | ClassNotFoundException e) {
            showInformation("Unable to connect to server at " + ip + "\nError: " + e.getMessage());
            client.disconnect();
        }
    }
    
    private void handleCreateUniversity() {
        String newUniversityName = JOptionPane.showInputDialog(this, "Enter name for a new University:");
        if (newUniversityName == null || newUniversityName.trim().isEmpty()) return;
        
        String ip = ipField.getText().trim();
        if (ip.isEmpty()) ip = "localhost";

        try {
            if (client == null) client = new Client();
            client.connect(ip, 9898);
            
            Message req = new Message(MessageType.ADD_UNIVERSITY, Status.NULL, newUniversityName.trim());
            Message res = client.send(req);
            
            JOptionPane.showMessageDialog(this, res.getText());
            
            if (res.getStatus() == Status.SUCCESS) {
                uniField.setText(newUniversityName.trim());
            }
            
            client.disconnect();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void openStudentMenu(String username) {
        new StudentPageGUI("Welcome " + username + "!", username, client).setVisible(true);
    }

    private void openAdminMenu(String username) {
        new AdminPageGUI("Welcome " + username + "!", client).setVisible(true);
    }

    private Message sendLoginRequest(String username, String password, String uniName) throws IOException, ClassNotFoundException {
        ArrayList<String> payload = new ArrayList<>();
        payload.add(username);
        payload.add(password);
        payload.add(studentRadio.isSelected() ? "STUDENT" : "ADMIN");
        payload.add(uniName);
        Message request = new Message(MessageType.LOGIN, Status.NULL, "", payload);
        return client.send(request);
    }

    public static void launch(Client client) {
        new LoginPage(client).setVisible(true);
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInformation(String message) {
        JOptionPane.showMessageDialog(this, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
    }
}