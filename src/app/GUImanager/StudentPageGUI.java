package app.GUImanager;

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

/**
 * basic flow — student page.
 * Shows a full window with four large buttons that hook into our existing dialog logic.
 */
public class StudentPageGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND = new Color(230, 230, 230);
	private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);

	//this is the constructor we call after a student logs in successfully
	public StudentPageGUI(String welcomeMessage) {
		super("Welcome student!");
		initializeFrame();
	}

	//this is the frame setup so the student sees a wide centered dashboard window
	private void initializeFrame() {
		setSize(1100, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(BACKGROUND);
		getContentPane().setLayout(new BorderLayout());
		add(buildContent(), BorderLayout.CENTER);
	}

	//this is the content panel that stacks the welcome label and the big buttons
	private JPanel buildContent() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.setBorder(BorderFactory.createEmptyBorder(40, 80, 60, 80));

		//this is the welcome label that mimics the mockup header
		JLabel title = new JLabel("Welcome student!", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
		wrapper.add(title, BorderLayout.NORTH);

		//this is the 2x2 grid that spaces out the four blue buttons
		JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
		grid.setOpaque(false);
		grid.add(createActionButton("Add a course", this::addCourse));
		grid.add(createActionButton("Drop a course", this::dropCourse));
		grid.add(createActionButton("View schedule", this::viewSchedule));
		grid.add(createActionButton("View courses", this::viewCourses));

		wrapper.add(grid, BorderLayout.CENTER);
		return wrapper;
	}

	//this is the helper that styles each blue rounded button while wiring actions
	private JButton createActionButton(String label, Runnable action) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(240, 140));
		button.setBackground(PRIMARY_BUTTON);
		button.setForeground(Color.BLACK);
		button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		button.setOpaque(true);
		button.addActionListener(e -> action.run());
		return button;
	}

	//this is the existing enrollment flow connected to the new button
	private void addCourse() {
		String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to enroll:");
		if (courseId == null || courseId.isBlank())
			return;

		JOptionPane.showMessageDialog(this,
				"Enroll flow will use SystemManager once integrated.\nInput recorded for: " + courseId.trim(),
				"Enroll Course",
				JOptionPane.INFORMATION_MESSAGE);
	}

	//this is the existing drop flow
	private void dropCourse() {
		String courseId = JOptionPane.showInputDialog(this, "Enter Course ID to drop:");
		if (courseId == null || courseId.isBlank())
			return;

		JOptionPane.showMessageDialog(this,
				"Drop flow will use SystemManager once integrated.\nInput recorded for: " + courseId.trim(),
				"Drop Course",
				JOptionPane.INFORMATION_MESSAGE);
	}

	//this is the existing schedule flow
	private void viewSchedule() {
		JOptionPane.showMessageDialog(this,
				"Schedule view will be connected to SystemManager in a later phase.",
				"Your Schedule",
				JOptionPane.INFORMATION_MESSAGE);
	}

	//this is a placeholder for viewing available courses
	private void viewCourses() {
		JOptionPane.showMessageDialog(this,
				"Course catalog will be connected to SystemManager in a later phase.",
				"Available Courses",
				JOptionPane.INFORMATION_MESSAGE);
	}
}

