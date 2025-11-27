package app.Shared;

public enum MessageType {
	CONNECT,        
    LOGIN,          
    LOGOUT,         
    
    // Student Use Cases
    ENROLL_COURSE,  
    DROP_COURSE,    
    VIEW_WAITLIST,  
    CHECK_PREREQS,  
    VIEW_SCHEDULE,  
    LIST_COURSES,
    VIEW_HOLD,
    
    // Admin Use Cases   
    GET_REPORT,     
    CREATE_COURSE,  
    EDIT_COURSE,
    REMOVE_COURSE,
    VIEW_STUDENTS,
    VIEW_STUDENT_SCHEDULE,
    REPORT,
    ADD_HOLD,
    REMOVE_HOLD, 
    ADD_USER,
    WITHDRAW_STUDENT,
    VIEW_UNIVERSITIES,
    
    // For enrollment list
    LIST_ENROLLMENT
}