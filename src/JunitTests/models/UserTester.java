package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.UserType;
import app.models.Admin;
import app.models.Student;

public class UserTester {

	@Test
    void studentUserFieldsAndPassword() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertEquals("student1", student.getUsername());
        assertEquals("First", student.getFirstName());
        assertEquals("Last", student.getLastName());
        assertEquals(UserType.STUDENT, student.getUserType());
        assertTrue(student.checkPassword("password1"));
        assertFalse(student.checkPassword("wrong"));
        assertEquals("password1", student.getPassword());
    }

    @Test
    void adminUserFieldsAndPassword() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertEquals("admin1", admin.getUsername());
        assertEquals("Admin", admin.getFirstName());
        assertEquals("User", admin.getLastName());
        assertEquals(UserType.ADMIN, admin.getUserType());
        assertTrue(admin.checkPassword("adminPass"));
        assertFalse(admin.checkPassword("nope"));
        assertEquals("adminPass", admin.getPassword());
    }

}
