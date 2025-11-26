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

        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setOpaque(false);

        JButton addCourseButton = createActionButton("Add a course", e -> addCourse());
        JButton dropCourseButton = createActionButton("Drop a course", e -> dropCourse());
        JButton viewScheduleButton = createActionButton("View schedule", e -> viewSchedule());
        JButton viewCoursesButton = createActionButton("View courses", e -> viewCourses());

        grid.add(addCourseButton);
        grid.add(dropCourseButton);
        grid.add(viewScheduleButton);
        grid.add(viewCoursesButton);

        wrapper.add(grid, BorderLayout.CENTER);
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

    private void sendRequest(Message msg) {
        try {
            Message response = client.send(msg);
            
            if (response.getStatus() == Status.SUCCESS) {
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