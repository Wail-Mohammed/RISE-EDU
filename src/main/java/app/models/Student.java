package app.models;

import java.io.Serializable;

/*
 * Student class
 *
 * This class is for storing basic info about a student.
 */
public class Student implements Serializable {

    // A unique ID for the student
    // Example: "mf1234"
    private String studentId;

    // Username that the student uses to log in to the system.
    private String username;

    // Full name of the student, like "Edison wang".
    private String name;

    // Which university this student belongs to.
    private String universityId;

    // Student email. This is optional 
    private String email;

    /*
     * Full constructor with all the fields.
     *
     * This one is used when we want to create a Student object with all
     * information already filled in, either when loading from a file
     * or when the admin creates a new student record.
     */
    public Student(String studentId, String username, String name, String universityId, String email) {
        this.studentId = studentId;
        this.username = username;
        this.name = name;
        this.universityId = universityId;
        this.email = email;
package main.java.app.models;

import main.java.app.Shared.UserType;
import java.util.ArrayList;

public class Student extends User {

    private String studentId;
    private Schedule schedule;
    private ArrayList<String> holds;

    public Student(String username, String password, String firstName, String lastName, String studentId) {
        super(username, password, firstName, lastName, UserType.STUDENT);
        this.studentId = studentId;
        this.schedule = new Schedule();
        this.holds = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", universityId='" + universityId + '\'' +
                ", email='" + email + '\'' +
                '}';
    public Schedule getSchedule() {
        return schedule;
    }

    public ArrayList<String> getHolds() {
        return holds;
    }

    public void addHold(String holdReason) {
        this.holds.add(holdReason);
    }

    public void removeHold(String holdReason) {
        this.holds.remove(holdReason);
    }

    public boolean hasHolds() {
        return !this.holds.isEmpty();
    }
}
