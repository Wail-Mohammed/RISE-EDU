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
	private static final String FOLDER_PATH = "data/";
//    private static final String FILE_PATH = "data/";
//    private static final String USERS_FILE = FILE_PATH + "Users.csv";
//    private static final String COURSES_FILE = FILE_PATH + "Courses.csv";
//    private static final String ENROLLMENTS_FILE = FILE_PATH + "Enrollments.csv";

    public DataManager() {
        // Ensure data directory exists
        new File(FOLDER_PATH).mkdirs();
    }

     //This is to be called by Server to load data upon start of server
     
    public ArrayList<University> loadAllUniversities(){
    	ArrayList<University> loadedUniversities = new ArrayList<>();
        File rootDir = new File(FOLDER_PATH);
        
        // to find all sub-directories (each folder is a university)
        File[] universityFolders = rootDir.listFiles(File::isDirectory);
        if (universityFolders != null) {
            for (File folder : universityFolders) {
                String uniName = folder.getName();
                System.out.println("DataManager: Found university folder for : " + uniName);
                
                University university = new University(uniName);
                String path = folder.getPath() + "/";
                
                //to load specific data from that folder
                loadUsers(university, path + "Users.csv");
                loadCourses(university, path + "Courses.csv");
                loadEnrollments(university, path + "Enrollments.csv");
                
                loadedUniversities.add(university);
            }
        }
        
        return loadedUniversities;
    }

    //To be called by server upon shutdown of server
    public void saveDataToFiles(University university) {
    	//to create folder of data/university name
        String path = FOLDER_PATH + university.getUniversityName() + "/";
        new File(path).mkdirs();
        
        System.out.println("DataManager: Saving data for " + university.getUniversityName() + "....");
        saveUsers(university, path + "Users.csv");
        saveCourses(university, path + "Courses.csv");
        saveEnrollments(university, path + "Enrollments.csv");
        
        System.out.println("DataManager: Data saved.");
    }

// Main methods to load users, load courses, and enrollments
    
    private void loadUsers(University university, String filePath) {
        File file = new File(filePath);
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
                	Student s = new Student (username, password, first, last, id);
                	// to load a list of holds if any in the users file
                	if (parts.length > 6 && !parts[6].isEmpty()) {
                        String[] holdList = parts[6].split(";");
                        for (String h : holdList) {
                            s.addHold(h);
                        }
                    }
                    university.addStudent(s);
                } else if (type.equals("ADMIN")) {
                    university.addAdmin(new Admin(username, password, first, last, id));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users for " + university.getUniversityName() +  ": " + e.getMessage());
        
    }
    }

    private void loadCourses(University university, String filePath) {
        File file = new File(filePath);
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
            System.err.println("Error loading courses for" + university.getUniversityName() +  ": " + e.getMessage());
        }
    }

    private void loadEnrollments(University university, String filePath) {
        File file = new File(filePath);
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
            System.err.println("Error loading enrollments for " + university.getUniversityName() +  ": "  + e.getMessage());
        }
    }
    
// to save to files from memory
    
    private void saveUsers(University university, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
        	writer.println("User-Type,Username,Password,FirstName,LastName,ID, Holds"); // for our user header
            for (User u : university.getAllUsers()) {
                String type = (u.getUserType() == UserType.STUDENT) ? "STUDENT" : "ADMIN";
                String id = (u.getUserType() == UserType.STUDENT) 
                            ? ((Student)u).getStudentId() 
                            : ((Admin)u).getAdminId();
                // to save holds
                String holds = "";
                if (u instanceof Student) {
                    Student s = (Student) u;
                    if (!s.getHolds().isEmpty()) {
                        holds = String.join(";", s.getHolds());
                    }
                // Format: type,username,password,firstname,lastname,id
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n", 
                    type, s.getUsername(), s.getPassword(), s.getFirstName(), s.getLastName(), s.getStudentId(), holds);
                } else if (u instanceof Admin) {
                    Admin a = (Admin) u;
                    writer.printf("ADMIN,%s,%s,%s,%s,%s,%n", 
                        a.getUsername(), a.getPassword(), a.getFirstName(), a.getLastName(), a.getAdminId());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private void saveCourses(University university, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
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

    private void saveEnrollments(University university, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
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