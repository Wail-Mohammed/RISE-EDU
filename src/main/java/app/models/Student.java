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
