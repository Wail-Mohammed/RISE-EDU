package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Course;
import app.models.Student;

public class StudentTester {
	
	  @Test
	    void holdsAndSchedule() {
	        Student student = new Student("student2","password2","first","last","S0100");
	        assertEquals("S0100", student.getStudentId());
	        student.addHold("h");
	        assertTrue(student.hasHolds());
	        assertEquals(1, student.getHolds().size());
	        student.removeHold("h");
	        assertFalse(student.hasHolds());
	        Course c = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
	        student.getSchedule().addCourse(c);
	        assertEquals(1, student.getSchedule().getCourses().size());
	        assertTrue(student.getSchedule().dropCourse("Course1"));
	        assertEquals(0, student.getSchedule().getCourses().size());
	    }
}
