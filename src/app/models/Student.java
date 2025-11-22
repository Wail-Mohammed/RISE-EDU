package app.models;

import java.util.ArrayList;
import app.Shared.UserType;

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

    public String getStudentId() { return studentId; }


    public Schedule getSchedule() {
        return schedule;
    }

    public boolean hasHolds() {
        return !holds.isEmpty();
    }

    public ArrayList<String> getHolds() {
        return holds;
    }

    public void addHold(String reason) {
        holds.add(reason);
    }

    public void removeHold(String reason) {
        holds.remove(reason);
    }
}