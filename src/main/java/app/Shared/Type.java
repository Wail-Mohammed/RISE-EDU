package app.Shared;

public enum Type {
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
    ADD_HOLD,
    REMOVE_HOLD
}