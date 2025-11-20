package SystemManager;

import java.util.*;
import java.io.*;
import app.models.Student;
import app.models.Course;
import app.models.Schedule;

/*
 * SystemManager
 *
 * we trying to make this class act like the brains on the server side.
 * Basically everything the server needs to know about students/courses/enrollment
 * should be inside here so the Server.java doesn't get super messy.
 *
 */
public class SystemManager {

	// These four dates are part of the design doc requirements.
	// To be honest... we are not using them yet, but I’ll keep them here
	// so our SystemManager doesn't look incomplete.
	private Date addDropPeriodStart;
	private Date addDropPeriodEnd;
	private Date withdrawalPeriodStart;
	private Date withdrawalPeriodEnd;

	// Just storing everything in memory using simple lists/maps.
	private List<Course> courses;
	private List<Student> students;

	// Map: studentId -> list of courseIds
	// This is basically our enrollment table.
	private Map<String, List<String>> enrollments;

	// Map: studentId -> holdReason
	// This stores holds on student accounts (prevents course enrollment)
	private Map<String, String> holds;


	/*
	 * Constructor
	 *
	 * When SystemManager is created, I'm just setting up empty lists/maps.
	 * I don’t load files here because sometimes the server wants to create
	 * SystemManager first and load later.
	 */
	public SystemManager() {
		this.courses = new ArrayList<>();
		this.students = new ArrayList<>();
		this.enrollments = new HashMap<>();
		this.holds = new HashMap<>();
	}

	/*
	 * intializeSystem()
	 *
	 * So this is the method the server should call after creating SystemManager.
	 *
	 * If a file is missing… I don’t really want to crash the whole program,
	 * so I just let it load as empty. It's still workable.
	 */
	public void intializeSystem() {
		loadCourses();
		loadStudents();
		loadEnrollments();
	}



// Create a new course and save it into the CSV file.
public boolean createCourse(String courseId, String courseName, String time,
                            String location, int credits, String instructor,
                            int maxCapacity) {

    // First thing: check if this courseId already exists.
    // Because courseId is the key for courses,
    // and if we somehow create a same courseId, it will crash the program.
    // findCourse() returns the course if it exists, otherwise null.
    if (findCourse(courseId) != null) {
        // If we found something, it means someone already created this course.
        // So, we just say no (return false).
        // The GUI can show "course already exists" or whatever message it wants.
        return false;
    }

    // reached this point, the ID is safe to use.
    // Now we actually create the course object.
    // currentEnrollment is always 0 for a brand new course,
    Course newCourse = new Course(courseId, courseName, time, location,
                                  credits, instructor, maxCapacity, 0);

    // Add the new course to our list.

    courses.add(newCourse);

    // save changes to the CSV file.
    // If we forget this line, the new course only exists in memory,
    // and once the program closes, it's gone.
    // So yeah, this makes sure the course is actually stored permanently.
    saveCourses();

    // If we got here, everything worked.
    return true;
}

// Edit an existing course and save it into the CSV file.
public boolean editCourse(String courseId, String courseName, String time,
                          String location, int credits, String instructor,
                          int maxCapacity, int currentEnrollment) {

    // First, we need try to find the course with this ID.
    // Because if the course doesn't exist, then… we can't edit 
    
    Course course = findCourse(courseId);
    if (course == null) {
        // If we hit this return, means ID was typed wrong
        // or the course was never created in the first place.
        return false;
    }

    // reach here the course actually exists.
    // Now we update all the fields except courseId.
    // The reason we don't touch courseId is because it's basically the identity 
    // of the course. Changing it would break enrollment relationships

    course.setCourseName(courseName);
    course.setTime(time);
    course.setLocation(location);
    course.setCredits(credits);
    course.setInstructor(instructor);
    course.setMaxCapacity(maxCapacity);


    course.setCurrentEnrollment(currentEnrollment);

    // Same as createCourse(): if we forget to save, all edits only exist in memory.
    // Once the program restarts, everything reverts back, and all our work disappears.
    // So yeah — save to CSV to make it permanent.
    saveCourses();

    // If we got this far, everything updated successfully.
    return true;
}


	public boolean deleteCourse() { return true; }

	public boolean processEnrollment() { return true; }

	public boolean processDrop() { return true; }

	public boolean processWithdrawal() { return true; }

	// Place a hold on a student account with a reason
	public boolean placeHoldOnAccount(String studentId, String reason) {
		if (studentId == null || studentId.trim().isEmpty()) {
			return false;
		}
		if (reason == null || reason.trim().isEmpty()) {
			reason = "No reason provided";
		}
		holds.put(studentId.trim(), reason.trim());
		return true;
	}

	// Remove a hold from a student account
	public boolean removeHoldOnAccount(String studentId) {
		if (studentId == null || studentId.trim().isEmpty()) {
			return false;
		}
		return holds.remove(studentId.trim()) != null;
	}

	// Check if a student has a hold
	public boolean hasHold(String studentId) {
		if (studentId == null || studentId.trim().isEmpty()) {
			return false;
		}
		return holds.containsKey(studentId.trim());
	}

	// Get the hold reason for a student
	public String getHoldReason(String studentId) {
		if (studentId == null || studentId.trim().isEmpty()) {
			return null;
		}
		return holds.get(studentId.trim());
	}

	// Get all students with holds
	public Map<String, String> getAllHolds() {
		return new HashMap<>(holds);
	}



	
	private void loadCourses() {
		String courseFile = "data/Course.csv";

		try (BufferedReader reader = new BufferedReader(new FileReader(courseFile))) {
			reader.readLine(); // Skip header row

			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");

				String courseId = parts[0].trim();
				String courseName = parts[1].trim();
				String time = parts[2].trim();
				String location = parts[3].trim();
				int credits = Integer.parseInt(parts[4].trim());
				String instructor = parts[5].trim();
				int maxCapacity = Integer.parseInt(parts[6].trim());
				int currentEnroll = Integer.parseInt(parts[7].trim());

				courses.add(new Course(courseId, courseName, time, location,
						credits, instructor, maxCapacity, currentEnroll));
			}
		} catch (Exception e) {
			System.err.println("Error loading courses from " + courseFile + ": " + e.getMessage());
		}
	}

	// 保存课程到 CSV 文件
	private void saveCourses() {
		String courseFile = "data/Course.csv";

		try (PrintWriter writer = new PrintWriter(new FileWriter(courseFile))) {
			// 写入 header
			writer.println("courseId,courseName,time,location,credits,instructor,maxCapacity,currentEnrollment");

			// 写入所有课程
			for (Course c : courses) {
				writer.println(String.format("%s,%s,%s,%s,%d,%s,%d,%d",
					c.getCourseId(), c.getCourseName(), c.getTime(), c.getLocation(),
					c.getCredits(), c.getInstructor(), c.getMaxCapacity(),
					c.getCurrentEnrollment()));
			}
		} catch (IOException e) {
			System.err.println("Error saving courses to " + courseFile + ": " + e.getMessage());
		}
	}

	// 从 data/User.csv 加载学生数据（跳过第一行header）
	private void loadStudents() {
		try (BufferedReader reader = new BufferedReader(new FileReader("data/User.csv"))) {
			reader.readLine(); // Skip header row

			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");

				students.add(new Student(
					parts[0].trim(),
					parts[1].trim(),
					parts[2].trim(),
					parts[3].trim(),
					parts[4].trim()
				));
			}
		} catch (Exception e) {
			System.err.println("Error loading students: " + e.getMessage());
		}
	}

	
	private void loadEnrollments() {
		String filePath = "data/Enrollment.csv";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			reader.readLine(); // Skip header row

			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");

				String studentId = parts[0].trim();
				String courseId = parts[1].trim();

				enrollments.putIfAbsent(studentId, new ArrayList<>());
				enrollments.get(studentId).add(courseId);
			}
		} catch (IOException e) {
			// File doesn't exist or is empty - that's okay, enrollments will be empty
			// Students can enroll in courses later through the system
		}
	}




	// 根据 studentId 查找学生
	public Student findStudent(String studentId) {
		for (Student s : students) {
			if (s.getStudentId().equals(studentId)) {
				return s;
			}
		}
		return null;
	}

	/*
	 * findStudentByUsername()
	 *
	 * Used during login or anytime we only know the username.
	 */
	public Student findStudentByUsername(String username) {
		for (Student s : students) {
			if (s.getUsername().equals(username)) {
				return s;
			}
		}
		return null;
	}

	/*
	 * findCourse()
	 *
	 * Again… just a simple loop search.
	 * Could be optimized later with a Map if needed.
	 */
	public Course findCourse(String courseId) {
		for (Course c : courses) {
			if (c.getCourseId().equals(courseId)) {
				return c;
			}
		}
		return null;
	}

	/*
	 * getStudentEnrollments()
	 *
	 * This grabs the list of courseIds for a student,
	 * then turns each one into an actual Course object.
	 */
	public List<Course> getStudentEnrollments(String studentId) {

		List<Course> result = new ArrayList<>();
		List<String> ids = enrollments.get(studentId);

		if (ids == null || ids.isEmpty()) {
			return result;
		}

		for (String id : ids) {
			Course c = findCourse(id);
			if (c != null) result.add(c);
		}

		return result;
	}

	public List<Course> getAllCourses() {
		return courses;
	}

	public List<Student> getAllStudents() {
		return students;
	}


	/*
	 * getStudentSchedule()
	 *
	 * This is the “back-end” part of viewSchedule.
	 * It just returns a Schedule object.
	 * Server will later convert this into string lines to send over the socket.
	 */
	public Schedule getStudentSchedule(String studentId) {
		List<Course> list = getStudentEnrollments(studentId);
		return new Schedule(studentId, list);
	}
}
