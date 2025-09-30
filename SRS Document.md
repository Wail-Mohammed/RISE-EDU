 
Assignment #2 – RISE-EDU

Software Requirements Specification

Revision History
Date	Revision	Description	Author
09/17/2025	1.0	Initial Version	Wail Mohammed
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 	 	 	 
 

       Table of Contents
1.    PURPOSE....................................................................................................................... 4
1.1.     SCOPE....................................................................................................................... 4
1.2.     DEFINITIONS, ACRONYMS, ABBREVIATIONS...................................................................... 4
1.3.     REFERENCES............................................................................................................... 4
1.4.     OVERVIEW................................................................................................................. 4
2.    OVERALL DESCRIPTION.............................................................................................. 5
2.1.     PRODUCT PERSPECTIVE................................................................................................. 5
2.2.     PRODUCT ARCHITECTURE.............................................................................................. 5
2.3.     PRODUCT FUNCTIONALITY/FEATURES.............................................................................. 5
2.4.     CONSTRAINTS............................................................................................................. 5
2.5.     ASSUMPTIONS AND DEPENDENCIES................................................................................. 5
3.    SPECIFIC REQUIREMENTS........................................................................................... 6
3.1.     FUNCTIONAL REQUIREMENTS......................................................................................... 6
3.2.     EXTERNAL INTERFACE REQUIREMENTS............................................................................ 6
3.3.     INTERNAL INTERFACE REQUIREMENTS............................................................................. 7
4.    NON-FUNCTIONAL REQUIREMENTS............................................................................ 8
4.1.     SECURITY AND PRIVACY REQUIREMENTS.......................................................................... 8
4.2.     ENVIRONMENTAL REQUIREMENTS................................................................................... 8
4.3.     Performance Requirements............................................................................................ 8
 
 
1.           Purpose
This document outlines the requirements for the College Course Enrollment System Project.
1.1.         Scope
This document will catalog the user, system, and hardware requirements for the CCES (College Course Enrollment) system. It will not, however, document how these requirements will be implemented.
1.2.         Definitions, Acronyms, Abbreviations
CCES: College Course Enrollment System


1.3.         References
Use Case Specification Document
UML Use Case Diagrams Document
Class Diagrams 
Sequence Diagrams
1.4.         Overview
The CCES (College Course Enrollment System) allows for the creation of college course schedules
 by administrators and allows students to enroll in these courses. The system will support class sizes, 
waiting lists, prerequisites, and reports. This system supports a network of universities, students, and 
Administrators. This is a Java application with a GUI that operates over TCP/IP. This system requires 
a server application and a client application. There is no web or HTML component.


2.           Overall Description
2.1.         Product Perspective
The CCES system is a platform designed for university students and administrators. Administrators can control the number of courses, class size, waiting list size, prerequisites lists, and issue reports. Students can enroll in courses, drop courses, and view schedules, 

2.2.         Product Architecture
The system will be organized into three major modules: the User module, the Course Scheduler module, and the Reporting (log) module.



2.3.         Product Functionality/Features
The high-level features of the system are as follows (see section 3 of this document for more detailed requirements that address these features):
2.4.         Constraints - Yesenia
List appropriate constraints.
Constraint example: Since users may use any web browser to access the system, no browser-specific code is to be used in the system. 
2.5.         Assumptions and Dependencies—shichang wang  and Wail
List appropriate assumptions
Shichang Wang:
2.5.7 students and administrators will not share their login information. 
2.5.8 Students will be responsible for checking their enrollment status(if the student is on the waitlist for the course).
2.5.9 Student accounts created based on correct personal information. 
2.5.10 Students will be notified of the enrollment start date.
2.5.11 Students will complete their enrollment within the deadlines.
2.5.12 The reporting module will only generate reports based on data in the system.
 
3.           Specific Requirements
3.1.         Functional Requirements
3.1.1.     Common Requirements:
Provide requirements that apply to all components as appropriate. 
Example:
3.1.1.1 Users should be allowed to log in using their issued id and pin, both of which are alphanumeric strings between 6 and 20 characters in length. 
3.1.1.2 The system should provide HTML-based help pages on each screen that describe the purpose of each function within the system. 
3.1.2.     _____ Module Requirements:-Emmanuel
Provide module specific requirements as appropriate. 
Example:
3.1.2.1 Users should be allowed to log in using their issued id and pin, both of which are alphanumeric strings between 6 and 20 characters in length. 
3.1.3.     _____ Module Requirements: - Yesenia
Provide module specific requirements as appropriate.             
Example:
3.1.2.1 Users should be allowed to log in using their issued id and pin, both of which are alphanumeric strings between 6 and 20 characters in length. 
3.1.4.     __Course Planner/Reporting___ Module Requirements: -Shichang Wang(Reporting (log))
3.1.4.1
 The system should keep track of every enrollment action that students make, such as add, drop, or waitlist for each course. It needs to save the student ID, the course ID, and also the timestaps. This is important because otherwise there is no way to trace back what the student has done before.
3.1.4.2
 Admins should be able to make reports that show how many people are in a class and also how many are on the waitlist. These numbers are useful so that admins can see which classes are full and maybe open another section. Without this feature, they won’t know how to adjust class capacity.
3.1.4.3
 The system will let administrators check a student’s past enrollment history. For example, what courses the student added before or dropped before. This way if a student has questions or a problem, the admin can look it up and confirm.
3.1.4.4
 After a student makes a change, the reporting module should update almost synchronously( avoid commercial advertisement ) so students can see the result. For example, if a student adds a course, it should appear in their schedule when they switch the pages. The idea is that the update must be shown right after the action is completed.
3.1.4.5
 The system also needs to display error messages whenever students make invalid action. Like if a student tries to register for a class but didn’t meet the prerequisites, it should display “you need finished… to register this course”. 
3.1.4.6
 The system will generate official documents such as transcripts or enrollment reports. These should come out as read-only files. Students can only view and print the document, while only authorized faculty or admins can edit. This makes sure the reports are secure and reliable.


 
3.2.         External Interface Requirements-Wail
Provide module specific requirements as appropriate. 
Example:
3.2.1 The system must provide an interface to the University billing system administered by the Bursar’s office so that students can be automatically billed for the courses in which they have enrolled. The interface is to be in a comma-separated text file containing the following fields: student id, course id, term id, action. Where “action” is whether the student has added or dropped the course. The file will be exported nightly and will contain new transactions only. 
3.3.         Internal Interface Requirements - Wail
Provide module specific requirements as appropriate. 
Example:
3.3.1 The system must process a data-feed from the grading system such that student grades are stored along with the historical student course enrolments. Data feed will be in the form of a comma-separated interface file that is exported from the grading system nightly.
3.3.2 The system must process a data-feed from the University billing system that contains new student records. The feed will be in the form of a comma-separated text file and will be exported from the billing system nightly with new student records. The fields included in the file are student name, student id, and student pin number.  

4.           Non-Functional Requirements - ALL
4.1.         Security and Privacy Requirements - Yesenia
Example:
4.1.1 The System must encrypt data being transmitted over the Internet. 
4.2.         Environmental Requirements- shichang wang
4.2.1 The system shall run on the university’s existing Linux-based server infrastructure.
4.2.2 The system shall operate properly with the university’s existing network and power infrastructure.
4.2.3 The system shall not require high-performance machines; any standard desktop used by students or administrators will be sufficient to run the client.
4.2.4 The system shall be deployable on existing university servers without requiring hardware upgrades.
4.2.5 The system shall remain compatible with future minor upgrades to the university’s operating systems.
4.3.         Performance Requirements - Emmanuel
Example:
4.3.1 System must render all UI pages in no more than 9 seconds for dynamic pages. Static pages (HTML-only) must be rendered in less than 3 seconds. 
 
 4.    Use Case Specifications Document

Use Case ID: UC01
Use Case Name: Manage User Login 
Relevant Requirements:
·       Requirements Document ID: Security and Privacy Document 4.1
Primary Actor:
·       Student
·       School Supervisor
Pre-conditions:
·       The server is online and user client is connected to server. 
·       The user must have an existing account and is active.
·       The user credentials are stored in a data file.
·       The client has access to user list.
Post-conditions:
·       User credentials are checked.
·       Session is created if credentials are valid, otherwise access is denied. 
·       Appropriate user role and access is given to students or supervisors.
Basic Flow or Main Scenario:
1)     The user navigates to login page.
2)     The user is prompted to enter username and password .
3)     The user enters username and password then hits submit.
4)     The system verifies the credentials.
5)     The system assigns role (supervisor or student) based on account type.
6)     If this is the first time the user logs in or if password reset needed:
a)     The system redirects to account management page.
b)     The user must either change their default username and/or password per system security requirements.
7)     The system validates the changes and updates the user records.
8)     The system creates an authenticated session.
9)     The system redirects the user to the main dashboard.
Extensions or Alternate Flows:
·       Invalid Credentials at log in:
If the user enters an invalid current credentials, per system’s security requirements, the system displays an error message, “Invalid Username or password”, and asks user to retry.
·       Invalid Credentials at update page:
If the user enters an invalid username or password per system’s security requirements, the system displays an error message “Invalid Username or password” and asks user to retry.
Exceptions:
·       If the authentication system or server is unavailable, an error message is displayed to the user.
Related Use Cases:
·       UC04 Course Planner and Enrollment.
-------------------------------------------------------
Use Case ID: UC10
Use Case Name: Enrollment & waitlist Report
Relevant Requirements:
Course enrollment data
Primary Actor:
Supervisor(Admin)
Pre-conditions:
The Supervisor is already logged in to the system.
Enrollment data exists. 
Post-conditions:
The report is displayed on the screen.
The supervisor can export the report.
Basic Flow or Main Scenario:
1)      	The supervisor logged in to the system and went to the report module. 
2) 	The target course has been selected.
3)     The system gathers information.
4)     Report display on the screen.
5) 	The supervisor can export the report.

Extensions or Alternate Flows:
If the supervisor exports the report, the report will be in pdf format.
If the supervisor didn’t choose any course, the system will prompt the supervisor to choose at least one course.
If there is no student in the waiting list, the report still shows a waiting list with 0.
Exceptions:
If the system can’t access the enrollment data, the system will display the error message and record the error.
Related Use Cases:
UC02 Manage User Login 
UC03 Course Enrollment
UC04 Course Course Drop
UC05 Course Waitlist
----------------------------------------------------------------------------
Use Case ID: UC11
Use Case Name: Real Time Report Update
Relevant Requirements:
The system should update immediately after students make an action.
Primary Actor:
Report module
Pre-conditions:
The server is online and the system is running normally.
At least one student made an action.
Post-conditions:
Report module update immediately to display the latest status.
Student’s schedule updates immediately. 
Basic Flow or Main Scenario:
1)        the student logging in to the system.
2) 	Students make an enrollment action(add, drop and waiting list).
3)     System record the transaction(student ID, Course number and timestamp)
4) 	The reporting module updates the enrollment and waitlist status.
Extensions or Alternate Flows:
If more	than one enrollment action made at same time, the system will group them and update together.
If the update takes a long time, we will display a “loading…” message until the update is finished.
Exceptions:
If the Report module can’t reach enrollment data, the page keeps the last known information and displays an error message ”System is temporarily unavailable”. 
Related Use Cases:
UC02 User Login
UC03 Course Enrollment
UC04 Course Drop
UC05 Course Waitlist
UC10 Enrollment & Waitlist Report


 
 
 

 

 
 

