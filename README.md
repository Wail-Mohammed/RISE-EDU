
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

##### Top 10 Requirements:
1. Student user’s login info will be the uppercase letter ‘S’ along with
their first name and last name and last 2 digits of their year of birth,
e.g. firstnamelastnameYY. (3.1.1.2)- [Common Requirements]
2. Administrator user’s login info will be the uppercase letter ‘A’ along
with their first name and last name and last 2 digits of their year of
birth, e.g. firstnamelastnameYY.(3.1.1.3) - [Common Requirements]
3. Students shall be allowed to look up courses by Course names,
Course ID, course time, instructor name, class location, class size
the semester those courses are offered. (3.1.2.2) - [Student
Requirements]
4. The system shall ensure all course prerequisites are satisfied
before successfully enrolling a student user in a course.(3.1.2.3) -
[Student Requirements]
5. The system shall allow student users to add a class or drop a class
during add/drop period. (3.1.2.3) - [Student Requirements]
6. The administrator will be allowed to add courses, delete courses,
and edit courses stored in a course list. (3.1.3.2) - [Administrator
Requirements ]
7. The administrator will be allowed to enroll a student in a course,
drop a student from a course, as well as withdraw a student from
a course only during add/drop period or withdrawal period.
(3.1.3.2)-[Administrator Requirements ]
8. The system should keep track of every enrollment action that
student user makes, such as add, drop, waitlist, withdraw for each
course. (3.1.4.1) - [Reporting Module]
9. The system will save the updated courses info associated with
every students account with timestamps (3.1.4.2) - [Reporting
Module]
10. The system shall allow school administrators to display a student’s
past course enrollments. (3.1.4.4) - [Reporting Module]

##### Limitations:
- Adding add/remove/withdraw periods to enrollment periods.
- We still need to implement input validation/sanitization from the client side.
- Currently, changes to the CSV files are only saved when the server shuts down overwriting existing files for a specific university. If we had more time we would implement immediate or periodic saving to prevent potential data loss or rollback.
- Waitlists are implemented but are not currently promoting the next person in the waitlist position to move up a spot when someone leaves the waitlist.
- User profiles are currently not editable and can’t modify passwords by users. Only admins can set user information and can’t be changed.

##### JUnit Testing:
JUnit tests were written and executed for all primary classes in the system. All major methods passed successfully. The classes covered include:
- SystemManager
- DataManager
- Server
- Client
- Message
- MessageType
- University
- Student
- Admin
- User
- Schedule
- Report
These tests ensured that core system functionalities—such as data loading and saving, client-server communication, message handling, course management, user operations, and reporting—operate correctly and consistently across the application

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

## Server Architecture
Our server uses a simple multi-threaded design so that multiple student/admins can connect at the same time without blocking each other.
  -the main server keeps listening on the port
  -when a new client connects, the server creates a new ClientHandler task.
  -this task is given to a thread pool
  -each client gets its own thread, which handles all communication with that client until they log out.
How the flow works
1. client connects
2. server accepts the socket
3. server hands it to the thread pool
4. a worker thread starts running clientHandler.run()
5. this thread loops:
        read a message
        process it with systemmanager
        send the response
6. when the client logs out, the thread finishes and returns to the pool


README.md           # Project overview, features, setup instructions
```
  
