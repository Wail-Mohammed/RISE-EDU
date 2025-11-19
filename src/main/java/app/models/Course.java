package main.java.app.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Course implements Serializable {
	
    private String courseId;
    private String title;
    private String subject;
    private String instructor;
    private int capacity;
    
    private ArrayList<String> prerequisites;
    
    private ArrayList<String> enrolledStudents;
    private Queue<String> waitlist;

    public Course(String courseId, String title, String subject, String instructor, int capacity) {
        this.courseId = courseId;
        this.title = title;
        this.subject = subject;
        this.instructor = instructor;
        this.capacity = capacity;
        
        this.prerequisites = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.waitlist = new LinkedList<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }
    
    public ArrayList<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    public Queue<String> getWaitlist() {
        return waitlist;
    }

    public boolean isFull() {
        return getCurrentEnrollment() >= capacity;
    }

    public boolean enrollStudent(String username) {
        if (!isFull()) {
            enrolledStudents.add(username);
            return true;
        }
        return false;
    }

    public void addToWaitlist(String username) {
        if (!waitlist.contains(username)) {
            waitlist.offer(username);
        }
    }

    public String dropStudent(String username) {
        boolean removed = enrolledStudents.remove(username);
        if (!removed) {
            waitlist.remove(username);
            return null;
        }

        if (!waitlist.isEmpty()) {
            String nextStudent = waitlist.poll();
            enrolledStudents.add(nextStudent);
            return nextStudent;
        }
        return null;
    }
    
    public int getWaitlistPosition(String username) {
        int position = 0;
        for (String user : waitlist) {
            position++;
            if (user.equals(username)) {
                return position;
            }
        }
        return -1;
    }
}