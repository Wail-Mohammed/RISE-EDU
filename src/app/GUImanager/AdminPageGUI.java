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
 * Administrator menu page using Swing windows instead of dialogs.
 * This matches the mockup with six large blue buttons for each admin task.
 */
public class AdminPageGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND = new Color(230, 230, 230);
	private static final Color PRIMARY_BUTTON = new Color(100, 170, 255);
	private final String welcomeMessage;

	//this is the constructor we call after admin login so the dashboard shows up
	public AdminPageGUI(String welcomeMessage) {
		super("Administrator Dashboard");
		this.welcomeMessage = welcomeMessage;
		initializeFrame();
	}

	//this is the frame setup so the admin sees a full window with big buttons
	private void initializeFrame() {
		setSize(900, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(BACKGROUND);
		getContentPane().setLayout(new BorderLayout());
		add(buildContent(), BorderLayout.CENTER);
	}

	//this is the layout that arranges the title and six action buttons
	private JPanel buildContent() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));

		//this is the welcome label that mimics the mockup header
		JLabel title = new JLabel(welcomeMessage, SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
		wrapper.add(title, BorderLayout.NORTH);

		//this is the 2x3 grid that spaces the six admin shortcuts
		JPanel grid = new JPanel(new GridLayout(2, 3, 20, 20));
		grid.setOpaque(false);
		grid.add(createActionButton("Create a course"));
		grid.add(createActionButton("Edit a course"));
		grid.add(createActionButton("Delete a course"));
		grid.add(createActionButton("Generate report"));
		grid.add(createActionButton("View students schedules"));
		grid.add(createActionButton("Student holds"));

		wrapper.add(grid, BorderLayout.CENTER);
		return wrapper;
	}

	//this is the helper that styles each blue admin button
	private JButton createActionButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(200, 120));
		button.setBackground(PRIMARY_BUTTON);
		button.setForeground(Color.BLACK);
		button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		button.setOpaque(true);
		button.addActionListener(e -> showComingSoon(label));
		return button;
	}

	//this is the placeholder until SystemManager is wired up
	private void showComingSoon(String action) {
		JOptionPane.showMessageDialog(
				this,
				action + " will be connected to the SystemManager in a later phase.",
				"Feature placeholder",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
}

