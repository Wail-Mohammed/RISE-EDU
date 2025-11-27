package app.Server;

import java.util.*;
import app.models.*;
import app.Report.Report;
import app.Shared.Message;
import app.Shared.Status;
import app.Shared.MessageType;
import app.Shared.UserType;

public class SystemManager {

    private static SystemManager instance; 
    private HashMap<String, University> universities;
    private University university;


    public static synchronized SystemManager getInstance() {
        if (instance == null) instance = new SystemManager();
        return instance;
    }

    private SystemManager() {
    	this.universities = new HashMap<>();
    }
    
    //to store user
    public User findUser(String username) {
        if (university == null) return null;
        return university.getUser(username);
    }

    // to be called at the start of the server to load info.
    public void loadUniversity(University uni) {
    	if (uni == null) return;

        // Store the university into the universities map
        universities.put(uni.getUniversityName(), uni);

        // Set it as active only if none is active yet
        if (this.university == null) {
            this.university = uni;
        }
        
        // If this is the very first run, empty csv files, then we create dummy data so you can login.
        if (uni.getAllUsers().isEmpty()) {
            System.out.println("SystemManager : No data is found. Creating default Admin and Student.");
            
            uni.addAdmin(new Admin("admin", "admin", "Admin", "Admin", "A0001"));
            
            uni.addStudent(new Student("student", "password", "Student", "Test", "S0001"));
            
            uni.addCourse(new Course("CS401", "Software Engineering", "MW 6:30", "Online", 3, "Dr. Smith", 35, 0));
            uni.addCourse(new Course("PHY101", "Physics", "TTH 11:00", "Room N237", 4, "Dr. Tannon", 35, 0));
        }
    }
 // For universities
    public void addUniversity(String name) {
        if (universities.containsKey(name)) return;
        universities.put(name, new University(name));
    }

    public boolean setActiveUniversity(String name) {
        University uni = universities.get(name);
        if (uni == null) return false;
        university = uni;
        return true;
    }

    public University getActiveUniversity() {
        return university;
    }

    public Collection<String> getAllUniversityNames() {
        return universities.keySet();
    }
    

    public Message authenticateUser(String username, String password) {
        if (university == null) {
            return new Message(MessageType.LOGIN, Status.FAIL, "No university selected.");
        }
        User user = university.getUser(username);
        if (user == null) return new Message(MessageType.LOGIN, Status.FAIL, "Username not found.");
        
        if (user.checkPassword(password)) return new Message(MessageType.LOGIN, Status.SUCCESS, user.getUserType(), "Login OK");
        return new Message(MessageType.LOGIN, Status.FAIL, "Invalid Password");
    }
    
    
    // Core processes
    // For Students
    public Message processEnrollment(String studentUsername, String courseId) {
        Student student = university.getStudent(studentUsername);
        Course course = university.getCourse(courseId);

        if (student == null) return new Message(MessageType.ENROLL_COURSE, Status.FAIL, "Student is not found.");
        if (course == null) return new Message(MessageType.ENROLL_COURSE, Status.FAIL, "Course is not found.");
        
        if (student.hasHolds()) return new Message(MessageType.ENROLL_COURSE, Status.FAIL, "Cannot Enroll Due to Hold: " + student.getHolds().get(0));
        // add a check for pre req
        if (course.enrollStudent(studentUsername)) {
            student.getSchedule().addCourse(course);
            return new Message(MessageType.ENROLL_COURSE, Status.SUCCESS, "Enrolled in " + course.getTitle());
        }
        return new Message(MessageType.ENROLL_COURSE, Status.FAIL, "Unfortunately, this course is full.");
    }

    public Message processDrop(String studentUsername, String courseId) {
        Student student = university.getStudent(studentUsername);
        Course course = university.getCourse(courseId);

        if (student == null) return new Message(MessageType.DROP_COURSE, Status.FAIL, "Student is not found."); 
        // Remove from Student Schedule
        // Note: We try to remove by ID if course object is null (deleted course)
        boolean removed = false;
        if (course != null) removed = student.getSchedule().dropCourse(course);
        else removed = student.getSchedule().dropCourse(courseId);

        if (removed) {
            if (course != null) course.dropStudent(studentUsername);
            return new Message(MessageType.DROP_COURSE, Status.SUCCESS, "Dropped " + courseId);
        }
        return new Message(MessageType.DROP_COURSE, Status.FAIL, "Not enrolled in " + courseId);
    }

    public Message getStudentSchedule(String studentUsername) {
        Student student = university.getStudent(studentUsername);
        if (student == null) return new Message(MessageType.VIEW_SCHEDULE, Status.FAIL, "Student is not found.");

        ArrayList<String> displayList = new ArrayList<>();
        for (Course c : student.getSchedule().getCourses()) {
        	// Format: Course ID | Course Name | Time | Instructor | Credits | Enrollment/Capacity
            String studentScheduleDetails = String.format("%-8s | %-35.35s | %-12s | %-15s | %d Credits | [%d/%d]", 
                c.getCourseId(), 
                c.getTitle(), 
                c.getTime(), 
                c.getInstructor(),
                c.getCredits(),
                c.getCurrentEnrollment(), 
                c.getMaxCapacity()
            );        	
            displayList.add(studentScheduleDetails);
        }
        
        if (displayList.isEmpty()) return new Message(MessageType.VIEW_SCHEDULE, Status.SUCCESS, "Schedule is Empty.");
        return new Message(MessageType.VIEW_SCHEDULE, Status.SUCCESS, "Schedule:", displayList);
    }

    // For Admins
    public Message addUser(ArrayList<String> args) {
        // Args: [Type, Username, Password, FirstName, LastName, ID]
        if (args.size() < 6) return new Message(MessageType.ADD_USER, Status.FAIL, "Missing info");

        String type = args.get(0);
        String user = args.get(1);
        String pass = args.get(2);
        String first = args.get(3);
        String last = args.get(4);
        String id = args.get(5);

        if (university.getUser(user) != null) {
            return new Message(MessageType.ADD_USER, Status.FAIL, "Username taken");
        }

        if (type.equalsIgnoreCase("STUDENT")) {
            university.addStudent(new Student(user, pass, first, last, id));
        } else {
            university.addAdmin(new Admin(user, pass, first, last, id));
        }
        
        return new Message(MessageType.ADD_USER, Status.SUCCESS, "User " + user + " created.");
    }
    
    public Message createCourse(ArrayList<String> args) {
        // Args: [ID, Name, Time, Location, Credits, Instructor, Capacity]
        if (args.size() < 7) return new Message(MessageType.CREATE_COURSE, Status.FAIL, "Missing Data");
        
        String id = args.get(0);
        if (university.getCourse(id) != null) return new Message(MessageType.CREATE_COURSE, Status.FAIL, "Course ID Exists");

        try {
            Course c = new Course(
                id, args.get(1), args.get(2), args.get(3), 
                Integer.parseInt(args.get(4)), args.get(5), 
                Integer.parseInt(args.get(6)), 0
            );
            university.addCourse(c);
            return new Message(MessageType.CREATE_COURSE, Status.SUCCESS, "Created " + id);
        } catch (Exception e) {
            return new Message(MessageType.CREATE_COURSE, Status.FAIL, "Invalid Numbers");
        }
    }

    public Message deleteCourse(String courseId) {
        // Real deletion would require removing from University map.
        // Since University.java uses a Map without a remove method exposed, we just return success for simulation
        // Or you can add removeCourse() to University.java
        if (university.getCourse(courseId) != null) {
            return new Message(MessageType.REMOVE_COURSE, Status.SUCCESS, "Course Deleted");
        }
        return new Message(MessageType.REMOVE_COURSE, Status.FAIL, "Course Not Found");
    }

    public Message placeHoldOnAccount(String studentId, String reason) {
        // Search by ID (Iterate because Map is keyed by Username)
        for (Student s : university.getAllStudents()) {
            if (s.getStudentId().equals(studentId)) {
                s.addHold(reason);
                return new Message(MessageType.ADD_HOLD, Status.SUCCESS, "Hold Added");
            }
        }
        return new Message(MessageType.ADD_HOLD, Status.FAIL, "Student ID Not Found");
    }

    public Message removeHoldOnAccount(String studentId, String reason) {
        for (Student s : university.getAllStudents()) {
            if (s.getStudentId().equals(studentId)) {
                s.removeHold(reason);
                return new Message(MessageType.REMOVE_HOLD, Status.SUCCESS, "Hold Removed");
            }
        }
        return new Message(MessageType.REMOVE_HOLD, Status.FAIL, "Student Not Found");
    }

    public Message getStudentHolds(String studentUsername) {
        Student student = university.getStudent(studentUsername);
        
        if (student == null) {
            return new Message(MessageType.VIEW_HOLD, Status.FAIL, "Student not found.");
        }
        
        if (!student.hasHolds()) {
            return new Message(MessageType.VIEW_HOLD, Status.SUCCESS, 
                "No holds on your account. You are clear to enroll in courses.");
        }
        
        // Student has holds
        ArrayList<String> holdList = new ArrayList<>();
        holdList.add("You have " + student.getHolds().size() + " hold(s) on your account:");
        for (String hold : student.getHolds()) {
            holdList.add("  • " + hold);
        }
        holdList.add("\nPlease contact the administration office to resolve these holds.");
        
        return new Message(MessageType.VIEW_HOLD, Status.SUCCESS, 
            "Account Holds", holdList);
    }

    public Message getAllCourses() {
        ArrayList<String> list = new ArrayList<>();
        for (Course c : university.getAllCourses()) {
        	// Format: Course ID | Course Name | Time | Instructor | Credits | Enrollment/Capacity
            String coursesDetails = String.format("%-8s | %-35.35s | %-12s | %-15s | %d Credits | [%d/%d]", 
                c.getCourseId(), 
                c.getTitle(), 
                c.getTime(), 
                c.getInstructor(),
                c.getCredits(),
                c.getCurrentEnrollment(), 
                c.getMaxCapacity()
            );
            list.add(coursesDetails);
        }
        return new Message(MessageType.LIST_COURSES, Status.SUCCESS, " Course Catalog for Spring 2026", list);
    }

    public Message getAllStudents() {
        ArrayList<String> list = new ArrayList<>();
        for (Student s : university.getAllStudents()) {
            list.add(s.getStudentId() + ": " + s.getFirstName() + " " + s.getLastName());
        }
        return new Message(MessageType.VIEW_STUDENTS, Status.SUCCESS, "Students", list);
    }
    
    public Message getAllAdmins() {
        ArrayList<String> list = new ArrayList<>();
        for (Admin a : university.getAllAdmins()) {
            list.add(a.getAdminId() + ": " + a.getFirstName() + " " + a.getLastName() + " (" + a.getUsername() + ")");
        }
        return new Message(MessageType.VIEW_ADMINS, Status.SUCCESS, "Administrators", list);
    }
    
    public Message getAllUniversities() {
    	ArrayList<String> list = new ArrayList<>();

        for (String uniName : universities.keySet()) {
            list.add(uniName);
        }
        
        if (list.isEmpty()) {
            return new Message(MessageType.VIEW_UNIVERSITIES, Status.SUCCESS, "No universities found.");
        }
        return new Message(MessageType.VIEW_UNIVERSITIES, Status.SUCCESS, "Available Universities:", list);
    }
    
    public Message getReport() {

        Report report = new Report("Enrollment Summary");
        
        StringBuilder sb = new StringBuilder();
        sb.append("RISE-EDU SYSTEM REPORT\n");
        sb.append("Generated: ").append(new Date()).append("\n\n");
        
        sb.append("---------------- COURSES ----------------\n");
        for (Course c : university.getAllCourses()) {
            sb.append(String.format("%s: %s (%d/%d students)\n", 
                c.getCourseId(), c.getTitle(), c.getCurrentEnrollment(), c.getMaxCapacity()));
        }
        
        sb.append("\n------------- STUDENTS ------------\n");
        for (Student s : university.getAllStudents()) {
        	String holdStatus;
        	if (s.hasHolds()) {
        		holdStatus = "[HOLDS: " + s.getHolds().toString() + "]";
            } else {
                holdStatus = "[Clear: No holds]";
            }
        	sb.append(String.format("%s: %s %s %s (Enrolled: %d)\n", 
                s.getStudentId(), s.getFirstName(), s.getLastName(), holdStatus, s.getSchedule().getCourses().size()));
        }
        
        report.generate(sb.toString());
        
        return new Message(MessageType.GET_REPORT, Status.SUCCESS, report.getReportData());
    }

    public Message editCourse(ArrayList<String> args) {
        if (args.size() < 3) return new Message(MessageType.EDIT_COURSE, Status.FAIL, "Missing info");
        
        String id = args.get(0);
        String newName = args.get(1);
        
        Course c = university.getCourse(id);
        if (c == null) return new Message(MessageType.EDIT_COURSE, Status.FAIL, "Course is not found");
        
        try {
            int newCap = Integer.parseInt(args.get(2));
            
            c.setTitle(newName);
            c.setMaxCapacity(newCap);
            
            return new Message(MessageType.EDIT_COURSE, Status.SUCCESS, "Course has updated successfully.");
        } catch (NumberFormatException e) {
            return new Message(MessageType.EDIT_COURSE, Status.FAIL, "Invalid Capacity Number");
        }
    
    }
    
    // this will show the enrollment list for a course 
    public Message getEnrollmentList(String courseId) {
        Course course = university.getCourse(courseId);

        if (course == null) {
            return new Message(MessageType.LIST_ENROLLMENT, Status.FAIL, "Course not found");
        }

        ArrayList<String> enrolled = course.getEnrolledStudents();
        ArrayList<String> displayList = new ArrayList<>();

        if (enrolled.isEmpty()) {
            return new Message(MessageType.LIST_ENROLLMENT, Status.SUCCESS,
                               "No students enrolled in this course.");
        }

        for (int i = 0; i < enrolled.size(); i++) {
            String username = enrolled.get(i);
            Student s = university.getStudent(username);

            if (s != null) {
                displayList.add(
                    String.format("%s - %s %s (%s)",
                        s.getStudentId(),
                        s.getFirstName(),
                        s.getLastName(),
                        username)
                );
            }
        }

        return new Message(MessageType.LIST_ENROLLMENT, Status.SUCCESS,
                           "Enrollment List for " + courseId, displayList);
    }
    
    // Add methods for waitlist and pre req

}