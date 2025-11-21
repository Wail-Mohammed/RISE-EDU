package app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Schedule class
 *
 * this class is pretty small. It's basically just:
 *   - a studentId
 *   - a list of Course objects
 *
 * The reason we put it in its own class is because SystemManager
 * returns a Schedule object when the student wants to view their schedule.

 */
public class Schedule implements Serializable {

    // The ID of the student this schedule belongs to.
    private String studentId;

    // A list of the courses the student is taking.
    // Nothing fancy, just an ArrayList under the hood.
    private List<Course> courses;

    /*
     * Constructor
     *
     * When SystemManager generates a schedule, it will already have:
     *   - the studentId
     *   - the list of courses the student is enrolled in
     *
     * So I'm just taking both and storing them.
     */
    public Schedule(String studentId, List<Course> courses) {
        this.studentId = studentId;

        // Just to be safe, if courses is null, I'll make it an empty list.
        // This helps avoid random NullPointerExceptions when calling courses.size().
        this.courses = (courses != null) ? courses : new ArrayList<>();
    }

    // Getter for studentId
    public String getStudentId() {
        return studentId;
    }

    // Setter for studentId
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    // Getter for the list of courses.
    public List<Course> getCourses() {
        return courses;
    }

    // Setter for courses
    public void setCourses(List<Course> courses) {
        this.courses = (courses != null) ? courses : new ArrayList<>();
    }

    /*
     * toString()
     *
     * Helpful when debugging. When you print a Schedule,
     * it'll show which student it belongs to and how many courses they have.
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "studentId='" + studentId + '\'' +
                ", courses=" + courses +
                ", courseCount=" + courses.size() +
                '}';
package main.java.app.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable {

    private ArrayList<String> courses;

    public Schedule() {
        this.courses = new ArrayList<>();
    }

    public void addCourse(String courseName) {
        if (!courses.contains(courseName)) {
            courses.add(courseName);
        }
    }

    public boolean dropCourse(String courseName) {
        return courses.remove(courseName);
    }

    public ArrayList<String> getCourses() {
        return new ArrayList<>(courses);
    }

    @Override
    public String toString() {
        if (courses.isEmpty()) {
            return "Schedule is empty.";
        }
        return String.join("\n", courses);
    }
}
