package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.UserType;
import app.models.Admin;
import app.models.Student;

public class UserTester {


    @Test
    void testStudentGetUsername() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertEquals("student1", student.getUsername());
    }

    @Test
    void testStudentGetFirstName() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertEquals("First", student.getFirstName());
    }

    @Test
    void testStudentGetLastName() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertEquals("Last", student.getLastName());
    }

    @Test
    void testStudentGetUserType() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertEquals(UserType.STUDENT, student.getUserType());
    }

    @Test
    void testStudentCheckPasswordCorrect() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertTrue(student.checkPassword("password1"));
    }

    @Test
    void testStudentCheckPasswordWrong() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertFalse(student.checkPassword("wrong"));
    }

    @Test
    void testStudentGetPassword() {
        Student student = new Student("student1", "password1", "First", "Last", "S0011");
        assertEquals("password1", student.getPassword());
    }

    // ---------- Admin tests ----------

    @Test
    void testAdminGetUsername() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertEquals("admin1", admin.getUsername());
    }

    @Test
    void testAdminGetFirstName() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertEquals("Admin", admin.getFirstName());
    }

    @Test
    void testAdminGetLastName() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertEquals("User", admin.getLastName());
    }

    @Test
    void testAdminGetUserType() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertEquals(UserType.ADMIN, admin.getUserType());
    }

    @Test
    void testAdminCheckPasswordCorrect() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertTrue(admin.checkPassword("adminPass"));
    }

    @Test
    void testAdminCheckPasswordWrong() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertFalse(admin.checkPassword("nope"));
    }

    @Test
    void testAdminGetPassword() {
        Admin admin = new Admin("admin1", "adminPass", "Admin", "User", "A1101");
        assertEquals("adminPass", admin.getPassword());
    }
}
