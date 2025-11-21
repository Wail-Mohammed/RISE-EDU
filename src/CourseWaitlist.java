
public class CourseWaitlist {
private SystemManager sysMngr; // using the system manager to call the methods it holds
	
	// Constructor
	public CourseWaitlist(SystemManager sysMngr) {
		this.sysMngr = sysMngr;
	}
	
	public Message waitlist(Message input) { // using this method to get and send msg feedback to and from user
		
		String txt = intput.getText(); // using this for the text input
		
		if (txt == null) { // using this in case user doesn't send any message or text
			return new Message(Type.ENROLL_STUDENT, Status.FAIL);
			
		}
		
		String[] separate = txt.split(","); // segregation of inpput
		String stdntID = separate[0].trim(); // using this for student ID
		String crseID = separate[1].trim(); // using this for course ID
		
		boolean waitlistAdd = systemManager.addStudentToWaitlist(stdntID, crseID); // using this to add
		
		if (waitlistAdd = false) {
			return new Message(Type.WAITLIST_COURSE, Status.FAIL, "Course seats are full");
		}
		
		int waitlistPosition = systemManager.getWaitlistPosition(stdntIF, crseID);
		
		return new Message(Type.WAITLIST_COURSE, Status.SUCCESS, "Your position in the waitlist is " + waitlistPosition);
}
}
