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

        JPanel grid = new JPanel(new GridLayout(3, 3, 20, 20));
        grid.setOpaque(false);
        
        grid.add(createActionButton("Add User", e -> addUser()));
        grid.add(createActionButton("Create a course", e -> createCourse()));
        grid.add(createActionButton("Edit a course", e -> editCourse()));
        grid.add(createActionButton("Delete a course", e -> deleteCourse()));
        grid.add(createActionButton("Generate report", e -> generateReport()));
        grid.add(createActionButton("View students schedules", e -> viewStudents()));
        grid.add(createActionButton("Student holds", e -> manageHolds()));
        grid.add(createActionButton("Withdraw student", e -> withdrawStudent()));

        wrapper.add(grid, BorderLayout.CENTER);
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

    //Networking Actions:
    private void addUser() {
        String[] types = {"STUDENT", "ADMIN"};
        int type = JOptionPane.showOptionDialog(this, "Select Type", "Add User",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
                
        String userType = (type == 0) ? "STUDENT" : "ADMIN";
        String username = JOptionPane.showInputDialog("Username:");
        String password = JOptionPane.showInputDialog("Password:");
        String first = JOptionPane.showInputDialog("First Name:");
        String last = JOptionPane.showInputDialog("Last Name:");
        String id = JOptionPane.showInputDialog("ID (e.g. S005):");

        if (username != null && id != null) {
            ArrayList<String> args = new ArrayList<>();
            args.add(userType);
            args.add(username);
            args.add(password);
            args.add(first);
            args.add(last);
            args.add(id);
            
            sendRequest(new Message(MessageType.ADD_USER, Status.NULL, "Add User", args));
        }
    }

    private void createCourse() {
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID:");
        if (courseId == null) return;
        
        String courseName = JOptionPane.showInputDialog(this, "Enter Course Name:");
        if (courseName == null) return;
        
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
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to delete:");
        if (courseId == null) return;
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
        
        String courseId = JOptionPane.showInputDialog(this, "Enter ID of Course to Edit: ");
        if (courseId == null || courseId.isBlank()) return;

        String newName = JOptionPane.showInputDialog(this, "Enter New Course Name: ");
        if (newName == null) return;
        
        String newCapacity = JOptionPane.showInputDialog(this, "Enter New Capacity:");
        if (newCapacity == null) return;

        ArrayList<String> args = new ArrayList<>();
        args.add(courseId.trim());
        args.add(newName.trim());
        args.add(newCapacity.trim());
        
        sendRequest(new Message(MessageType.EDIT_COURSE, Status.NULL, "Edit", args));
    }

    private void viewStudents() { 
        sendRequest(new Message(MessageType.VIEW_STUDENTS, Status.NULL, "")); 
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
    
    private void generateReport() { sendRequest(new Message(MessageType.GET_REPORT, Status.NULL, "")); }

    private void sendRequest(Message msg) {
        try {
            Message response = client.send(msg);
            
            if (response.getStatus() == Status.SUCCESS) {
                // Show course ID after creating a course
                if (msg.getType() == MessageType.CREATE_COURSE && 
                    response.getText().startsWith("Created ")) {
                    
                    String courseId = response.getText().substring(8).trim();
                    String html = 
                        "<html><body>" +
                        "<div style='text-align:center; font-family:Arial;'>" +
                            "<h2 style='color:black; margin:0;'>Course Created!</h2>" +
                            "<p style='margin-top:10px; font-size:16px;'>Course ID:</p>" +
                            "<p style='font-size:22px; font-weight:bold;'>" + courseId + "</p>" +
                        "</div>" +
                        "</body></html>";
                    
                    JLabel label = new JLabel(html);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    // Create a panel to ensure everything is centered
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.add(label, BorderLayout.CENTER);
                    panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
                    
                   
                    UIManager.put("OptionPane.buttonOrientation", SwingConstants.CENTER);
                    try {
                        JOptionPane.showMessageDialog(
                            this,
                            panel,
                            "Success",
                            JOptionPane.PLAIN_MESSAGE,
                            null  // no icon
                        );
                    } finally {
                       
                        UIManager.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
                    }
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