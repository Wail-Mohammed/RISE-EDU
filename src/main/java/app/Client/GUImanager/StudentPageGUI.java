package app.Client.GUImanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.Client.Client;
import app.Shared.Message;
import app.Shared.Status;
import app.Shared.Type;

/*
 * StudentPageGUI is the page for a student after they log in.
 * The main goal of this window is to give the student four big options:
 *  - Add a course
 *  - Drop a course
 *  - View schedule
 *  - View courses
 *
 * Right now, each button only shows a dialog
 *
 */
public class StudentPageGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    // gray background so the buttons are more visible.
    private static final Color BACKGROUND = new Color(230, 230, 230);

    // Blue color for the main action buttons.
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    // We create one button for each action
    private JButton addCourseButton;
    private JButton dropCourseButton;
    private JButton viewScheduleButton;
    private JButton viewCoursesButton;
    private Client client;
    private final String welcomeMessage;
    private final String studentId;

    private static final String DEFAULT_WELCOME = "Welcome student!";
    private static final String UNKNOWN_STUDENT = "unknown";

    /*
     * Constructor: this should be called after the student logs in.
     * The welcomeMessage parameter used to display the student's name,
     * but for now we just show a title.
     */
    public StudentPageGUI(String welcomeMessage, String studentId, Client client) {
        super("Student Dashboard");
        this.welcomeMessage = (welcomeMessage == null || welcomeMessage.isBlank())
                ? DEFAULT_WELCOME
                : welcomeMessage;
        this.studentId = (studentId == null || studentId.isBlank()) ? UNKNOWN_STUDENT : studentId;
        this.client = client;
        initializeFrame();
    }

    /*
     * This method sets up the main window for the student.
     * It is similar to the LoginPage setup:
     *   - we choose a size,
     *   - center the window,
     *   - set the background color,
     *   - pick BorderLayout and put our content panel in the center.
     */
    private void initializeFrame() {
        setSize(1100, 500);
        setLocationRelativeTo(null); // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    /*
     * This method builds the content of the student page.
     *
     * Layout idea:
     * top    -> "Welcome student!" label
     * center -> 2 x 2 grid with four large buttons
     *
     */
    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 80, 60, 80));

        // Big title label at the top of the window.
        JLabel title = new JLabel(welcomeMessage, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
        wrapper.add(title, BorderLayout.NORTH);

        /*
         * Create the 2x2 grid panel. GridLayout automatically makes the
         * four buttons the same size and places them evenly.
         * The (30, 30) gap makes some space between each button.
         */
        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setOpaque(false);

        addCourseButton = new JButton("Add a course");
        dropCourseButton = new JButton("Drop a course");
        viewScheduleButton = new JButton("View schedule");
        viewCoursesButton = new JButton("View courses");

        // Apply the same style to all four buttons.
        styleMainButton(addCourseButton);
        styleMainButton(dropCourseButton);
        styleMainButton(viewScheduleButton);
        styleMainButton(viewCoursesButton);

        // Connect each button to its own method using listeners.
        addCourseButton.addActionListener(e -> addCourse());
        dropCourseButton.addActionListener(e -> dropCourse());
        viewScheduleButton.addActionListener(e -> viewSchedule());
        viewCoursesButton.addActionListener(e -> viewCourses());

        // Add the buttons to the grid in the order we want them to appear.
        grid.add(addCourseButton);
        grid.add(dropCourseButton);
        grid.add(viewScheduleButton);
        grid.add(viewCoursesButton);

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    /*
     * This helper method sets the visual style for a main action button.
     * We call this for each of the four buttons so they look same.
     *
     * The goal is to make the buttons big, easy to click, and clearly been seen.
     */
    private void styleMainButton(JButton button) {
        button.setPreferredSize(new Dimension(240, 140));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        button.setOpaque(true);
    }

    /*
     * Ask the student for a Course ID, send an ENROLL_COURSE request
     * through the client, and show the server response.
     */
    private void addCourse() {
        String courseId = JOptionPane.showInputDialog(
                this,
                "Enter Course ID to enroll:",
                "Add a course",
                JOptionPane.QUESTION_MESSAGE
        );

        if (courseId == null || courseId.isBlank()) {
            return;
        }

        String trimmed = courseId.trim();
        Message response = sendRequest(Type.ENROLL_COURSE, trimmed);
        if (response == null) {
            return;
        }

        if (response.getStatus() == Status.SUCCESS) {
            String message = response.getText();
            if (message == null || message.isBlank()) {
                message = "Successfully enrolled in " + trimmed;
            }
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Enroll Course",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            showServerError(response.getText());
        }
    }

    /*
     * Ask for a Course ID and send DROP_COURSE request to the server.
     */
    private void dropCourse() {
        String courseId = JOptionPane.showInputDialog(
                this,
                "Enter Course ID to drop:",
                "Drop a course",
                JOptionPane.QUESTION_MESSAGE
        );

        if (courseId == null || courseId.isBlank()) {
            return;
        }

        String trimmed = courseId.trim();
        Message response = sendRequest(Type.DROP_COURSE, trimmed);
        if (response == null) {
            return;
        }

        if (response.getStatus() == Status.SUCCESS) {
            String message = response.getText();
            if (message == null || message.isBlank()) {
                message = "Successfully dropped " + trimmed;
            }
            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Drop Course",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            showServerError(response.getText());
        }
    }

    /*
     * Send VIEW_SCHEDULE request and display whatever the server returns.
     */
    private void viewSchedule() {
        Message response = sendRequest(Type.VIEW_SCHEDULE, "");
        if (response == null) {
            return;
        }

        if (response.getStatus() == Status.SUCCESS) {
            String text = response.getText();
            if (text == null || text.isBlank()) {
                text = "Schedule not available yet.";
            }
            JOptionPane.showMessageDialog(
                    this,
                    text,
                    "Your Schedule",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            showServerError(response.getText());
        }
    }

    /*
     * Send LIST_COURSES request and display the catalog text returned by server.
     */
    private void viewCourses() {
        Message response = sendRequest(Type.LIST_COURSES, "");
        if (response == null) {
            return;
        }

        if (response.getStatus() == Status.SUCCESS) {
            String text = response.getText();
            if (text == null || text.isBlank()) {
                text = "Course catalog will be available soon.";
            }
            JOptionPane.showMessageDialog(
                    this,
                    text,
                    "Available Courses",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            showServerError(response.getText());
        }
    }

    private Message sendRequest(Type type, String text) {
        if (client == null) {
            showServerError("Client is not initialized; unable to reach the server.");
            return null;
        }

        try {
            ArrayList<String> payload = new ArrayList<>();
            payload.add(studentId);
            payload.add(text == null ? "" : text);
            Message request = new Message(type, Status.NULL, "", payload);
            return client.send(request);
        } catch (IOException | ClassNotFoundException e) {
            showServerError("Unable to reach server: " + e.getMessage());
            return null;
        }
    }

    private void showServerError(String message) {
        String display = (message == null || message.isBlank())
                ? "Server returned an unknown error."
                : message;
        JOptionPane.showMessageDialog(
                this,
                display,
                "Server Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
