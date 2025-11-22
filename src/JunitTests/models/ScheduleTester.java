package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Course;
import app.models.Schedule;

public class ScheduleTester {

	 @Test
	    void addDropVariants() {
	        Schedule schedule = new Schedule();
	        Course course = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
	        schedule.addCourse(course);
	        assertEquals(1, schedule.getCourses().size());
	        assertTrue(schedule.dropCourse(course));
	        assertEquals(0, schedule.getCourses().size());
	        schedule.addCourse(course);
	        assertTrue(schedule.dropCourse("Course1"));
	        assertEquals(0, schedule.getCourses().size());
	    }

}
