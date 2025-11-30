package app.Client.GUImanager;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import app.Client.Client;
import app.Shared.Message;
import app.Shared.Status;
import app.Shared.MessageType;

public class AdminPageGUI extends JFrame {

    private static final Color BACKGROUND = new Color(230, 230, 230);
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    private String welcomeMessage;
    private Client client;

    public AdminPageGUI(String welcomeMessage, Client client) {
        super("Administrator Dashboard");
        this.welcomeMessage = welcomeMessage;
        this.client = client;
        initializeFrame();
    }

    private void initializeFrame() {
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));

        JLabel title = new JLabel(welcomeMessage, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        wrapper.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 4, 20, 20));
        grid.setOpaque(false);
        
        grid.add(createActionButton("Add User", e -> addUser()));
        grid.add(createActionButton("Create a course", e -> createCourse()));
        grid.add(createActionButton("Edit a course", e -> editCourse()));
        grid.add(createActionButton("Delete a course", e -> deleteCourse()));
        grid.add(createActionButton("Generate report", e -> generateReport()));
        grid.add(createActionButton("View students schedules", e -> viewStudents()));
        grid.add(createActionButton("Student holds", e -> manageHolds()));
        grid.add(createActionButton("Withdraw student", e -> withdrawStudent()));
        grid.add(createActionButton("View Enrollment List", e -> viewEnrollmentList()));
        grid.add(createActionButton("View Universties", e -> viewUniversties()));
        grid.add(createActionButton("View Admins", e -> viewAdmins()));
        

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
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

    //Networking Actions:
    private void addUser() {
        String[] types = {"STUDENT", "ADMIN"};
        int type = JOptionPane.showOptionDialog(this, "Select Type", "Add User",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        
        // If user cancelled type selection, return
        if (type == JOptionPane.CLOSED_OPTION) return;
                
        String userType = (type == 0) ? "STUDENT" : "ADMIN";
        
        // Collect user information, each step can be cancelled
        String username = JOptionPane.showInputDialog(this, "Username:", "Add User", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.isBlank()) return;
        
        String password = JOptionPane.showInputDialog(this, "Password:", "Add User", JOptionPane.QUESTION_MESSAGE);
        if (password == null || password.isBlank()) return;
        
        String first = JOptionPane.showInputDialog(this, "First Name:", "Add User", JOptionPane.QUESTION_MESSAGE);
        if (first == null || first.isBlank()) return;
        
        String last = JOptionPane.showInputDialog(this, "Last Name:", "Add User", JOptionPane.QUESTION_MESSAGE);
        if (last == null || last.isBlank()) return;
        
        // Display different ID example based on user type
        String idPrompt;
        if (userType.equals("ADMIN")) {
            idPrompt = "ID (e.g. A0001):";
        } else {
            idPrompt = "ID (e.g. S005):";
        }
        String id = JOptionPane.showInputDialog(this, idPrompt, "Add User", JOptionPane.QUESTION_MESSAGE);
        if (id == null || id.isBlank()) return;

        // Send request
        ArrayList<String> args = new ArrayList<>();
        args.add(userType);
        args.add(username.trim());
        args.add(password.trim());
        args.add(first.trim());
        args.add(last.trim());
        args.add(id.trim());
        
        sendRequest(new Message(MessageType.ADD_USER, Status.NULL, "Add User", args));
    }

    private void createCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID (e.g. CS401):", "Create Course", JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.isBlank()) return;
        
        String courseName = JOptionPane.showInputDialog(this, "Enter Course Name:", "Create Course", JOptionPane.QUESTION_MESSAGE);
        if (courseName == null || courseName.isBlank()) return;
        
        ArrayList<String> args = new ArrayList<>();
        args.add(courseId.trim());
        args.add(courseName.trim());
        args.add("TBA"); // Time
        args.add("TBA"); // Location
        args.add("3");   // Credits
        args.add("Smith"); // Instructor
        args.add("35");  // Capacity

        // We send the data to the Server. The Server's SystemManager does the work.
        sendRequest(new Message(MessageType.CREATE_COURSE, Status.NULL, "Create", args));
    }

    private void deleteCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to delete (e.g. CS401):", "Delete Course", JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.isBlank()) return;
        sendRequest(new Message(MessageType.REMOVE_COURSE, Status.NULL, courseId.trim()));
    }

    private void manageHolds() {
        String[] options = {"Add Hold", "Remove Hold"};
        int choice = JOptionPane.showOptionDialog(this, "Select Action", "Manage Holds",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            String studentId = JOptionPane.showInputDialog("Enter Student ID:");
            String reason = JOptionPane.showInputDialog("Enter Reason:");
            if (studentId != null && reason != null) {
                ArrayList<String> args = new ArrayList<>();
                args.add(studentId);
                args.add(reason);
                sendRequest(new Message(MessageType.ADD_HOLD, Status.NULL, "", args));
            }
        } else if (choice == 1) {
            String studentId = JOptionPane.showInputDialog("Enter Student ID:");
            String reason = JOptionPane.showInputDialog("Enter the Exact Hold Reason to Remove: ");
            if (studentId != null) {
                ArrayList<String> args = new ArrayList<>();
                args.add(studentId);
                args.add(reason);
                sendRequest(new Message(MessageType.REMOVE_HOLD, Status.NULL, "", args));
            }
        }
    }
    
    private void editCourse() { 
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to Edit (e.g. CS401):", "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.isBlank()) return;

        String newName = JOptionPane.showInputDialog(this, "Enter New Course Name:", "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (newName == null || newName.isBlank()) return;
        
        String newCapacity = JOptionPane.showInputDialog(this, "Enter New Capacity:", "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (newCapacity == null || newCapacity.isBlank()) return;

        ArrayList<String> args = new ArrayList<>();
        args.add(courseId.trim());
        args.add(newName.trim());
        args.add(newCapacity.trim());
        
        sendRequest(new Message(MessageType.EDIT_COURSE, Status.NULL, "Edit", args));
    }

    private void viewStudents() { 
        // Ask admin to input student ID
        String studentId = JOptionPane.showInputDialog(this, 
            "Enter Student ID (e.g. S0011):", 
            "View Student Schedule", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (studentId == null || studentId.isBlank()) return;
        
        // Send request to view student schedule
        sendRequest(new Message(MessageType.VIEW_STUDENT_SCHEDULE, Status.NULL, studentId.trim()));
    }
    
    private void viewAdmins() { 
        sendRequest(new Message(MessageType.VIEW_ADMINS, Status.NULL, "")); 
    }
    
    private void withdrawStudent() {
        // Step 1: Get course ID
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID:", "Withdraw Student", JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.isBlank()) return;
        
        // Step 2: Get enrollment list to show admin which students are enrolled
        try {
            Message enrollmentMsg = new Message(MessageType.LIST_ENROLLMENT, Status.NULL, courseId.trim());
            Message enrollmentResponse = client.send(enrollmentMsg);
            
            if (enrollmentResponse.getStatus() == Status.SUCCESS) {
                if (enrollmentResponse.getList() != null && !enrollmentResponse.getList().isEmpty()) {
                    // Show enrollment list
                    StringBuilder sb = new StringBuilder("Enrolled Students in " + courseId + ":\n\n");
                    for (String item : enrollmentResponse.getList()) {
                        sb.append(item).append("\n");
                    }
                    JTextArea textArea = new JTextArea(sb.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    scrollPane.setPreferredSize(new Dimension(500, 300));
                    JOptionPane.showMessageDialog(this, scrollPane, "Enrollment List", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // No students enrolled
                    JOptionPane.showMessageDialog(this, 
                        enrollmentResponse.getText(), 
                        "Enrollment List", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return; // Exit if no students enrolled
                }
            } else {
                // Course not found or error
                JOptionPane.showMessageDialog(this, 
                    enrollmentResponse.getText(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return; // Exit on error
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error fetching enrollment list: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return; // Exit on exception
        }
        
        // Step 3: Get student username to withdraw
        String studentUsername = JOptionPane.showInputDialog(this, "Enter Student Username to withdraw:", "Withdraw Student", JOptionPane.QUESTION_MESSAGE);
        if (studentUsername == null || studentUsername.isBlank()) return;
        
        // Step 4: Confirm action
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to withdraw student '" + studentUsername + "' from course '" + courseId + "'?",
            "Confirm Withdrawal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            ArrayList<String> args = new ArrayList<>();
            args.add(studentUsername.trim());
            args.add(courseId.trim());
            sendRequest(new Message(MessageType.WITHDRAW_STUDENT, Status.NULL, "", args));
        }
    }
    
    private void viewUniversties() { 
        sendRequest(new Message(MessageType.VIEW_UNIVERSITIES, Status.NULL, "")); 
    }
    private void viewEnrollmentList() {
        // Ask admin which course to check
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to view enrollment list:");
        if (courseId == null || courseId.isBlank()) return;

        // Send request to server
        sendRequest(new Message(MessageType.LIST_ENROLLMENT, Status.NULL, courseId.trim()));
    }

    private void generateReport() { sendRequest(new Message(MessageType.GET_REPORT, Status.NULL, "")); }

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
                
                // Close the admin dashboard
                dispose();
                
                // Return to login page
                Client newClient = new Client();
                LoginPage.launch(newClient);
            } catch (Exception e) {
                // Even if there's an error, close the window and disconnect
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
                // Show course ID after creating a course
                if (msg.getType() == MessageType.CREATE_COURSE && 
                    response.getText().startsWith("Created ")) {
                    
                    String courseId = response.getText().substring(8).trim();
                    
                    // Create panel with Swing components (no HTML)
                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
                    
                    // Title label
                    JLabel titleLabel = new JLabel("Course Created!");
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
                    titleLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    
                    // Course ID label
                    JLabel idLabel = new JLabel("Course ID:");
                    idLabel.setFont(idLabel.getFont().deriveFont(14f));
                    idLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    
                    // Course ID value label
                    JLabel courseIdLabel = new JLabel(courseId);
                    courseIdLabel.setFont(courseIdLabel.getFont().deriveFont(Font.BOLD, 20f));
                    courseIdLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    
                    panel.add(titleLabel);
                    panel.add(Box.createVerticalStrut(15));
                    panel.add(idLabel);
                    panel.add(Box.createVerticalStrut(5));
                    panel.add(courseIdLabel);
                    
                    JOptionPane.showMessageDialog(
                        this,
                        panel,
                        "Success",
                        JOptionPane.PLAIN_MESSAGE,
                        null  // no icon
                    );
                    return;
                }
                
             
                if (msg.getType() == MessageType.REMOVE_COURSE && 
                    response.getStatus() == Status.SUCCESS &&
                    response.getText().contains("deleted successfully")) {
                    
                  
                    String messageText = response.getText();
                    String courseId = messageText.replace("Course ", "").replace(" deleted successfully", "").trim();
                    
                   
                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
           
                    JLabel titleLabel = new JLabel("Course Deleted!");
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
                    titleLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    
                  
                    JLabel idLabel = new JLabel("Course ID:");
                    idLabel.setFont(idLabel.getFont().deriveFont(14f));
                    idLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    
                   
                    JLabel courseIdLabel = new JLabel(courseId);
                    courseIdLabel.setFont(courseIdLabel.getFont().deriveFont(Font.BOLD, 20f));
                    courseIdLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    
                    panel.add(titleLabel);
                    panel.add(Box.createVerticalStrut(15));
                    panel.add(idLabel);
                    panel.add(Box.createVerticalStrut(5));
                    panel.add(courseIdLabel);
                    
                    JOptionPane.showMessageDialog(
                        this,
                        panel,
                        "Success",
                        JOptionPane.PLAIN_MESSAGE,
                        null  
                    );
                    return;
                }
                
                
                if (msg.getType() == MessageType.VIEW_STUDENT_SCHEDULE && 
                    response.getList() != null && !response.getList().isEmpty()) {
                    
                    
                    String[] columnNames = {"Course ID", "Course Title", "Class Time", "Instructor", "Credits", "Enrollment/Capacity"};
                    ArrayList<String[]> tableData = new ArrayList<>();
                    
                    for (String item : response.getList()) {
                        String[] parts = item.split("\\|");
                        if (parts.length >= 6) {

                            tableData.add(new String[]{
                                parts[0],  // Course ID
                                parts[1],  // Course Title
                                parts[2],  // Time
                                parts[3],  // Instructor
                                parts[4] + " Credits",  // Credits
                                "[" + parts[5] + "]"  // Enrollment/Capacity (format: "current/max")
                            });
                        }
                    }
                    
                    String[][] data = tableData.toArray(new String[tableData.size()][]);
                    JTable table = new JTable(data, columnNames);
                    table.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    table.setRowHeight(20);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table.getColumnModel().getColumn(0).setPreferredWidth(80);   // Course ID
                    table.getColumnModel().getColumn(1).setPreferredWidth(250);  // Course Title
                    table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Class Time
                    table.getColumnModel().getColumn(3).setPreferredWidth(150);  // Instructor
                    table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Credits
                    table.getColumnModel().getColumn(5).setPreferredWidth(150);  // Enrollment/Capacity
                    table.setEnabled(false); // Make table read-only
                    
                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(850, 300));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, response.getText(), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // for view list of students or reports
                if (response.getList() != null && !response.getList().isEmpty()) {
                    
                    StringBuilder sb = new StringBuilder(response.getText() + "\n\n");
                    for (String item : response.getList()) {
                        sb.append(item).append("\n");
                    }
                    // for long lists
                    JTextArea textArea = new JTextArea(sb.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, response.getText());
                }
            } else {
                JOptionPane.showMessageDialog(this, response.getText(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Network Error: " + e.getMessage());
        }
    }
}