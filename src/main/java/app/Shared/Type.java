package app.Shared;

public enum Type {
	LOGIN,
	TEXT,
	LOGOUT,
	GET_UNIVERSITIES,
    LIST_COURSES, 
    CREATE_COURSE,
    REMOVE_COURSE,
    ENROLL_STUDENT,
    ENROLL_ADMIN,
    DROP_COURSE, 
    ADD_HOLD,
    CLEAR_HOLD,
    REMOVE_HOLD,
    VIEW_STUDENTS,
    VIEW_SCHEDULE,        // student view their own schedule
    VIEW_STUDENT_SCHEDULE, // admin view a student's schedule
    REPORT,
}