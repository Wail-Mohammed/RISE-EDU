package Shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.UserType;

public class UserTypeTester {

	@Test
    void testUserTypeAdmin() {
        assertEquals("ADMIN", UserType.ADMIN.name());
    }

	@Test
    void testUserTypeStudent() {
        assertEquals("STUDENT", UserType.STUDENT.name());
    }

	@Test
    void testUserTypeValuesLength() {
        assertEquals(2, UserType.values().length);
    }

}
