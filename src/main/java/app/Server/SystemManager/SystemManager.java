package main.java.app.Server.SystemManager;
import java.util.*;

import main.java.app.DataManager.DataManager;
import main.java.app.models.Course;
import main.java.app.models.Student;
import main.java.app.models.University;
import main.java.app.models.User;
public class SystemManager {
	//Fields
	private DataManager dataManager;               
    private List<University> universities;  
	private Date addDropPeriodStart;
	private Date addDropPeriodEnd;
	private Date withdrawalPeriodStart;
	private Date withdrawalPeriodEnd;
	
	//Constructors
	public SystemManager(String userFilePath, String courseFilePath, String logFilePath) {
	    this.dataManager = new DataManager(userFilePath, courseFilePath, logFilePath);
	    this.universities = new ArrayList<>();
	}
	public void intializeSystem() {
	// Load universities from the file (adjust if you have a separate CSV)
		this.universities = dataManager.loadUniversities(); 

	    for (int i = 0; i < universities.size(); i++) {
	        University uni = universities.get(i);
	        dataManager.loadUsers(uni);    
	        dataManager.loadCourses(uni);  
	    }
	    
	    System.out.println("System initialized: " + universities.size() + " universities loaded.");
	}
	
	// Methods
	/*
	 * authenticateUser(String username, String password, University universityID):User
	 * findStudent(studentID, universityID):Student
	 * addUniversity(universityName):University
	 * getUniversity(universityID):University
	 * findCourse():Course
	 */
	// Authenticates using username, password and university id
	public User authenticateUser(String username, String password, String universityID) {
	    University uni = getUniversity(universityID);
	    if (uni == null) return null;
	    return uni.verifyLogin(username, password);
	}
	
	// Finds student using student id and university id
	public Student findStudent(String studentID, String universityID) {
	    University uni = getUniversity(universityID);
	    if (uni == null) return null;
	    return uni.findStudent(studentID);
	}
	
	// Adds university 
	public University addUniversity(String universityName) {
	    University newUni = new University(universityName);
	    universities.add(newUni);
	    return newUni;
	}
	
	// Gets university by university id
	public University getUniversity(String universityID) {
	    for (University uni : universities) {
	        if (uni.getUniversityID().equals(universityID)) {
	            return uni;
	        }
	    }
	    return null;
	}
	
	// Finds course using course id and university id
	public Course findCourse(String courseID, String universityID) {
	    University uni = getUniversity(universityID);
	    if (uni == null) return null;
	    return uni.findCourse(courseID);
	}
	
	//create course:course
	public boolean createCourse(String universityID, Course course) {
	    University uni = getUniversity(universityID);
	    if (uni == null) return false;
	    return uni.addCourse(course);
	}
	
	//add course:course, details
	public boolean editCourse(String universityID, Course updatedCourse) {
	    University uni = getUniversity(universityID);
	    if (uni == null) return false;
	    return uni.updateCourse(updatedCourse);
	}
	
	//add course:course
	public boolean deleteCourse(String universityID, String courseID) {
	    University uni = getUniversity(universityID);
	    if (uni == null) return false;
	    return uni.removeCourse(courseID);
	}
	
	//add student: Student, course:course
	public boolean processEnrollment(Student student, Course course) {
	    return course.enrollStudent(student);
	}
	
	//add student: Student, course:course
	public boolean processDrop(Student student, Course course) {
	    return course.dropStudent(student);
	}
	
	//add student: Student, course:course
	public boolean processWithdrawal(Student student, Course course) {
	    return course.withdrawStudent(student);
	}

	//add student: Student, holdReason: String
	public boolean placeHoldOnAccount(Student student, String holdReason) {
	    return student.placeHold(holdReason);
	}
	
	//add student: Student, holdReason: Hold
	public boolean removeHoldOnAccount(Student student) {
	    return student.removeHold();
	}
	
	/*
	 * getStudentSchedule(student: Student):Schedule
	 * generateEnrollmentReport(course:Course):Report
	 * */
	// Gets the students schedule
	public Schedule getStudentSchedule(Student student) {
	    return student.getSchedule();
	}
	
	// Generates the report for enrollment in a class
	public Report generateEnrollmentReport(Course course) {
	    Report report = new Report("Enrollment Report");
	    StringBuilder sb = new StringBuilder();
	    sb.append("Course ID: ").append(course.getCourseID()).append("\n");
	    sb.append("Course Name: ").append(course.getCourseName()).append("\n");
	    sb.append("Enrolled Students:\n");

	    List<Student> students = course.getEnrolledStudents();
	    for (int i = 0; i < students.size(); i++) {
	        Student s = students.get(i);
	        sb.append("- ").append(s.getUserID())
	          .append(" | ").append(s.getUsername())
	          .append("\n");
	    }

	    report.generate(sb.toString());
	    dataManager.exportReport(report); 
	    return report;
	}

}