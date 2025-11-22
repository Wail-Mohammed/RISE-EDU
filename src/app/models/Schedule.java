package app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Schedule implements Serializable {

    private String studentId;
    private List<Course> courses;

    public Schedule() {
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }
    
    public boolean dropCourse(Course course) {
        return this.courses.remove(course);
    }
    
    public boolean dropCourse(String courseId) {
        return this.courses.removeIf(c -> c.getCourseId().equals(courseId));
    }

    public List<Course> getCourses() {
        return courses;
    }
}