package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Course;

public class CourseTester {

	@Test
    void gettersAndEnrollment() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals("Course1", course.getCourseId());
        assertEquals("Title", course.getTitle());
        assertEquals("time", course.getTime());
        assertEquals("location", course.getLocation());
        assertEquals(3, course.getCredits());
        assertEquals("instructor", course.getInstructor());
        assertEquals(0, course.getCurrentEnrollment());
        assertTrue(course.enrollStudent("u1"));
        assertTrue(course.isFull());
        assertFalse(course.enrollStudent("u2"));
        course.dropStudent("u1");
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    void setters() {
        Course course = new Course("Course2","SecondTitle","SecondTime","SecondLocation",3,"SecondInstructor",10,0);
        course.setTitle("ThirdTitle");
        course.setMaxCapacity(5);
        assertEquals("ThirdTitle", course.getTitle());
        assertEquals(5, course.getMaxCapacity());
    }
}
