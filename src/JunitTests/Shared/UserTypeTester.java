package Shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.UserType;

public class UserTypeTester {

	@Test
    void userTypeEnumValues() {
        assertEquals("ADMIN", UserType.ADMIN.name());
        assertEquals("STUDENT", UserType.STUDENT.name());
        assertEquals(2, UserType.values().length);
    }

}
