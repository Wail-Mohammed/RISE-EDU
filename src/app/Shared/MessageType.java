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
    
    // For enrollment list
    LIST_ENROLLMENT
}