package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Admin;
import app.models.Course;
import app.models.Student;
import app.models.University;

public class UniversityTester {

	@Test
    void testUniversityAddStudent() {
        University university = new University("U");
        Student s = new Student("student1","password123","firstName","lastName","S0010");
        university.addStudent(s);
        assertEquals(s, university.getStudent("student1"));
    }

	@Test
    void testUniversityAddAdmin() {
        University university = new University("U");
        Admin a = new Admin("admin","password","firstN","lastN","A0010");
        university.addAdmin(a);
        assertEquals(a, university.getUser("admin"));
    }

	@Test
    void testUniversityAddCourse() {
        University university = new University("U");
        Course c = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
        university.addCourse(c);
        assertEquals(c, university.getCourse("Course1"));
    }

	@Test
    void testUniversityGetAllCourses() {
        University university = new University("U");
        Course c = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
        university.addCourse(c);
        assertEquals(1, university.getAllCourses().size());
    }

	@Test
    void testUniversityGetAllStudents() {
        University university = new University("U");
        Student s = new Student("student1","password123","firstName","lastName","S0010");
        university.addStudent(s);
        assertEquals(1, university.getAllStudents().size());
    }

	@Test
    void testUniversityGetAllUsers() {
        University university = new University("U");
        Student s = new Student("student1","password123","firstName","lastName","S0010");
        Admin a = new Admin("admin","password","firstN","lastN","A0010");
        university.addStudent(s);
        university.addAdmin(a);
        assertEquals(2, university.getAllUsers().size());
    }

}
