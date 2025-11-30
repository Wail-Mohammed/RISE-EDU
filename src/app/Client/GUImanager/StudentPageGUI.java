package app.Client.GUImanager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

import app.Client.Client;
import app.Shared.Message;
import app.Shared.Status;
import app.Shared.MessageType;

public class StudentPageGUI extends JFrame {

    private static final Color BACKGROUND = new Color(230, 230, 230);
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    private Client client;
    private String welcomeMessage;
    private String username;

    public StudentPageGUI(String welcomeMessage, String username, Client client) {
        super("Student Dashboard");
        this.welcomeMessage = welcomeMessage;
        this.username = username;
        this.client = client;
        initializeFrame();
    }

    private void initializeFrame() {
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 80, 60, 80));

        JLabel title = new JLabel(welcomeMessage, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
        wrapper.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setOpaque(false);

        JButton addCourseButton = createActionButton("Add a course", e -> addCourse());
        JButton dropCourseButton = createActionButton("Drop a course", e -> dropCourse());
        JButton viewScheduleButton = createActionButton("View schedule", e -> viewSchedule());
        JButton viewCoursesButton = createActionButton("View courses", e -> viewCourses());
        JButton viewHoldButton = createActionButton("View hold", e -> viewHold());

        grid.add(addCourseButton);
        grid.add(dropCourseButton);
        grid.add(viewScheduleButton);
        grid.add(viewCoursesButton);
        grid.add(viewHoldButton);

        wrapper.add(grid, BorderLayout.CENTER);
        
        // Add Logout button at the bottom
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JButton logoutButton = createLogoutButton();
        logoutPanel.add(logoutButton);
        wrapper.add(logoutPanel, BorderLayout.SOUTH);
        
        return wrapper;
    }

    private JButton createActionButton(String label, java.awt.event.ActionListener action) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(240, 140));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        button.setOpaque(true);
        button.addActionListener(action);
        return button;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Logout");
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(200, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);
        button.addActionListener(e -> logout());
        return button;
    }


    private void addCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to enroll:", "Add a course", JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.isBlank()) return;

        sendRequest(new Message(MessageType.ENROLL_COURSE, Status.NULL, courseId.trim()));
    }

    private void dropCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to drop:", "Drop a course", JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.isBlank()) return;

        sendRequest(new Message(MessageType.DROP_COURSE, Status.NULL, courseId.trim()));
    }

    private void viewSchedule() {
        sendRequest(new Message(MessageType.VIEW_SCHEDULE, Status.NULL, ""));
    }

    private void viewCourses() {
        sendRequest(new Message(MessageType.LIST_COURSES, Status.NULL, ""));
    }

    private void viewHold() {
        sendRequest(new Message(MessageType.VIEW_HOLD, Status.NULL, ""));
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Send logout message to server
                Message logoutMsg = new Message(MessageType.LOGOUT, Status.NULL, "");
                Message response = client.send(logoutMsg);
                
                // Disconnect from server
                client.disconnect();
                
           
                dispose();
                
                // Return to login page
                Client newClient = new Client();
                LoginPage.launch(newClient);
            } catch (Exception e) {
                
                client.disconnect();
                dispose();
                
                // Still return to login page
                Client newClient = new Client();
                LoginPage.launch(newClient);
            }
        }
    }

    private void sendRequest(Message msg) {
        try {
            Message response = client.send(msg);
            
            if (response.getStatus() == Status.SUCCESS) {
                //  Add course ID 
                if (msg.getType() == MessageType.ENROLL_COURSE) {
                    // Get course ID from the request message 
                    String courseId = msg.getText().trim();
                    // Append course ID to the server response text
                    String displayText = response.getText() + " (" + courseId + ")";
                    JOptionPane.showMessageDialog(this, displayText, "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                
                // Special handling for VIEW_HOLD
                if (msg.getType() == MessageType.VIEW_HOLD) {
                    if (response.getList() != null && !response.getList().isEmpty()) {
                        // Has holds - show list with warning (consistent with AdminPageGUI format)
                        StringBuilder sb = new StringBuilder(response.getText() + "\n\n");
                        for (String item : response.getList()) {
                            sb.append(item).append("\n");
                        }
                        JTextArea textArea = new JTextArea(sb.toString());
                        textArea.setEditable(false);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        scrollPane.setPreferredSize(new Dimension(500, 200));
                        JOptionPane.showMessageDialog(this, scrollPane, "Account Holds", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // No holds - simple message
                        JOptionPane.showMessageDialog(this, response.getText(), "Hold Status", JOptionPane.INFORMATION_MESSAGE);
                    }
                    return;
                }
                
                // If we got a list back (like schedule or courses), we format it nicely
                if (response.getList() != null && !response.getList().isEmpty()) {
                    StringBuilder sb = new StringBuilder(response.getText() + "\n\n");
                    sb.append(String.format("%-8s | %-35.35s | %-12s | %-15s | %s | %s\n", "Course ID", " Course Title", "Class Time", "Instructor", "Credits", "Class Capacity"));
                    sb.append("-".repeat(110) + "\n");
                    for (String item : response.getList()) {
                        sb.append(item).append("\n");
                    }
                    JTextArea textArea = new JTextArea(sb.toString());
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    scrollPane.setPreferredSize(new Dimension(800, 400));
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    JOptionPane.showMessageDialog(this, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, response.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, response.getText(), "Server Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Network Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}