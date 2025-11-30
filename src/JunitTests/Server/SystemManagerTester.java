package Server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.Server.SystemManager;
import app.Shared.Status;
import app.models.Course;
import app.models.Student;
import app.models.University;

public class SystemManagerTester {

	private SystemManager manager;
    private University university;
    private Student student1; 
    private Course course1;

    @BeforeEach
    void init() {
        university = new University("U");
        student1 = new Student("student1", "password1", "First", "Last", "S0011");
        course1 = new Course("Course1", "Title", "Time", "Location", 3, "Instructor", 1, 0);
        university.addStudent(student1);
        university.addCourse(course1);
        manager = SystemManager.getInstance();
        manager.loadUniversity(university);
    }

    @Test
    void testFindUserSuccess() {
        assertEquals(student1, manager.findUser("student1"));
    }

    @Test
    void testFindUserNotFound() {
        assertNull(manager.findUser("unknown"));
    }

    @Test
    void testAuthenticateUserSuccess() {
        assertEquals(Status.SUCCESS, manager.authenticateUser("student1", "password1").getStatus());
    }

    @Test
    void testAuthenticateUserWrongPassword() {
        assertEquals(Status.FAIL, manager.authenticateUser("student1", "wrongPassword").getStatus());
    }

    @Test
    void testAuthenticateUserNonExistent() {
        assertEquals(Status.FAIL, manager.authenticateUser("noUser", "noPassword").getStatus());
    }

    @Test
    void testProcessEnrollment() {
        assertEquals(Status.SUCCESS, manager.processEnrollment("student1", "Course1").getStatus());
    }

    @Test
    void testGetStudentSchedule() {
        manager.processEnrollment("student1", "Course1");
        assertEquals(Status.SUCCESS, manager.getStudentSchedule("student1").getStatus());
    }

    @Test
    void testProcessDrop() {
        manager.processEnrollment("student1", "Course1");
        assertEquals(Status.SUCCESS, manager.processDrop("student1", "Course1").getStatus());
    }

    @Test
    void testProcessEnrollmentNonExistentStudent() {
        assertEquals(Status.FAIL, manager.processEnrollment("nonExistent", "Course1").getStatus());
    }

    @Test
    void testProcessEnrollmentInvalidCourse() {
        assertEquals(Status.FAIL, manager.processEnrollment("student1", "InvalidCourse").getStatus());
    }

    @Test
    void testProcessEnrollmentWithHold() {
        student1.addHold("missing payment");
        assertEquals(Status.FAIL, manager.processEnrollment("student1", "Course1").getStatus());
        student1.removeHold("missing payment");
    }

    @Test
    void testProcessEnrollmentDuplicate() {
        manager.processEnrollment("student1", "Course1");
        assertEquals(Status.FAIL, manager.processEnrollment("student1", "Course1").getStatus());
    }

    @Test
    void dropWhenCourseRemovedCheck() {
        manager.processEnrollment("student1", "Course1");
        assertTrue(university.getAllCourses().remove(course1));
        assertEquals(Status.SUCCESS, manager.processDrop("student1", "Course1").getStatus());
    }

    @Test
    void scheduleForStudentCheck() {
        assertEquals(Status.FAIL, manager.getStudentSchedule("missing Student").getStatus());
    }

    @Test
    void testAddUserSuccess() {
        ArrayList<String> userArgs = new ArrayList<>();
        userArgs.add("STUDENT");
        userArgs.add("student2");
        userArgs.add("pass2");
        userArgs.add("First");
        userArgs.add("Last");
        userArgs.add("S0002");
        assertEquals(Status.SUCCESS, manager.addUser(userArgs).getStatus());
    }

    @Test
    void testAddUserDuplicate() {
        ArrayList<String> userArgs = new ArrayList<>();
        userArgs.add("STUDENT");
        userArgs.add("student2");
        userArgs.add("pass2");
        userArgs.add("First");
        userArgs.add("Last");
        userArgs.add("S0002");
        manager.addUser(userArgs);
        assertEquals(Status.FAIL, manager.addUser(userArgs).getStatus());
    }

    @Test
    void testCreateCourseSuccess() {
        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        courseArgs.add("CourseName");
        courseArgs.add("Time");
        courseArgs.add("Room");
        courseArgs.add("3");
        courseArgs.add("Instructor");
        courseArgs.add("10");
        assertEquals(Status.SUCCESS, manager.createCourse(courseArgs).getStatus());
    }

    @Test
    void testCreateCourseDuplicate() {
        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        courseArgs.add("CourseName");
        courseArgs.add("Time");
        courseArgs.add("Room");
        courseArgs.add("3");
        courseArgs.add("Instructor");
        courseArgs.add("10");
        manager.createCourse(courseArgs);
        assertEquals(Status.FAIL, manager.createCourse(courseArgs).getStatus());
    }

    @Test
    void testEditCourseSuccess() {
        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        courseArgs.add("CourseName");
        courseArgs.add("Time");
        courseArgs.add("Room");
        courseArgs.add("3");
        courseArgs.add("Instructor");
        courseArgs.add("10");
        manager.createCourse(courseArgs);
        
        ArrayList<String> editArgs = new ArrayList<>();
        editArgs.add("Course2");
        editArgs.add("UpdatedCourse");
        editArgs.add("15");
        assertEquals(Status.SUCCESS, manager.editCourse(editArgs).getStatus());
    }

    @Test
    void testEditCourseInvalidInput() {
        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        courseArgs.add("CourseName");
        courseArgs.add("Time");
        courseArgs.add("Room");
        courseArgs.add("3");
        courseArgs.add("Instructor");
        courseArgs.add("10");
        manager.createCourse(courseArgs);
        
        ArrayList<String> badArgs = new ArrayList<>();
        badArgs.add("Course2");
        badArgs.add("WrongCourse");
        badArgs.add("NotANumber");
        assertEquals(Status.FAIL, manager.editCourse(badArgs).getStatus());
    }

    @Test
    void testDeleteCourseSuccess() {
        assertEquals(Status.SUCCESS, manager.deleteCourse("Course1").getStatus());
    }

    @Test
    void testDeleteCourseNotFound() {
        assertEquals(Status.FAIL, manager.deleteCourse("NoSuchCourse").getStatus());
    }

    @Test
    void testPlaceHoldOnAccountSuccess() {
        assertEquals(Status.SUCCESS, manager.placeHoldOnAccount("S0011", "Reason").getStatus());
    }

    @Test
    void testRemoveHoldOnAccountSuccess() {
        manager.placeHoldOnAccount("S0011", "Reason");
        assertEquals(Status.SUCCESS, manager.removeHoldOnAccount("S0011", "Reason").getStatus());
    }

    @Test
    void testPlaceHoldOnAccountInvalidId() {
        assertEquals(Status.FAIL, manager.placeHoldOnAccount("InvalidID", "Reason").getStatus());
    }

    @Test
    void testRemoveHoldOnAccountInvalidId() {
        assertEquals(Status.FAIL, manager.removeHoldOnAccount("InvalidID", "Reason").getStatus());
    }

    @Test
    void testGetAllCourses() {
        assertEquals(Status.SUCCESS, manager.getAllCourses().getStatus());
    }

    @Test
    void testGetAllStudents() {
        assertEquals(Status.SUCCESS, manager.getAllStudents().getStatus());
    }

    @Test
    void testGetReport() {
        assertEquals(Status.SUCCESS, manager.getReport().getStatus());
        assertNotNull(manager.getReport().getText());
    }
    
    @Test
    void enrollmentListEmptyCheck() {
        var msg = manager.getEnrollmentList("Course1");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertEquals("No students enrolled in this course.", msg.getText());
        assertNotNull(msg.getList());
        assertTrue(msg.getList().isEmpty());
    }

    @Test
    void enrollmentListWithStudentsCheck() {
        // Create and add student2 to university
        Student student2 = new Student("student2", "password2", "Second", "User", "S0002");
        university.addStudent(student2);
        
        // Increase course capacity to allow 2 students
        course1.setMaxCapacity(2);
        
        manager.processEnrollment("student1", "Course1");
        manager.processEnrollment("student2", "Course1");

        var msg = manager.getEnrollmentList("Course1");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertEquals("Enrollment List for Course1", msg.getText());

        ArrayList<String> list = msg.getList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains("S0011 - First Last (student1)"));
        assertTrue(list.contains("S0002 - Second User (student2)"));
    }
    
    @Test
    void testPrereqCheckAtProcessEnrollment() {
    	course1.addPrerequisite("CS100"); // using this CS100 as prereq for first course
    	var info = manager.processEnrollment("Student1", "Course1");
    	assertEquals(Status.FAIL, info.getStatus());
    }
    
    @Test
    void testWaitlistFullAtProcessEnrollment() {
    	assertEquals(Status.SUCCESS, manager.processEnrollment("student1", "Course1").getStatus());
    	
    	Student secondStudent = new Student("student2", "password2", "Second", "User", "S0002");
    	
    	var info = manager.processEnrollment("student2", "Course1");
    	
    	assertEquals(Status.FAIL, info.getStatus()); // using this for when student cannot enroll due to class being full
    	
    	// now second student to be on the waitlist for first course1
    	assertEquals(1, course1.getWaitlist().size());
    	assertTrue(course1.getWaitlist().contains("student2"));
    }
    void testAddAndGetAllUniversities() {
        // Add a second university
        manager.addUniversity("U2");
        
        var allUnis = manager.getAllUniversities();
        ArrayList<String> uniNames = allUnis.getList();  // Assuming getAllUniversities returns a Message
        
        assertEquals(Status.SUCCESS, allUnis.getStatus());
        assertTrue(uniNames.contains("U"));
        assertTrue(uniNames.contains("U2"));
    }

    @Test
    void testSetActiveUniversity() {
        manager.addUniversity("U3");
        assertTrue(manager.setActiveUniversity("U3"));
        assertEquals("U3", manager.getActiveUniversity().getUniversityName());
        
        assertFalse(manager.setActiveUniversity("University doesn't exist"));
    }
}
