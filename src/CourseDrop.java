
public class CourseDrop {
	private SystemManager sysMngr;
	
	// Constructor
	public CourseDrop(SystemManager sysMngr) {
		this.sysMngr = sysMngr;
	}
	
	public Message dropCrse (Message input) { // using this method to get and send msg feedback to and from user
		
		String txt = intput.getText(); // using this for the text input
		
		if (txt == null) { // using this in case user doesn't send any message or text
			return new Message(Type.DROP_COURSE, Status.FAIL);
			
		}
		
		String[] separate = txt.split(","); // segregation of inpput
		String stdntID = separate[0].trim(); // using this for student ID
		String crseID = separate[1].trim(); // using this for course ID
		
		boolean dropCourse	= sysMngr.dropStudentFromCourse(stdntID, crseID); // using this to retrun true if dropping of course is successful 
		// and false if otherwise
		
		if (dropCourse == false) {
			return new Message(Type.DROP_COURSE,Status.FAIL, "You were never enrolled in " + crseID); // using this for when the student was never enrolled in the course
			// in the  first place
		}
		
		sysMngr.promoteStudentFromWaitlist(crseID); // using this to move student from waitlist into the course when a student drops
		
		return new Message(Status.SUCCESS, "Course " + crseID + "has been successfully dropped");
}
