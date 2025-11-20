package main.java.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

public class University implements Serializable {

    private String universityName;
    private HashMap<String, Student> students;
    private HashMap<String, Admin> admins;
    private HashMap<String, Course> courseCatalog;

    public University(String universityName) {
        this.universityName = universityName;
        this.students = new HashMap<>();
        this.admins = new HashMap<>();
        this.courseCatalog = new HashMap<>();
    }
    
    public String getUniversityName() {
        return universityName;
    }

    public Student getStudent(String username) {
        return students.get(username);
    }

    public Admin getAdmin(String username) {
        return admins.get(username);
    }
    
    public User getUser(String username) {
        if (students.containsKey(username)) {
            return students.get(username);
        }
        return admins.get(username);
    }
    
    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public void addStudent(Student student) {
        if (student != null && !students.containsKey(student.getUsername())) {
            students.put(student.getUsername(), student);
        }
    }

    public void addAdmin(Admin admin) {
        if (admin != null && !admins.containsKey(admin.getUsername())) {
            admins.put(admin.getUsername(), admin);
        }
    }

    public void addCourse(Course course) {
        if (course != null && !courseCatalog.containsKey(course.getCourseId())) {
            courseCatalog.put(course.getCourseId(), course);
        }
    }

    public Course getCourse(String courseId) {
        return courseCatalog.get(courseId);
    }

    public ArrayList<Course> getAllCourses() {
        return new ArrayList<>(courseCatalog.values());
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.addAll(students.values());
        allUsers.addAll(admins.values());
        return allUsers;
    }
}