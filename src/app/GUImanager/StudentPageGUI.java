package app.GUImanager;

import javax.swing.JOptionPane;

import SystemManager.SystemManager;
import User.Student;

/**
 *basic flow — student page
 The system displays a Student Menu dialog with the following options:

         “Enroll in Course”:

         “Drop Course”:

        “Join Waitlist”:

        “View Schedule”:

         “Logout”

The student selects one of the available actions.

    If the student chooses “Enroll in Course”:

        The system prompts for a Course ID.

    The student enters the Course ID.

        The system calls systemManager.enrollCourse to process the enrollment action.

        The system displays the result message (success, failure, or waitlist information).

    If the student chooses “Drop Course”:

        The system prompts for a Course ID.

        The student enters the Course ID.

        The system calls systemManager.dropCourse to drop the course.

        The system displays the result message.

    If the student chooses “Join Waitlist”:

        The system prompts for a Course ID.

        The student enters the Course ID.

        The system calls systemManager.joinWaitlist to add the student to the waitlist.

        The system displays the result message.

    If the student chooses “View Schedule”:

        The system calls systemManager.getStudentSchedule to get the current schedule.

        The system displays the current schedule in a message dialog.
When the student selects “Logout”, the system shows a logout confirmation message and exits the student menu loop.

Preconditions:
    The student must be logged in to the system.
    The student must have an existing account and is active.

Postconditions:
    SystemManager has been initialized
    he student has successfully authenticated through the LoginPage.

Postconditions:
    The student has chosen to log out of the system.
    */
public class StudentPageGUI {

    // Reference to the system manager handling the actual enrollment logic
	private final SystemManager systemManager;
    // Reference to the current student
	private final Student currentStudent;

    // Constructor to initialize the StudentPageGUI with the system manager and current student
	public StudentPageGUI(SystemManager systemManager, Student student) {
		this.systemManager = systemManager;
		this.currentStudent = student;
	}

	/**
	 * Main menu loop for student actions.
	 */
	public void processCommands() {

		String[] options = {
				"Enroll in Course",
				"Drop Course",
				"Join Waitlist",
				"View Schedule",
				"Logout"
		};

		int choice;

		do {
			choice = JOptionPane.showOptionDialog(
					null,
					"Welcome, " + currentStudent.getUsername()
							+ "\nSelect an action:",
					"Student Menu",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					null,
					options,
					options[0]
			);

			switch (choice) {
			case 0 -> doEnroll();
			case 1 -> doDrop();
			case 2 -> doWaitlist();
			case 3 -> doViewSchedule();
			default -> { /* do nothing */ }
			}

		} while (choice != 4); // "Logout"

		JOptionPane.showMessageDialog(null, "You have logged out.");
	}


	private void doEnroll() {
		String courseId = JOptionPane.showInputDialog("Enter Course ID to enroll:");
		if (courseId == null || courseId.isBlank())
			return;

		String result = systemManager.enrollCourse(currentStudent, courseId.trim());

		JOptionPane.showMessageDialog(
				null,
				result,
				"Enroll Result",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void doDrop() {
		String courseId = JOptionPane.showInputDialog("Enter Course ID to drop:");
		if (courseId == null || courseId.isBlank())
			return;

		String result = systemManager.dropCourse(currentStudent, courseId.trim());

		JOptionPane.showMessageDialog(
				null,
				result,
				"Drop Result",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void doWaitlist() {
		String courseId = JOptionPane.showInputDialog("Enter Course ID to join waitlist:");
		if (courseId == null || courseId.isBlank())
			return;

		String result = systemManager.joinWaitlist(currentStudent, courseId.trim());

		JOptionPane.showMessageDialog(
				null,
				result,
				"Waitlist Result",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void doViewSchedule() {
		String schedule = systemManager.getStudentSchedule(currentStudent);

		JOptionPane.showMessageDialog(
				null,
				schedule,
				"Your Schedule",
				JOptionPane.INFORMATION_MESSAGE
		);
	}
}

