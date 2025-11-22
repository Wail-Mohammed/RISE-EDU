package app.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    private String courseId;
    private String title;
    private String time;
    private String location;
    private int credits;
    private String instructor;
    private int maxCapacity;
    
    // Tracks current enrollment
    private ArrayList<String> enrolledStudents; 

    public Course(String courseId, String title, String time, String location, int credits, String instructor, int maxCapacity, int currentEnrollment) {
        this.courseId = courseId;
        this.title = title;
        this.time = time;
        this.location = location;
        this.credits = credits;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
    }



    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getTime() { return time; }
    public String getInstructor() { return instructor; }
    public int getMaxCapacity() { return maxCapacity; }
    public String getLocation() { return location; }
    public int getCredits() { return credits; }
    
    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }

    public boolean isFull() {
        return getCurrentEnrollment() >= maxCapacity;
    }

    public boolean enrollStudent(String username) {
        if (!isFull() && !enrolledStudents.contains(username)) {
            enrolledStudents.add(username);
            return true;
        }
        return false;
    }

    public void dropStudent(String username) {
        enrolledStudents.remove(username);
    }

	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	public void setMaxCapacity(int newCapacity) {
		this.maxCapacity = newCapacity;		
	}
}