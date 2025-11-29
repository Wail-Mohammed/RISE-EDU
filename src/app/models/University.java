package app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import app.Shared.UserType;

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

    public Course getCourse(String id) {
        return courseCatalog.get(id);
    }

    public Student getStudent(String username) {
        return students.get(username);
    }

    public User getUser(String username) {
        if (students.containsKey(username)) {
            return students.get(username);
        }
        if (admins.containsKey(username)) {
            return admins.get(username);
        }
        return null;
    }
    
    public ArrayList<Course> getAllCourses() {
        return new ArrayList<>(courseCatalog.values());
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }
    
    public Collection<Admin> getAllAdmins() {
        return admins.values();
    }
    
    public ArrayList<User> getAllUsers() {
        ArrayList<User> all = new ArrayList<>(students.values());
        all.addAll(admins.values());
        return all;
    }


    public void addStudent(Student student) {
        if (student != null) students.put(student.getUsername(), student);
    }

    public void addAdmin(Admin admin) {
        if (admin != null) admins.put(admin.getUsername(), admin);
    }

    public void addCourse(Course course) {
        if (course != null) courseCatalog.put(course.getCourseId(), course);
    }
    
    public boolean removeCourse(String course) {
        if (courseCatalog.containsKey(course)) {
            courseCatalog.remove(course);
            return true;
        }
        return false;
    }

}