package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.models.Course;
import app.models.Schedule;

public class ScheduleTester {

	@Test
    void testScheduleAddCourse() {
        Schedule schedule = new Schedule();
        Course course = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
        schedule.addCourse(course);
        assertEquals(1, schedule.getCourses().size());
    }

	@Test
    void testScheduleDropCourseByObject() {
        Schedule schedule = new Schedule();
        Course course = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
        schedule.addCourse(course);
        assertTrue(schedule.dropCourse(course));
        assertEquals(0, schedule.getCourses().size());
    }

	@Test
    void testScheduleDropCourseById() {
        Schedule schedule = new Schedule();
        Course course = new Course("Course1","Title1","time1","location1",3,"instructor1",10,0);
        schedule.addCourse(course);
        assertTrue(schedule.dropCourse("Course1"));
        assertEquals(0, schedule.getCourses().size());
    }

}
