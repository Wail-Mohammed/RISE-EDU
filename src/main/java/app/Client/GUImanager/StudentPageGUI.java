package app.Client.GUImanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

    /*
     * Constructor: this should be called after the student logs in.
     * The welcomeMessage parameter used to display the student's name,
     * but for now we just show a title.
     */
    public StudentPageGUI(String welcomeMessage) {
        super("Welcome student!");
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
        JLabel title = new JLabel("Welcome student!", SwingConstants.CENTER);
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
     * Placeholder for the "add course" feature.
     * Right now, we show an input dialog asking the student for a Course ID.
     * Later, this method should call something like:
     *   systemManager.enrollStudent(studentId, courseId)
     * and then update the schedule.
     */
    private void addCourse() {
        String courseId = JOptionPane.showInputDialog(
                this,
                "Enter Course ID to enroll:",
                "Add a course",
                JOptionPane.QUESTION_MESSAGE
        );

        // If the user pressed Cancel or left it blank, we simply do nothing.
        if (courseId == null || courseId.isBlank()) {
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Enroll flow will use SystemManager once integrated.\nInput recorded for: " + courseId.trim(),
                "Enroll Course",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * Placeholder for the "drop course" feature.
     * Similar to addCourse(), but used for dropping an existing course.
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

        JOptionPane.showMessageDialog(
                this,
                "Drop flow will use SystemManager once integrated.\nInput recorded for: " + courseId.trim(),
                "Drop Course",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * Placeholder for viewing the student's schedule.
     * Right now we just tell the user that this will be connected later.
     * After we implement viewSchedule in SystemManager, this method should
     * ask SystemManager for the list of enrolled courses and show them.
     */
    private void viewSchedule() {
        JOptionPane.showMessageDialog(
                this,
                "Schedule view will be connected to SystemManager in a later phase.",
                "Your Schedule",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * Placeholder for viewing the course catalog.
     * Later, this will probably show all courses from the Course.csv file.
     */
    private void viewCourses() {
        JOptionPane.showMessageDialog(
                this,
                "Course catalog will be connected to SystemManager in a later phase.",
                "Available Courses",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
