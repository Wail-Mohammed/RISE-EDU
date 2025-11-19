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
