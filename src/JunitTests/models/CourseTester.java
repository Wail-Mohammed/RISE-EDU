package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Course;

public class CourseTester {

	@Test
    void testCourseGetCourseId() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals("Course1", course.getCourseId());
    }

	@Test
    void testCourseGetTitle() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals("Title", course.getTitle());
    }

	@Test
    void testCourseGetTime() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals("time", course.getTime());
    }

	@Test
    void testCourseGetLocation() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals("location", course.getLocation());
    }

	@Test
    void testCourseGetCredits() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals(3, course.getCredits());
    }

	@Test
    void testCourseGetInstructor() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals("instructor", course.getInstructor());
    }

	@Test
    void testCourseGetCurrentEnrollment() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertEquals(0, course.getCurrentEnrollment());
    }

	@Test
    void testCourseEnrollStudent() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        assertTrue(course.enrollStudent("u1"));
        assertEquals(1, course.getCurrentEnrollment());
    }

	@Test
    void testCourseEnrollStudentWhenFull() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        course.enrollStudent("u1");
        assertTrue(course.isFull());
        assertFalse(course.enrollStudent("u2"));
    }

	@Test
    void testCourseDropStudent() {
        Course course = new Course("Course1","Title","time","location",3,"instructor",1,0);
        course.enrollStudent("u1");
        course.dropStudent("u1");
        assertEquals(0, course.getCurrentEnrollment());
    }

	@Test
    void testCourseSetTitle() {
        Course course = new Course("Course2","SecondTitle","SecondTime","SecondLocation",3,"SecondInstructor",10,0);
        course.setTitle("ThirdTitle");
        assertEquals("ThirdTitle", course.getTitle());
    }

	@Test
    void testCourseSetMaxCapacity() {
        Course course = new Course("Course2","SecondTitle","SecondTime","SecondLocation",3,"SecondInstructor",10,0);
        course.setMaxCapacity(5);
        assertEquals(5, course.getMaxCapacity());
    }
}
