package Server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

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
        assertEquals(student1, manager.findUser(university, "student1"));
    }

    @Test
    void testFindUserNotFound() {
        assertNull(manager.findUser(university, "unknown"));
    }

    @Test
    void testAuthenticateUserSuccess() {
        assertEquals(Status.SUCCESS, manager.authenticateUser("student1", "password1", "STUDENT", "U").getStatus());
    }

    @Test
    void testAuthenticateUserWrongPassword() {
        assertEquals(Status.FAIL, manager.authenticateUser("student1", "wrongPassword", "STUDENT", "U").getStatus());
    }

    @Test
    void testAuthenticateUserNonExistent() {
        assertEquals(Status.FAIL, manager.authenticateUser("noUser", "noPassword", "STUDENT", "U").getStatus());
    }

    @Test
    void testAuthenticateUserUniversityNotFound() {
        assertEquals(Status.FAIL, manager.authenticateUser("student1", "password1", "STUDENT", "NonExistent").getStatus());
    }

    @Test
    void testAuthenticateUserWrongType() {
        assertEquals(Status.FAIL, manager.authenticateUser("student1", "password1", "ADMIN", "U").getStatus());
    }

    @Test
    void testProcessEnrollment() {
        assertEquals(Status.SUCCESS, manager.processEnrollment(university, "student1", "Course1").getStatus());
    }

    @Test
    void testGetStudentSchedule() {
        manager.processEnrollment(university, "student1", "Course1");
        assertEquals(Status.SUCCESS, manager.getStudentSchedule(university, "student1").getStatus());
    }

    @Test
    void testProcessDrop() {
        manager.processEnrollment(university, "student1", "Course1");
        assertEquals(Status.SUCCESS, manager.processDrop(university, "student1", "Course1").getStatus());
    }

    @Test
    void testProcessEnrollmentNonExistentStudent() {
        assertEquals(Status.FAIL, manager.processEnrollment(university, "nonExistent", "Course1").getStatus());
    }

    @Test
    void testProcessEnrollmentInvalidCourse() {
        assertEquals(Status.FAIL, manager.processEnrollment(university, "student1", "InvalidCourse").getStatus());
    }

    @Test
    void testProcessEnrollmentWithHold() {
        student1.addHold("missing payment");
        assertEquals(Status.FAIL, manager.processEnrollment(university, "student1", "Course1").getStatus());
        student1.removeHold("missing payment");
    }

    @Test
    void testProcessEnrollmentDuplicate() {
        manager.processEnrollment(university, "student1", "Course1");
        assertEquals(Status.FAIL, manager.processEnrollment(university, "student1", "Course1").getStatus());
    }

    @Test
    void testDropWhenCourseRemovedCheck() {
        manager.processEnrollment(university, "student1", "Course1");
        assertTrue(university.getAllCourses().remove(course1));
        assertEquals(Status.SUCCESS, manager.processDrop(university, "student1", "Course1").getStatus());
    }

    @Test
    void testScheduleForStudentCheck() {
        assertEquals(Status.FAIL, manager.getStudentSchedule(university, "missing Student").getStatus());
    }

    @Test
    void testProcessDropStudentNotFound() {
        assertEquals(Status.FAIL, manager.processDrop(university, "nonExistent", "Course1").getStatus());
    }

    @Test
    void testProcessDropCourseNotFound() {
        assertEquals(Status.FAIL, manager.processDrop(university, "student1", "NonExistent").getStatus());
    }

    @Test
    void testProcessDropByStudentIdSuccess() {
        manager.processEnrollment(university, "student1", "Course1");
        assertEquals(Status.SUCCESS, manager.processDropByStudentId(university, "S0011", "Course1").getStatus());
    }

    @Test
    void testProcessDropByStudentIdNotFound() {
        assertEquals(Status.FAIL, manager.processDropByStudentId(university, "InvalidID", "Course1").getStatus());
    }

    @Test
    void testGetStudentScheduleByStudentIdSuccess() {
        manager.processEnrollment(university, "student1", "Course1");
        var msg = manager.getStudentScheduleByStudentId(university, "S0011");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertNotNull(msg.getList());
        assertFalse(msg.getList().isEmpty());
        String courseData = msg.getList().get(0);
        assertTrue(courseData.contains("Course1"));
        assertTrue(courseData.contains("|"));
    }

    @Test
    void testGetStudentScheduleByStudentIdNotFound() {
        assertEquals(Status.FAIL, manager.getStudentScheduleByStudentId(university, "InvalidID").getStatus());
    }

    @Test
    void testGetStudentScheduleByStudentIdEmpty() {
        var msg = manager.getStudentScheduleByStudentId(university, "S0011");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertTrue(msg.getText().contains("empty"));
    }

    @Test
    void testGetStudentHoldsWithHolds() {
        student1.addHold("Payment due");
        student1.addHold("Document missing");
        var msg = manager.getStudentHolds(university, "student1");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertNotNull(msg.getList());
        assertTrue(msg.getList().size() > 0);
    }

    @Test
    void testGetStudentHoldsNoHolds() {
        var msg = manager.getStudentHolds(university, "student1");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertTrue(msg.getText().contains("No holds"));
    }

    @Test
    void testGetStudentHoldsStudentNotFound() {
        assertEquals(Status.FAIL, manager.getStudentHolds(university, "nonExistent").getStatus());
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
        assertEquals(Status.SUCCESS, manager.addUser(university, userArgs).getStatus());
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
        manager.addUser(university, userArgs);
        assertEquals(Status.FAIL, manager.addUser(university, userArgs).getStatus());
    }

    @Test
    void testAddUserMissingArgs() {
        ArrayList<String> userArgs = new ArrayList<>();
        userArgs.add("STUDENT");
        userArgs.add("student2");
        assertEquals(Status.FAIL, manager.addUser(university, userArgs).getStatus());
    }

    @Test
    void testAddUserAdmin() {
        ArrayList<String> userArgs = new ArrayList<>();
        userArgs.add("ADMIN");
        userArgs.add("admin2");
        userArgs.add("pass2");
        userArgs.add("Admin");
        userArgs.add("User");
        userArgs.add("A0002");
        assertEquals(Status.SUCCESS, manager.addUser(university, userArgs).getStatus());
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
        assertEquals(Status.SUCCESS, manager.createCourse(university, courseArgs).getStatus());
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
        manager.createCourse(university, courseArgs);
        assertEquals(Status.FAIL, manager.createCourse(university, courseArgs).getStatus());
    }

    @Test
    void testCreateCourseMissingArgs() {
        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        assertEquals(Status.FAIL, manager.createCourse(university, courseArgs).getStatus());
    }

    @Test
    void testCreateCourseInvalidNumbers() {
        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        courseArgs.add("CourseName");
        courseArgs.add("Time");
        courseArgs.add("Room");
        courseArgs.add("invalid");
        courseArgs.add("Instructor");
        courseArgs.add("10");
        assertEquals(Status.FAIL, manager.createCourse(university, courseArgs).getStatus());
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
        manager.createCourse(university, courseArgs);
        
        ArrayList<String> editArgs = new ArrayList<>();
        editArgs.add("Course2");
        editArgs.add("UpdatedCourse");
        editArgs.add("15");
        assertEquals(Status.SUCCESS, manager.editCourse(university, editArgs).getStatus());
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
        manager.createCourse(university, courseArgs);
        
        ArrayList<String> badArgs = new ArrayList<>();
        badArgs.add("Course2");
        badArgs.add("WrongCourse");
        badArgs.add("NotANumber");
        assertEquals(Status.FAIL, manager.editCourse(university, badArgs).getStatus());
    }

    @Test
    void testEditCourseNotFound() {
        ArrayList<String> editArgs = new ArrayList<>();
        editArgs.add("NonExistent");
        editArgs.add("UpdatedCourse");
        editArgs.add("15");
        assertEquals(Status.FAIL, manager.editCourse(university, editArgs).getStatus());
    }

    @Test
    void testEditCourseMissingArgs() {
        ArrayList<String> editArgs = new ArrayList<>();
        editArgs.add("Course1");
        assertEquals(Status.FAIL, manager.editCourse(university, editArgs).getStatus());
    }

    @Test
    void testDeleteCourseSuccess() {
        assertEquals(Status.SUCCESS, manager.deleteCourse(university, "Course1").getStatus());
    }

    @Test
    void testDeleteCourseNotFound() {
        assertEquals(Status.FAIL, manager.deleteCourse(university, "NoSuchCourse").getStatus());
    }

    @Test
    void testPlaceHoldOnAccountSuccess() {
        assertEquals(Status.SUCCESS, manager.placeHoldOnAccount(university, "S0011", "Reason").getStatus());
    }

    @Test
    void testRemoveHoldOnAccountSuccess() {
        manager.placeHoldOnAccount(university, "S0011", "Reason");
        assertEquals(Status.SUCCESS, manager.removeHoldOnAccount(university, "S0011", "Reason").getStatus());
    }

    @Test
    void testPlaceHoldOnAccountInvalidId() {
        assertEquals(Status.FAIL, manager.placeHoldOnAccount(university, "InvalidID", "Reason").getStatus());
    }

    @Test
    void testRemoveHoldOnAccountInvalidId() {
        assertEquals(Status.FAIL, manager.removeHoldOnAccount(university, "InvalidID", "Reason").getStatus());
    }

    @Test
    void testGetAllCourses() {
        assertEquals(Status.SUCCESS, manager.getAllCourses(university).getStatus());
    }

    @Test
    void testGetAllStudents() {
        assertEquals(Status.SUCCESS, manager.getAllStudents(university).getStatus());
    }

    @Test
    void testGetAllAdmins() {
        assertEquals(Status.SUCCESS, manager.getAllAdmins(university).getStatus());
        var msg = manager.getAllAdmins(university);
        assertNotNull(msg.getList());
    }

    @Test
    void testGetReport() {
        assertEquals(Status.SUCCESS, manager.getReport(university).getStatus());
        var msg = manager.getReport(university);
        assertNotNull(msg.getText());
        assertTrue(msg.getText().contains("SYSTEM REPORT"));
        assertTrue(msg.getText().contains("----------------"));
    }
    
    @Test
    void testEnrollmentListEmptyCheck() {
        var msg = manager.getEnrollmentList(university, "Course1");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertEquals("No students enrolled in this course.", msg.getText());
        assertNotNull(msg.getList());
        assertTrue(msg.getList().isEmpty());
    }

    @Test
    void testEnrollmentListWithStudentsCheck() {
        Student student2 = new Student("student2", "password2", "Second", "User", "S0002");
        university.addStudent(student2);
        
        course1.setMaxCapacity(2);
        
        manager.processEnrollment(university, "student1", "Course1");
        manager.processEnrollment(university, "student2", "Course1");

        var msg = manager.getEnrollmentList(university, "Course1");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertEquals("Enrollment List for Course1", msg.getText());

        ArrayList<String> list = msg.getList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains("S0011 - First Last (student1)"));
        assertTrue(list.contains("S0002 - Second User (student2)"));
    }

    @Test
    void testGetEnrollmentListCourseNotFound() {
        assertEquals(Status.FAIL, manager.getEnrollmentList(university, "NonExistent").getStatus());
    }
    
    @Test
    void testPrereqCheckAtProcessEnrollment() {
    	course1.addPrerequisite("CS401");
    	var info = manager.processEnrollment(university, "student1", "Course1");
    	assertEquals(Status.FAIL, info.getStatus());
    }

    @Test
    void testPrereqCheckSuccess() {
    	Course prereqCourse = new Course("CS401", "SWE", "TBA", "TBA", 3, "Smith", 35, 0);
    	university.addCourse(prereqCourse);
    	manager.processEnrollment(university, "student1", "CS401");
    	
    	course1.addPrerequisite("CS401");
    	var info = manager.processEnrollment(university, "student1", "Course1");
    	assertEquals(Status.SUCCESS, info.getStatus());
    }

    @Test
    void testPrereqCheckMultiplePrereqs() {
    	Course prereq1 = new Course("CS401", "SWE", "TBA", "TBA", 3, "Smith", 35, 0);
    	Course prereq2 = new Course("CS311", "Programming Languages Concepts", "MW 11:00", "N212", 3, "Dr. kang", 35, 0);
    	university.addCourse(prereq1);
    	university.addCourse(prereq2);
    	
    	manager.processEnrollment(university, "student1", "CS401");
    	manager.processEnrollment(university, "student1", "CS311");
    	
    	course1.addPrerequisite("CS401");
    	course1.addPrerequisite("CS311");
    	var info = manager.processEnrollment(university, "student1", "Course1");
    	assertEquals(Status.SUCCESS, info.getStatus());
    }

    @Test
    void testPrereqCheckMultiplePrereqsMissingOne() {
    	Course prereq1 = new Course("CS401", "SWE", "TBA", "TBA", 3, "Smith", 35, 0);
    	Course prereq2 = new Course("CS311", "Programming Languages Concepts", "MW 11:00", "N212", 3, "Dr. kang", 35, 0);
    	university.addCourse(prereq1);
    	university.addCourse(prereq2);
    	
    	manager.processEnrollment(university, "student1", "CS401");
    	
    	course1.addPrerequisite("CS401");
    	course1.addPrerequisite("CS311");
    	var info = manager.processEnrollment(university, "student1", "Course1");
    	assertEquals(Status.FAIL, info.getStatus());
    }

    @Test
    void testPrereqCheckNoPrereqs() {
    	var info = manager.processEnrollment(university, "student1", "Course1");
    	assertEquals(Status.SUCCESS, info.getStatus());
    }
    
    @Test
    void testWaitlistFullAtProcessEnrollment() {
    	assertEquals(Status.SUCCESS, manager.processEnrollment(university, "student1", "Course1").getStatus());
    	
    	Student secondStudent = new Student("student2", "password2", "Second", "User", "S0002");
    	university.addStudent(secondStudent);
    	
    	var info = manager.processEnrollment(university, "student2", "Course1");
    	
    	assertEquals(Status.FAIL, info.getStatus());
    	assertTrue(info.getText().contains("waitlist"));
    	
    	assertEquals(1, course1.getWaitlist().size());
    	assertTrue(course1.getWaitlist().contains("student2"));
    }

    @Test
    void testWaitlistMultipleStudents() {
    	assertEquals(Status.SUCCESS, manager.processEnrollment(university, "student1", "Course1").getStatus());
    	
    	Student student2 = new Student("student2", "password2", "Second", "User", "S0002");
    	Student student3 = new Student("student3", "password3", "Third", "User", "S0003");
    	university.addStudent(student2);
    	university.addStudent(student3);
    	
    	manager.processEnrollment(university, "student2", "Course1");
    	manager.processEnrollment(university, "student3", "Course1");
    	
    	assertEquals(2, course1.getWaitlist().size());
    	assertTrue(course1.getWaitlist().contains("student2"));
    	assertTrue(course1.getWaitlist().contains("student3"));
    }

    @Test
    void testGetStudentScheduleByStudentIdWithPrereqs() {
    	Course prereqCourse = new Course("CS311", "Programming Languages Concepts", "MW 11:00", "N212", 3, "Dr. kang", 35, 0);
    	Course mainCourse = new Course("CS401", "SWE", "TBA", "TBA", 3, "Smith", 35, 0);
    	university.addCourse(prereqCourse);
    	university.addCourse(mainCourse);
    	mainCourse.addPrerequisite("CS311");
    	manager.processEnrollment(university, "student1", "CS311");
    	manager.processEnrollment(university, "student1", "CS401");
    	
    	var msg = manager.getStudentScheduleByStudentId(university, "S0011");
    	assertEquals(Status.SUCCESS, msg.getStatus());
    	assertNotNull(msg.getList());
    	assertTrue(msg.getList().size() >= 2);
    	
    	boolean foundCS401 = false;
    	boolean foundCS311 = false;
    	for (String courseData : msg.getList()) {
    		if (courseData.contains("CS401")) {
    			foundCS401 = true;
    			assertTrue(courseData.contains("CS311"));
    		}
    		if (courseData.contains("CS311")) {
    			foundCS311 = true;
    		}
    	}
    	assertTrue(foundCS401);
    	assertTrue(foundCS311);
    }

    @Test
    void testGetStudentScheduleByStudentIdWithWaitlist() {
    	assertEquals(Status.SUCCESS, manager.processEnrollment(university, "student1", "Course1").getStatus());
    	
    	Student student2 = new Student("student2", "password2", "Second", "User", "S0002");
    	university.addStudent(student2);
    	manager.processEnrollment(university, "student2", "Course1");
    	
    	var msg = manager.getStudentScheduleByStudentId(university, "S0011");
    	assertEquals(Status.SUCCESS, msg.getStatus());
    	assertNotNull(msg.getList());
    	String courseData = msg.getList().get(0);
    	assertTrue(courseData.contains("Course1"));
    	assertTrue(courseData.contains("student2") || courseData.contains("None"));
    }
    @Test
    void testGetUniversity() {
        University result = manager.getUniversity("U");
        assertNotNull(result);
        assertEquals("U", result.getUniversityName());
    }

    @Test
    void testGetUniversityNotFound() {
        assertNull(manager.getUniversity("NonExistent"));
    }

    @Test
    void testCreateNewUniversity() {
        var msg = manager.createNewUniversity("NewUni");
        assertEquals(Status.SUCCESS, msg.getStatus());
        assertNotNull(manager.getUniversity("NewUni"));
    }

    @Test
    void testCreateNewUniversityDuplicate() {
        manager.createNewUniversity("NewUni2");
        var msg = manager.createNewUniversity("NewUni2");
        assertEquals(Status.FAIL, msg.getStatus());
    }

    @Test
    void testAddUniversity() {
        manager.addUniversity("U2");
        assertNotNull(manager.getUniversity("U2"));
    }

    @Test
    void testAddUniversityDuplicate() {
        manager.addUniversity("U3");
        manager.addUniversity("U3");
        assertNotNull(manager.getUniversity("U3"));
    }

    @Test
    void testGetAllUniversityNames() {
        manager.addUniversity("U4");
        Collection<String> names = manager.getAllUniversityNames();
        assertNotNull(names);
        assertTrue(names.contains("U"));
        assertTrue(names.contains("U4"));
    }

    @Test
    void testGetAllUniversities() {
        manager.addUniversity("U5");
        var allUnis = manager.getAllUniversities();
        ArrayList<String> uniNames = allUnis.getList();
        
        assertEquals(Status.SUCCESS, allUnis.getStatus());
        assertTrue(uniNames.contains("U"));
        assertTrue(uniNames.contains("U5"));
    }
}
