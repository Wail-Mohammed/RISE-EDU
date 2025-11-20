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
    }
}
