package app.models;

import java.io.Serializable;

// Course 类，表示系统中的一门课程
public class Course implements Serializable {

    // A unique ID for the course 
    private String courseId;

    // The name of the course
    private String courseName;

    // The time when the course meets.
    // like "Mon/Wed 3:00–4:15" or "Tue 2:00–4:00"
    private String time;

    // The location of the course 
    private String location;

    // Number of credits the course is worth
    private int credits;

    // The instructor's name 
    private String instructor;
    // number of students allowed in this course
    private int maxCapacity;

    // How many students are currently enrolled
    private int currentEnrollment;

    // 构造函数：创建包含所有信息的课程对象
    public Course(String courseId, String courseName, String time, String location,
                  int credits, String instructor, int maxCapacity, int currentEnrollment) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.time = time;
        this.location = location;
        this.credits = credits;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
    }


    // Below are all the getter and setter methods.

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


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }


    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }


    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }


    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    public void setCurrentEnrollment(int currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }


    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", credits=" + credits +
                ", instructor='" + instructor + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", currentEnrollment=" + currentEnrollment +
                '}';
    }
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
