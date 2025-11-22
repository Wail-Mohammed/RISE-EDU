package app.Server;

import java.io.*;
import java.util.ArrayList;
import app.models.*;
import app.Shared.UserType;
import app.models.Admin;
import app.models.Course;
import app.models.Student;
import app.models.University;
import app.models.User;

public class DataManager {

    // File paths
    private static final String FILE_PATH = "data/";
    private static final String USERS_FILE = FILE_PATH + "Users.csv";
    private static final String COURSES_FILE = FILE_PATH + "Courses.csv";
    private static final String ENROLLMENTS_FILE = FILE_PATH + "Enrollments.csv";

    public DataManager() {
        // Ensure data directory exists
        new File(FILE_PATH).mkdirs();
    }

     //This is to be called by Server to load data upon start of server
     
    public University loadDataFromFiles() {
        University university = new University("RISE-EDU");

        System.out.println("DataManager: Loading data...");
        loadUsers(university);
        loadCourses(university);
        loadEnrollments(university);
        System.out.println("DataManager: Data load complete.");

        return university;
    }

    //To be called by server upon shutdown of server
    public void saveDataToFiles(University university) {
        System.out.println("DataManager: Saving data...");
        saveUsers(university);
        saveCourses(university);
        saveEnrollments(university);
        System.out.println("DataManager: Data saved.");
    }

// Main methods to load users, load courses, and enrollments
    
    private void loadUsers(University university) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean header = true;//flag to skip the first header line from being read as user
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Format: type,username,password,firstname,lastname,id(S0001 format for students or A0001 for admins), we are skipping the header line
                if (header) { header = false; continue; } 
                if (parts.length < 6) continue;

                String type = parts[0];
                String username = parts[1];
                String password = parts[2];
                String first = parts[3];
                String last = parts[4];
                String id = parts[5];

                if (type.equals("STUDENT")) {
                    university.addStudent(new Student(username, password, first, last, id));
                } else if (type.equals("ADMIN")) {
                    university.addAdmin(new Admin(username, password, first, last, id));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private void loadCourses(University university) {
        File file = new File(COURSES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Format: id,name,time,location,credits,instructor,capacity,enrolled
                if (parts.length < 8) continue;

                String id = parts[0];
                String name = parts[1];
                String time = parts[2];
                String loc = parts[3];
                int credits = Integer.parseInt(parts[4]);
                String instr = parts[5];
                int cap = Integer.parseInt(parts[6]);
                int enrolled = Integer.parseInt(parts[7]);

                Course c = new Course(id, name, time, loc, credits, instr, cap, enrolled);
                university.addCourse(c);
            }
        } catch (Exception e) {
            System.err.println("Error loading courses: " + e.getMessage());
        }
    }

    private void loadEnrollments(University university) {
        File file = new File(ENROLLMENTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String studentId = parts[0];
                String courseId = parts[1];

                // Find objects
                Student s = null;
                // Note: We need to find by ID, not username. 
                // A helper in University/SystemManager usually handles this, 
                // but for raw loading we iterate:
                for(User u : university.getAllUsers()) {
                    if (u instanceof Student && ((Student)u).getStudentId().equals(studentId)) {
                        s = (Student)u;
                        break;
                    }
                }

                Course c = university.getCourse(courseId);

                // Link them
                if (s != null && c != null) {
                    s.getSchedule().addCourse(c);
                    c.enrollStudent(s.getUsername()); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading enrollments: " + e.getMessage());
        }
    }
    
// to save to files from memory
    
    private void saveUsers(University university) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
        	writer.println("User-Type,Username,Password,FirstName,LastName,ID"); // for our user header
            for (User u : university.getAllUsers()) {
                String type = (u.getUserType() == UserType.STUDENT) ? "STUDENT" : "ADMIN";
                String id = (u.getUserType() == UserType.STUDENT) 
                            ? ((Student)u).getStudentId() 
                            : ((Admin)u).getAdminId();

                // Format: type,username,password,firstname,lastname,id
                writer.printf("%s,%s,%s,%s,%s,%s%n", 
                    type, u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName(), id);
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private void saveCourses(University university) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSES_FILE))) {
            for (Course c : university.getAllCourses()) {
                // Format: id,name,time,location,credits,instructor,capacity,enrolled
                writer.printf("%s,%s,%s,%s,%d,%s,%d,%d%n",
                    c.getCourseId(), c.getTitle(), c.getTime(),c.getLocation(),c.getCredits(),
                     c.getInstructor(), c.getMaxCapacity(), c.getCurrentEnrollment());
            }
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    private void saveEnrollments(University university) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ENROLLMENTS_FILE))) {
            for (User u : university.getAllUsers()) {
                if (u instanceof Student) {
                    Student s = (Student) u;
                    // Iterate through the student's schedule
                    for (Course c : s.getSchedule().getCourses()) {
                        // Format: studentId,courseId
                        writer.printf("%s,%s%n", s.getStudentId(), c.getCourseId());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving enrollments: " + e.getMessage());
        }
    }
}