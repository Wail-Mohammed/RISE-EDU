package Shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import app.Shared.Message;
import app.Shared.MessageType;
import app.Shared.Status;
import app.Shared.UserType;

public class MessageTester {

	@Test
    void defaultConstructor() {
        Message message = new Message();
        assertNull(message.getType());
        assertNull(message.getStatus());
        assertEquals("Undefined", message.getText());
        assertNotNull(message.getList());
    }

    @Test
    void loginConstructor() {
        Message message = new Message(MessageType.LOGIN, Status.SUCCESS, UserType.STUDENT, "Login OK");
        assertEquals(MessageType.LOGIN, message.getType());
        assertEquals(Status.SUCCESS, message.getStatus());
        assertEquals(UserType.STUDENT, message.getUserType());
        assertEquals("Login OK", message.getText());
        assertNotNull(message.getList());
    }

    @Test
    void statusConstructor() {
        Message message = new Message(MessageType.LOGOUT, Status.FAIL, "Logout Failed");
        assertEquals(MessageType.LOGOUT, message.getType());
        assertEquals(Status.FAIL, message.getStatus());
        assertEquals("Logout Failed", message.getText());
        assertNotNull(message.getList());
    }

    @Test
    void listConstructor() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Course1");
        Message message = new Message(MessageType.REPORT, Status.SUCCESS, "Report Created", list);
        assertEquals(MessageType.REPORT, message.getType());
        assertEquals(Status.SUCCESS, message.getStatus());
        assertEquals("Report Created", message.getText());
        assertEquals(1, message.getList().size());
        assertEquals("Course1", message.getList().get(0));
    }


}
