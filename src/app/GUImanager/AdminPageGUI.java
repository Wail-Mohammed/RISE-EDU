package app.GUImanager;

import javax.swing.JOptionPane;

import SystemManager.SystemManager;
import User.Administrator;

/**
 * Administrator menu page using JOptionPane dialogs.
 *
 * Basic Flow – Administrator Menu:
    The system displays the Administrator Menu, showing the following options:

        Create Course:

        Edit Course:        

        Generate Enrollment Report:

        Manage Student Hold:

        Update Changes Report:  

        Logout:

    The administrator selects one of the available actions.

 */
public class AdminPageGUI {

	private final SystemManager systemManager;
	private final Administrator admin;

	public AdminPageGUI(SystemManager systemManager, Administrator admin) {
		this.systemManager = systemManager;
		this.admin = admin;
	}

	/**
	 * Main menu loop for administrator actions.
	 */
	public void processCommands() {

		String[] options = {
				"Create Course",
				"Edit Course",
				"Generate Enrollment Report",
				"Manage Student Hold",
				"Update Changes Report",
				"Logout"
		};

		int choice;

		do {
			choice = JOptionPane.showOptionDialog(
					null,
					"Welcome, Admin " + admin.getUsername()
							+ "\nSelect an action:",
					"Administrator Menu",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					options,
					options[0]
			);

			switch (choice) {
			case 0 -> doCreateCourse();
			case 1 -> doEditCourse();
			case 2 -> doGenerateReport();
			case 3 -> doManageHold();
			case 4 -> doUpdateChangesReport();
			default -> { /* do nothing */ }
			}

		} while (choice != 5); // "Logout"

		JOptionPane.showMessageDialog(null, "You have logged out.");
	}

	private void doCreateCourse() {

		String id = JOptionPane.showInputDialog("Enter Course ID:");
		if (id == null || id.isBlank())
			return;

		String title = JOptionPane.showInputDialog("Enter Course Title:");
		// allow blank? text says return if blank
		if (title == null || title.isBlank())
			return;

		String instructor = JOptionPane.showInputDialog("Enter Instructor Name:");
		if (instructor == null || instructor.isBlank())
			return;

		String capacity = JOptionPane.showInputDialog("Enter Class Size / Capacity:");
		if (capacity == null || capacity.isBlank())
			return;

		String result = systemManager.createCourse(
				id.trim(), title.trim(), instructor.trim(), capacity.trim()
		);

		JOptionPane.showMessageDialog(
				null,
				result,
				"Create Course Result",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void doEditCourse() {
		String id = JOptionPane.showInputDialog("Enter Course ID to edit:");
		if (id == null || id.isBlank())
			return;

		String newTitle = JOptionPane.showInputDialog("New Title (leave empty to skip):");
		if (newTitle == null)
			return;

		String newInstructor = JOptionPane.showInputDialog("New Instructor (leave empty to skip):");
		if (newInstructor == null)
			return;

		String newCap = JOptionPane.showInputDialog("New Capacity (leave empty to skip):");
		if (newCap == null)
			return;

		String result = systemManager.editCourse(
				id.trim(),
				newTitle.trim(),
				newInstructor.trim(),
				newCap.trim()
		);

		JOptionPane.showMessageDialog(
				null,
				result,
				"Edit Course Result",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void doGenerateReport() {
		String report = systemManager.generateEnrollmentReport();

		JOptionPane.showMessageDialog(
				null,
				report,
				"Enrollment Report",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void doManageHold() {
		String student = JOptionPane.showInputDialog("Enter Student Username:");
		if (student == null || student.isBlank())
			return;

		String[] holdActions = {"Add Hold", "Remove Hold", "Cancel"};
		int action = JOptionPane.showOptionDialog(
				null,
				"Manage Hold for " + student.trim(),
				"Hold Management",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				holdActions,
				holdActions[0]
		);

		if (action == 0) {
			String result = systemManager.addHold(student.trim());
			JOptionPane.showMessageDialog(null, result);
		} else if (action == 1) {
			String result = systemManager.removeHold(student.trim());
			JOptionPane.showMessageDialog(null, result);
		}
	}

	private void doUpdateChangesReport() {
		String result = systemManager.updateChangesReport();

		JOptionPane.showMessageDialog(
				null,
				result,
				"Changes Report",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
}

