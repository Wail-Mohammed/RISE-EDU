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
    
    // using this for the Ids of prereq courses
    private ArrayList<String> prerequisites = new ArrayList<>();
    
    // 
    private ArrayList<String> waitlist = new ArrayList<>();

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
    
    public ArrayList<String> getEnrolledStudents() {
        if (enrolledStudents == null) {
            enrolledStudents = new ArrayList<>();
        }
        return enrolledStudents;
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
    
    public ArrayList<String> getPrerequisites() {
    	return prerequisites;
    }
    
    public void addPrerequisite(String courseId) {
    	prerequisites.add(courseId);
    }
    
    public int waitlistPlaced(String usrname) {
    	waitlist.add(usrname);
    	return waitlist.size();
    }
    
    public ArrayList<String> getWaitlist() {
    	return waitlist;
    }

	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	public void setMaxCapacity(int newCapacity) {
		this.maxCapacity = newCapacity;		
	}
}