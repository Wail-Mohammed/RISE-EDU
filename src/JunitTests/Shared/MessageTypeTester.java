package Shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import app.Shared.MessageType;
import app.Shared.Status;
import app.Shared.UserType;

public class MessageTypeTester {

	@Test
    void messageTypeValues() {
        assertNotNull(MessageType.valueOf("LOGIN"));
        assertNotNull(MessageType.valueOf("ENROLL_COURSE"));
        assertNotNull(MessageType.valueOf("ADD_USER"));
    }

    @Test
    void statusValues() {
        assertNotNull(Status.valueOf("SUCCESS"));
        assertNotNull(Status.valueOf("FAIL"));
        assertNotNull(Status.valueOf("NULL"));
    }

    @Test
    void userTypeValues() {
        assertNotNull(UserType.valueOf("ADMIN"));
        assertNotNull(UserType.valueOf("STUDENT"));
    }

}
