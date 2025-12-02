
# RiseEdu - College Enrollment System

### Overview

Rise-Edu is a Java-based client-server program that helps students manage their courses. Students can enroll in classes, drop courses, withdraw, join waitlists, and check their schedules. Administrators can add users (students or admins), create and edit courses, withdraw students, manage holds, view reports, and see lists of system admins. At the user login point, user can create university, which in turn the system manager will create defualt users for admin and student, as well as CSV files for Users, Courses, and Enrollments. 

The system runs on a multithreaded server that communicates with multiple clients over TCP/IP. Each client connects from a separate computer, and all communication is done using serialized message objects.

The client has a Java Swing GUI, making it easy for students and admins to interact with the system. Requests go to the server, which updates the data and logs activity, then sends results back to clients in real time.

The data is saved in csv files within the project, DataManger loads in the data and saves the data once the server closes.
### Main Features

##### For Students:
- Enroll in Courses – Sign up for available classes at your university.
- Drop Courses – Remove courses from your schedule if plans change.
- Withdraw from Courses – Officially withdraw from a course if needed.
- Join Waitlists – Get in line for full courses and be notified if a spot opens.
- View Schedule – Check your current class schedule anytime.
- Check Prerequisites – See if you meet the requirements for a course.

##### For Administrators:
- Add Users – Create student or admin accounts.
- Create and Edit Courses – Set up new courses or change course info like name, ID, and capacity.
- Withdraw Students – Remove students from courses if needed.
- Manage Holds – Add or remove holds on student accounts.
- View Reports – Generate reports on courses, enrollments, and universities.
- View System Users – See lists of students, admins, and enrollment information.

##### University:
- Handles multiple universities
- Courses and students are linked to specific universities
- Administrators can manage courses per university

##### Limitations:
- Adding add/remove/withdraw periods to enrollment periods.
- We still need to implement input validation/sanitization from the client side.
- Currently, changes to the CSV files are only saved when the server shuts down overwriting existing files for a specific university. If we had more time we would implement immediate or periodic saving to prevent potential data loss or rollback.
- Waitlists are implemented but are not currently promoting the next person in the waitlist position to move up a spot when someone leaves the waitlist.
- User profiles are currently not editable and can’t modify passwords by users. Only admins can set user information and can’t be changed.

### File Overview:
```
/app
  /client           # Contains all client-side code
    Client.java
  /client.GUImanager  # Contains all the GUI's
    AdminPageGUI.java
    GUI.java
    LoginPage.java
    StudentPageGUI.java
  /models          # Contains core classes for Users, Courses, Universities
    Admin.java
    Course.java
    Schedule.java
    Student.java
    University.java
    User.java
  /report          # Contains the code for the report
    Report.java
  /server           # Contains server-side code
    Server.java
    DataManager.java
    SystemManager.java
  /Shared            # Contains helper client-side code
    Message.java
    MessageType.java
    Status.java
    UserType.java
    
/JunitTests # Contains all the test for our classes and methods
  /app
    AllTests.java
  /Client
    ClientTester.java
  /models
    AdminTester.java
    CourseTester.java
    ScheduleTester.java
    StudentTester.java
    UniversityTester.java
    UserTester.java
  /Reports
    ReportTester.java
  /Server
    ServerTester.java
    DataManagerTester.java
    SystemManagerTester.java
    SystemManagerPrereqWaitlistTest.java
  /Shared
    MessageTester.java
    MessageTypeTester.java
    StatusTester.java
    UserTypeTester.java

/data # Data files for the project
  Courses.csv
  Enrollements.csv
  Users.csv

/Documents
  #Contains all design document revisions and final versions

/Gnatt charts

/Meeting Minutes
  #Contains all meeting minutes

README.md           # Project overview, features, setup instructions
```
  
