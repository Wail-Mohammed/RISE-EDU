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
