
public class Enrollment {
	
	private SystemManager sysMngr; // using the system manager to call the methods it holds
	
	// Constructor
	public Enrollment(SystemManager sysMngr) {
		this.sysMngr = sysMngr;
	}
	
	public Message enrollment(Message input) { // using this method to get and send msg feedback to and from user
		
		String txt = intput.getText(); // using this for the text input
		
		if (txt == null) { // using this in case user doesn't send any message or text
			return new Message(Type.ENROLL_STUDENT, Status.FAIL);
			
		}
		
		String[] separate = txt.split(","); // segregation of inpput
		String stdntID = separate[0].trim(); // using this for student ID
		String crseID = separate[1].trim(); // using this for course ID
		
		boolean checkPrereq = sysMngr.checkStudentPrerequisites(stdntID, crseID); // using this for checking prerequisites
		
		if (checkPrereq = false) { // if prereqs not met
			return new Message(Type.ENROLL_STUDENT, Status.FAIL, "You will have to complete the Prerequisites for " + crseID);
		}
		
		boolean hasEnrollment = sysMngr.enrollStudentInCourse(stdntID, crseID); // enroll
		
		if(hasEnrollment) {
			return new Message(Type.ENROLL_STUDENT, Status.SUCCESS, "Enrollment Success for " + stdntID + " for " + crseID);
		}
		
		boolean seatFull = sysMngr.isCourseFull(crseID); // using this for when seats are filled up
		
		if (seatFull) {
			boolean added = sysMngr.addStudentToWaitlist(stdntID, crseID); // usign this for waitlisting
			
			if(waitlisted) {
				int waitlistPos = sysMngr.getWaitlistPosition(stdntID, crseID);
				return new Message(Type.ENROLL_STUDENT, Status.SUCCESS, "You have a placement number " + waitlistPos + "in the waitlist");
			}
			
		}
		
		return new Message(Type.ENROLL_STUDENT, Status.FAIL, "Enrollment Failed for " + stdntID);
		
		
	}
}
