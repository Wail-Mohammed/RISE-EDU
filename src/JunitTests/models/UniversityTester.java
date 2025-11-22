package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Admin;
import app.models.Course;
import app.models.Student;
import app.models.University;

public class UniversityTester {

	@Test
    void addAndGet() {
        University university = new University("U");
        Student s = new Student("student1","password123","firstName","lastName","S0010");
        Admin a = new Admin("admin","password","firstN","lastN","A0010");
        Course c = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
        university.addStudent(s);
        university.addAdmin(a);
        university.addCourse(c);
        assertEquals(s, university.getStudent("student1"));
        assertEquals(c, university.getCourse("Course1"));
        assertEquals(a, university.getUser("admin"));
        assertEquals(1, university.getAllCourses().size());
        assertEquals(1, university.getAllStudents().size());
        assertEquals(2, university.getAllUsers().size());
    }

}
