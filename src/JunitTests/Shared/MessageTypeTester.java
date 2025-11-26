package Shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.MessageType;
import app.Shared.Status;
import app.Shared.UserType;

public class MessageTypeTester {

	@Test
    void testMessageTypeLogin() {
        assertNotNull(MessageType.valueOf("LOGIN"));
    }

	@Test
    void testMessageTypeEnrollCourse() {
        assertNotNull(MessageType.valueOf("ENROLL_COURSE"));
    }

	@Test
    void testMessageTypeAddUser() {
        assertNotNull(MessageType.valueOf("ADD_USER"));
    }

    @Test
    void testStatusSuccess() {
        assertNotNull(Status.valueOf("SUCCESS"));
    }

	@Test
    void testStatusFail() {
        assertNotNull(Status.valueOf("FAIL"));
    }

	@Test
    void testStatusNull() {
        assertNotNull(Status.valueOf("NULL"));
    }

    @Test
    void testUserTypeAdmin() {
        assertNotNull(UserType.valueOf("ADMIN"));
    }

	@Test
    void testUserTypeStudent() {
        assertNotNull(UserType.valueOf("STUDENT"));
    }

}
