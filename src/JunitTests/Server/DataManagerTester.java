package Server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Server.DataManager;
import app.models.Admin;
import app.models.Course;
import app.models.Student;
import app.models.University;

public class DataManagerTester {

    @Test
    void saveThenLoad() {
        University university = new University("University1");
        Student student1 = new Student("student1", "password1", "FirstName", "LastName", "S0011");
        Course course1 = new Course("Course1", "Title", "time", "location", 3, "Instructor", 5, 0);
        student1.getSchedule().addCourse(course1);
        university.addStudent(student1);
        university.addAdmin(new Admin("admin1", "adminPass", "Admin", "User", "A1100"));
        university.addCourse(course1);

        DataManager dataManager = new DataManager();
        dataManager.saveDataToFiles(university);
        University loadedUniversity = dataManager.loadDataFromFiles();

        assertTrue(loadedUniversity.getAllUsers().size() > 0);
        assertTrue(loadedUniversity.getAllCourses().size() > 0);
    }
}
