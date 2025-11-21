
public class CoursePrerequisitesCheck {
private SystemManager sysMngr; // using the system manager to call the methods it holds
	
	// Constructor
	public CoursePrerequisitesCheck(SystemManager sysMngr) {
		this.sysMngr = sysMngr;
	}
	
	public Message prereqChk(Message input) { // using this method to get and send msg feedback to and from user
		
		String txt = intput.getText(); // using this for the text input
		
		if (txt == null) { // using this in case user doesn't send any message or text
			return new Message(Type.ENROLL_STUDENT, Status.FAIL);
			
		}
		
		String[] separate = txt.split(","); // segregation of inpput
		String stdntID = separate[0].trim(); // using this for student ID
		String crseID = separate[1].trim(); // using this for course ID
		
		boolean checkPrereq = systemManager.checkStudentPrereuisites(stdntID, crseID); // using this to check prerequisites
		
		if (checkPrereq) {
			return new Message(Type.PREREUISITES_CHECK, Status.SUCCESS, "Met prerequisites for " + crseID);
		}
		
		return new Message(Type.PREREUISITES_CHECK, Status.FAIL, "You have not met the prerequisites for " + crseID);
}
}
