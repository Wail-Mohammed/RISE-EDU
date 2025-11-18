package SystemManager;
import java.util.*;
public class SystemManager {
	//Fields
	/**
	 * private dataManager DataManager
	 * private Universities universties List<University>
	 * */
	private Date addDropPeriodStart;
	private Date addDropPeriodEnd;
	private Date withdrawalPeriodStart;
	private Date withdrawalPeriodEnd;
	
	//Constructors
	public SystemManager() {
		
	}
	public void intializeSystem() {
		
	}
	
	// Methods
	/*
	 * authenticateUser(String username, String password, University universityID):User
	 * findStudent(studentID, universityID):Student
	 * addUniversity(universityName):University
	 * getUniversity(universityID):University
	 * findCourse():Course
	 */
	public boolean createCourse() {
		return true;
	}
	//add course:course, details
	public boolean editCourse() {
		return true;
	}
	//add course:course
	public boolean deleteCourse() {
		return true;
	}
	
	//add student: Student, course:course
	public boolean processEnrollment() {
		return true;
	}
	//add student: Student, course:course
	public boolean processDrop() {
		return true;
	}
	//add student: Student, course:course
	public boolean processWithdrawal() {
		return true;
	}

	//add student: Student, holdReason: String
	public boolean placeHoldOnAccount() {
		return true;
	}
	//add student: Student, holdReason: Hold
	public boolean removeHoldOnAccount() {
		return true;
	}
	
	/*
	 * getStudentSchedule(student: Student):Schedule
	 * generateEnrollmentReport(course:Course):Report
	 * */


}