package main.java.app.DataManager;

import app.models.*;
import java.io.*;
import java.util.*;

public class DataManager {

    // Fields:
    private String userFilePath;
    private String courseFilePath;
    private String enrollmentLogPath;

    // Constructor:
	public SystemManager(String userFilePath, String courseFilePath, String logFilePath) {
	     this.dataManager = new DataManager(userFilePath, courseFilePath, logFilePath);
	     this.universities = new ArrayList<>();
	 }

    // University Loading / Saving
    public List<University> loadUniversities() {
        List<University> universities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length >= 2) {
                    String uniID = p[0].trim();
                    String uniName = p[1].trim();
                    universities.add(new University(uniID, uniName));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading universities: " + e.getMessage());
        }

        return universities;
    }

    public void saveUniversities(List<University> universities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {

            for (University uni : universities) {
                writer.write(uni.getUniversityID() + "," + uni.getUniversityName());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving universities: " + e.getMessage());
        }
    }

    // User Loading / Saving
    public List<User> loadUsers(University university) {

        List<User> users = new ArrayList<>();
        String file = "data/" + university.getUniversityID() + "_users.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length >= 4) {
                    String id = p[0];
                    String username = p[1];
                    String password = p[2];
                    String role = p[3];

                    if (role.equalsIgnoreCase("student")) {
                        users.add(new Student(id, username, password, university));
                    } else if (role.equalsIgnoreCase("admin")) {
                        users.add(new Admin(id, username, password, university));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        return users;
    }

    public void saveUsers(University university, List<User> users) {
        String file = "data/" + university.getUniversityID() + "_users.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (User u : users) {
                writer.write(u.getUserID() + "," + u.getUsername() + "," + u.getPassword() + "," + u.getRole());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Course Loading / Saving
    public List<Course> loadCourses(University university) {

        List<Course> courses = new ArrayList<>();
        String file = "data/" + university.getUniversityID() + "_courses.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length >= 4) {
                    String id = p[0];
                    String name = p[1];
                    int maxSeats = Integer.parseInt(p[2]);
                    int credits = Integer.parseInt(p[3]);

                    courses.add(new Course(id, name, maxSeats, credits));
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }

        return courses;
    }

    public void saveCourses(University university, List<Course> courses) {
        String file = "data/" + university.getUniversityID() + "_courses.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (Course c : courses) {
                writer.write(c.getCourseID() + "," + c.getCourseName() + "," + c.getMaxSeats() + "," + c.getCredits());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    // Enrollment Log
    public void logEnrollmentAction(String studentID, String courseID, String actionType) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(enrollmentLogPath, true))) {

            writer.write(studentID + "," + courseID + "," + actionType + "," + new Date().toString());
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error logging enrollment: " + e.getMessage());
        }
    }

    // Report Export
    public void exportReport(Report report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/report_output.txt"))) {

            writer.write(report.generate());
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error exporting report: " + e.getMessage());
        }
    }
}

