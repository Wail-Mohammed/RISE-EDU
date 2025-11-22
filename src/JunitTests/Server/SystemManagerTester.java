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
    void findUserCheck() {
        assertEquals(student1, manager.findUser("student1"));
        assertNull(manager.findUser("unknown"));
    }

    @Test
    void authenticateUserCheck() {
        assertEquals(Status.SUCCESS, manager.authenticateUser("student1", "password1").getStatus());
        assertEquals(Status.FAIL, manager.authenticateUser("student1", "wrongPassword").getStatus());
        assertEquals(Status.FAIL, manager.authenticateUser("noUser", "noPassword").getStatus());
    }

    @Test
    void enrollmentPathCheck() {
        assertEquals(Status.SUCCESS, manager.processEnrollment("student1", "Course1").getStatus());
        assertEquals(Status.SUCCESS, manager.getStudentSchedule("student1").getStatus());
        assertEquals(Status.SUCCESS, manager.processDrop("student1", "Course1").getStatus());
    }

    @Test
    void enrollmentEdgeCaseChecks() {
        assertEquals(Status.FAIL, manager.processEnrollment("nonExistent", "Course1").getStatus());
        assertEquals(Status.FAIL, manager.processEnrollment("student1", "InvalidCourse").getStatus());
        student1.addHold("missing payment");
        assertEquals(Status.FAIL, manager.processEnrollment("student1", "Course1").getStatus());
        student1.removeHold("missing payment");
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
    void addUserCreateCourseEditCourseChecks() {
        ArrayList<String> userArgs = new ArrayList<>();
        userArgs.add("STUDENT");
        userArgs.add("student2");
        userArgs.add("pass2");
        userArgs.add("First");
        userArgs.add("Last");
        userArgs.add("S0002");
        assertEquals(Status.SUCCESS, manager.addUser(userArgs).getStatus());
        assertEquals(Status.FAIL, manager.addUser(userArgs).getStatus());

        ArrayList<String> courseArgs = new ArrayList<>();
        courseArgs.add("Course2");
        courseArgs.add("CourseName");
        courseArgs.add("Time");
        courseArgs.add("Room");
        courseArgs.add("3");
        courseArgs.add("Instructor");
        courseArgs.add("10");
        assertEquals(Status.SUCCESS, manager.createCourse(courseArgs).getStatus());
        assertEquals(Status.FAIL, manager.createCourse(courseArgs).getStatus());

        ArrayList<String> editArgs = new ArrayList<>();
        editArgs.add("Course2");
        editArgs.add("UpdatedCourse");
        editArgs.add("15");
        assertEquals(Status.SUCCESS, manager.editCourse(editArgs).getStatus());

        ArrayList<String> badArgs = new ArrayList<>();
        badArgs.add("Course2");
        badArgs.add("WrongCourse");
        badArgs.add("NotANumber");
        assertEquals(Status.FAIL, manager.editCourse(badArgs).getStatus());
    }

    @Test
    void deleteCourseCheck() {
        assertEquals(Status.SUCCESS, manager.deleteCourse("Course1").getStatus());
        assertEquals(Status.FAIL, manager.deleteCourse("NoSuchCourse").getStatus());
    }

    @Test
    void holdsCheck() {
        assertEquals(Status.SUCCESS, manager.placeHoldOnAccount("S0011", "Reason").getStatus());
        assertEquals(Status.SUCCESS, manager.removeHoldOnAccount("S0011", "Reason").getStatus());
        assertEquals(Status.FAIL, manager.placeHoldOnAccount("InvalidID", "Reason").getStatus());
        assertEquals(Status.FAIL, manager.removeHoldOnAccount("InvalidID", "Reason").getStatus());
    }

    @Test
    void listsAndReportCheck() {
        assertEquals(Status.SUCCESS, manager.getAllCourses().getStatus());
        assertEquals(Status.SUCCESS, manager.getAllStudents().getStatus());
        assertEquals(Status.SUCCESS, manager.getReport().getStatus());
        assertNotNull(manager.getReport().getText());
    }
}
