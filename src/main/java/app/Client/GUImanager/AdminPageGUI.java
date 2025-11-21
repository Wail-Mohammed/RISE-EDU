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

import SystemManager.SystemManager;
import app.models.Course;

/*
 * AdminPageGUI is the page for an administrator after they log in.
 * The idea is very similar to StudentPageGUI, but here we show six buttons
 * for the common admin tasks:
 *   - Create a course
 *   - Edit a course
 *   - Delete a course
 *   - Generate report
 *   - View students schedules
 *   - Student holds
 *
 * Right now, each button just shows a simple "coming soon" dialog.
 * Later, these buttons will be connected to real methods in SystemManager
 * so that an admin can actually manage courses and student holds.
 */
public class AdminPageGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    // gray background for the whole window, same style as the student page.
    private static final Color BACKGROUND = new Color(230, 230, 230);

    // Blue color for the main admin buttons so they match the overall theme.
    private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

    // We keep the welcome message so we can show something like "Welcome Admin"
    // at the top of the window. This makes the page super friendly.
    private String welcomeMessage;

    // SystemManager reference for business logic
    private SystemManager systemManager;

    /*
     * Constructor: this should be called right after the admin logs in.
     * The welcomeMessage can be used to show a personalized header, e.g.
     * "Welcome Alice (Admin)".
     */
    public AdminPageGUI(String welcomeMessage, SystemManager systemManager) {
        super("Administrator Dashboard");
        this.welcomeMessage = welcomeMessage;
        this.systemManager = systemManager;
        initializeFrame();
    }

    /*
     * This method sets up the main admin window.
     * It is also very similar to the login and student pages:
     *   - set a fixed size,
     *   - center the window on the screen,
     *   - set a background color,
     *   - use BorderLayout so we can put the title at the top and buttons in the center.
     *
     * We use DISPOSE_ON_CLOSE so closing the admin window does not kill the entire program.
     */
    private void initializeFrame() {
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND);
        getContentPane().setLayout(new BorderLayout());
        add(buildContent(), BorderLayout.CENTER);
    }

    /*
     * This method builds the content of the admin page.
     *
     * Layout idea:
     *  top-> a welcome label using the welcomeMessage string
     *  (center) -> a 2 x 3 grid of large buttons for each admin function
     */
    private JPanel buildContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));

        JLabel title = new JLabel(welcomeMessage, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        wrapper.add(title, BorderLayout.NORTH);

        /*
         * Create the 2 x 3 grid for the six admin buttons.
         * GridLayout is useful here because it makes all six buttons the same size
         * and puts them in two rows with three columns.
         */
        JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
        grid.setOpaque(false);

        // Each button is created with the same helper method.

        // Create a course button needs special handling
        JButton createCourseButton = new JButton("Create a course");
        createCourseButton.setPreferredSize(new Dimension(200, 120));
        createCourseButton.setBackground(PRIMARY_BUTTON);
        createCourseButton.setForeground(Color.BLACK);
        createCourseButton.setFont(createCourseButton.getFont().deriveFont(Font.BOLD, 16f));
        createCourseButton.setFocusPainted(false);
        createCourseButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        createCourseButton.setOpaque(true);
        createCourseButton.addActionListener(e -> createCourse());
        grid.add(createCourseButton);
        
        // Edit a course button needs special handling
        JButton editCourseButton = new JButton("Edit a course");
        editCourseButton.setPreferredSize(new Dimension(200, 120));
        editCourseButton.setBackground(PRIMARY_BUTTON);
        editCourseButton.setForeground(Color.BLACK);
        editCourseButton.setFont(editCourseButton.getFont().deriveFont(Font.BOLD, 16f));
        editCourseButton.setFocusPainted(false);
        editCourseButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        editCourseButton.setOpaque(true);
        editCourseButton.addActionListener(e -> editCourse());
        grid.add(editCourseButton);
        
        grid.add(createActionButton("Delete a course"));
        grid.add(createActionButton("Generate report"));
        grid.add(createActionButton("View students schedules"));
        
        // Student holds button needs special handling
        JButton holdsButton = new JButton("Student holds");
        holdsButton.setPreferredSize(new Dimension(200, 120));
        holdsButton.setBackground(PRIMARY_BUTTON);
        holdsButton.setForeground(Color.BLACK);
        holdsButton.setFont(holdsButton.getFont().deriveFont(Font.BOLD, 16f));
        holdsButton.setFocusPainted(false);
        holdsButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        holdsButton.setOpaque(true);
        holdsButton.addActionListener(e -> manageHolds());
        grid.add(holdsButton);

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    /*
     * Helper method to create one of the admin buttons.
     * We using the same size, colors, and font
     * for all six buttons.
     *
     * The ActionListener simply calls showComingSoon(label), which shows a
     * message dialog saying that this feature will be connected later.
     */
    private JButton createActionButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(200, 120));
        button.setBackground(PRIMARY_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setOpaque(true);

        // For now, all admin actions are still TODO, so we just show a placeholder.
        button.addActionListener(e -> showComingSoon(label));
        return button;
    }

    /*
     * This method is used for all six buttons right now.
     * Instead of leaving the button empty, we show a message explaining
     * that the real logic will be wired to SystemManager later.

     */
    private void showComingSoon(String action) {
        JOptionPane.showMessageDialog(
                this,
                action + " will be connected to the SystemManager in a later phase.",
                "Feature placeholder",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * Create a new course
     * Collects course information from admin and creates the course
     */
    private void createCourse() {
        // 1. 输入课程ID
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID:", 
                                                       "Create Course", 
                                                       JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.trim().isEmpty()) {
            return;
        }

        // 检查是否已存在
        if (systemManager.findCourse(courseId.trim()) != null) {
            JOptionPane.showMessageDialog(this, 
                "Course ID already exists: " + courseId, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. 输入课程名称
        String courseName = JOptionPane.showInputDialog(this, "Enter Course Name:", 
                                                         "Create Course", 
                                                         JOptionPane.QUESTION_MESSAGE);
        if (courseName == null || courseName.trim().isEmpty()) {
            return;
        }

        // 3. 输入时间
        String time = JOptionPane.showInputDialog(this, 
            "Enter Course Time (e.g., Mon/Wed 6:30-7:45):", 
            "Create Course", JOptionPane.QUESTION_MESSAGE);
        if (time == null || time.trim().isEmpty()) {
            return;
        }

        // 4. 输入地点
        String location = JOptionPane.showInputDialog(this, "Enter Location:", 
                                                       "Create Course", 
                                                       JOptionPane.QUESTION_MESSAGE);
        if (location == null || location.trim().isEmpty()) {
            return;
        }

        // 5. 输入学分
        String creditsStr = JOptionPane.showInputDialog(this, "Enter Credits:", 
                                                         "Create Course", 
                                                         JOptionPane.QUESTION_MESSAGE);
        if (creditsStr == null || creditsStr.trim().isEmpty()) {
            return;
        }
        
        int credits;
        try {
            credits = Integer.parseInt(creditsStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid credits. Please enter a number.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 6. 输入教师
        String instructor = JOptionPane.showInputDialog(this, "Enter Instructor:", 
                                                         "Create Course", 
                                                         JOptionPane.QUESTION_MESSAGE);
        if (instructor == null || instructor.trim().isEmpty()) {
            return;
        }

        // 7. 输入最大容量
        String maxCapacityStr = JOptionPane.showInputDialog(this, "Enter Max Capacity:", 
                                                             "Create Course", 
                                                             JOptionPane.QUESTION_MESSAGE);
        if (maxCapacityStr == null || maxCapacityStr.trim().isEmpty()) {
            return;
        }
        
        int maxCapacity;
        try {
            maxCapacity = Integer.parseInt(maxCapacityStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid max capacity. Please enter a number.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 创建课程（会自动保存到 CSV 文件）
        boolean success = systemManager.createCourse(courseId.trim(), courseName.trim(), 
                                                     time.trim(), location.trim(), 
                                                     credits, instructor.trim(), maxCapacity);

        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Course created successfully!\nCourse ID: " + courseId, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create course.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editCourse() {
        // 1. 输入课程ID
        String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to edit:", 
                                                       "Edit Course", 
                                                       JOptionPane.QUESTION_MESSAGE);
        if (courseId == null || courseId.trim().isEmpty()) {
            return;
        }

        // 查找课程
        Course course = systemManager.findCourse(courseId.trim());
        if (course == null) {
            JOptionPane.showMessageDialog(this, 
                "Course not found: " + courseId, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 显示当前课程信息
        String currentInfo = String.format(
            "Current Course Info:\n\n" +
            "Course ID: %s\n" +
            "Course Name: %s\n" +
            "Time: %s\n" +
            "Location: %s\n" +
            "Credits: %d\n" +
            "Instructor: %s\n" +
            "Max Capacity: %d\n" +
            "Current Enrollment: %d\n\n" +
            "Enter new values below:",
            course.getCourseId(), course.getCourseName(), course.getTime(), 
            course.getLocation(), course.getCredits(), course.getInstructor(), 
            course.getMaxCapacity(), course.getCurrentEnrollment()
        );

        JOptionPane.showMessageDialog(this, currentInfo, 
                                      "Edit Course", 
                                      JOptionPane.INFORMATION_MESSAGE);

        // 2. 输入课程名称（提示当前值）
        String courseName = JOptionPane.showInputDialog(this, 
            "Course Name (Current: " + course.getCourseName() + "):", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (courseName == null || courseName.trim().isEmpty()) {
            return;
        }

        // 3. 输入时间（提示当前值）
        String time = JOptionPane.showInputDialog(this, 
            "Course Time (Current: " + course.getTime() + "):\n(e.g., Mon/Wed 6:30-7:45)", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (time == null || time.trim().isEmpty()) {
            return;
        }

        // 4. 输入地点（提示当前值）
        String location = JOptionPane.showInputDialog(this, 
            "Location (Current: " + course.getLocation() + "):", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (location == null || location.trim().isEmpty()) {
            return;
        }

        // 5. 输入学分（提示当前值）
        String creditsStr = JOptionPane.showInputDialog(this, 
            "Credits (Current: " + course.getCredits() + "):", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (creditsStr == null || creditsStr.trim().isEmpty()) {
            return;
        }
        
        int credits;
        try {
            credits = Integer.parseInt(creditsStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid credits. Please enter a number.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 6. 输入教师（提示当前值）
        String instructor = JOptionPane.showInputDialog(this, 
            "Instructor (Current: " + course.getInstructor() + "):", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (instructor == null || instructor.trim().isEmpty()) {
            return;
        }

        // 7. 输入最大容量（提示当前值）
        String maxCapacityStr = JOptionPane.showInputDialog(this, 
            "Max Capacity (Current: " + course.getMaxCapacity() + "):", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (maxCapacityStr == null || maxCapacityStr.trim().isEmpty()) {
            return;
        }
        
        int maxCapacity;
        try {
            maxCapacity = Integer.parseInt(maxCapacityStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid max capacity. Please enter a number.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 8. 输入当前注册人数（提示当前值）
        String currentEnrollmentStr = JOptionPane.showInputDialog(this, 
            "Current Enrollment (Current: " + course.getCurrentEnrollment() + "):", 
            "Edit Course", JOptionPane.QUESTION_MESSAGE);
        if (currentEnrollmentStr == null || currentEnrollmentStr.trim().isEmpty()) {
            return;
        }
        
        int currentEnrollment;
        try {
            currentEnrollment = Integer.parseInt(currentEnrollmentStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid current enrollment. Please enter a number.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 修改课程（会自动保存到 CSV 文件）
        boolean success = systemManager.editCourse(courseId.trim(), courseName.trim(), 
                                                  time.trim(), location.trim(), 
                                                  credits, instructor.trim(), 
                                                  maxCapacity, currentEnrollment);

        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Course updated successfully!\nCourse ID: " + courseId, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update course.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Manage student holds
     * Allows admin to view, add, or remove holds on student accounts
     */
    private void manageHolds() {
        String[] options = {"View All Holds", "Add Hold", "Remove Hold", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "What would you like to do?",
                "Manage Student Holds",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            viewAllHolds();
        } else if (choice == 1) {
            addHold();
        } else if (choice == 2) {
            removeHold();
        }
    }

    private void viewAllHolds() {
        java.util.Map<String, String> holds = systemManager.getAllHolds();
        
        if (holds.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No holds on any student accounts.",
                    "All Holds",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder message = new StringBuilder("Current Holds:\n\n");
        for (java.util.Map.Entry<String, String> entry : holds.entrySet()) {
            message.append("Student ID: ").append(entry.getKey())
                   .append("\nReason: ").append(entry.getValue())
                   .append("\n\n");
        }

        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "All Holds",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void addHold() {
        String studentId = JOptionPane.showInputDialog(
                this,
                "Enter Student ID:",
                "Add Hold",
                JOptionPane.QUESTION_MESSAGE
        );

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        // Check if student exists
        if (systemManager.findStudent(studentId) == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Student not found: " + studentId,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Check if student already has a hold
        if (systemManager.hasHold(studentId)) {
            String currentReason = systemManager.getHoldReason(studentId);
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Student already has a hold:\n" + currentReason + "\n\nReplace it?",
                    "Hold Exists",
                    JOptionPane.YES_NO_OPTION
            );
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        String reason = JOptionPane.showInputDialog(
                this,
                "Enter hold reason:",
                "Add Hold",
                JOptionPane.QUESTION_MESSAGE
        );

        if (reason == null) {
            return;
        }

        boolean success = systemManager.placeHoldOnAccount(studentId.trim(), reason);
        if (success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Hold added successfully for student: " + studentId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add hold.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void removeHold() {
        String studentId = JOptionPane.showInputDialog(
                this,
                "Enter Student ID:",
                "Remove Hold",
                JOptionPane.QUESTION_MESSAGE
        );

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        if (!systemManager.hasHold(studentId)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Student does not have a hold: " + studentId,
                    "No Hold Found",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String reason = systemManager.getHoldReason(studentId);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove hold for student: " + studentId + "\nReason: " + reason + "\n\nAre you sure?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = systemManager.removeHoldOnAccount(studentId.trim());
            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Hold removed successfully for student: " + studentId,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to remove hold.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
