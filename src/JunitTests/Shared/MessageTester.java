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
    void testMessageDefaultConstructorType() {
        Message message = new Message();
        assertNull(message.getType());
    }

	@Test
    void testMessageDefaultConstructorStatus() {
        Message message = new Message();
        assertNull(message.getStatus());
    }

	@Test
    void testMessageDefaultConstructorText() {
        Message message = new Message();
        assertEquals("Undefined", message.getText());
    }

	@Test
    void testMessageDefaultConstructorList() {
        Message message = new Message();
        assertNotNull(message.getList());
    }

    @Test
    void testMessageLoginConstructorType() {
        Message message = new Message(MessageType.LOGIN, Status.SUCCESS, UserType.STUDENT, "Login OK");
        assertEquals(MessageType.LOGIN, message.getType());
    }

	@Test
    void testMessageLoginConstructorStatus() {
        Message message = new Message(MessageType.LOGIN, Status.SUCCESS, UserType.STUDENT, "Login OK");
        assertEquals(Status.SUCCESS, message.getStatus());
    }

	@Test
    void testMessageLoginConstructorUserType() {
        Message message = new Message(MessageType.LOGIN, Status.SUCCESS, UserType.STUDENT, "Login OK");
        assertEquals(UserType.STUDENT, message.getUserType());
    }

	@Test
    void testMessageLoginConstructorText() {
        Message message = new Message(MessageType.LOGIN, Status.SUCCESS, UserType.STUDENT, "Login OK");
        assertEquals("Login OK", message.getText());
    }

	@Test
    void testMessageLoginConstructorList() {
        Message message = new Message(MessageType.LOGIN, Status.SUCCESS, UserType.STUDENT, "Login OK");
        assertNotNull(message.getList());
    }

    @Test
    void testMessageStatusConstructorType() {
        Message message = new Message(MessageType.LOGOUT, Status.FAIL, "Logout Failed");
        assertEquals(MessageType.LOGOUT, message.getType());
    }

	@Test
    void testMessageStatusConstructorStatus() {
        Message message = new Message(MessageType.LOGOUT, Status.FAIL, "Logout Failed");
        assertEquals(Status.FAIL, message.getStatus());
    }

	@Test
    void testMessageStatusConstructorText() {
        Message message = new Message(MessageType.LOGOUT, Status.FAIL, "Logout Failed");
        assertEquals("Logout Failed", message.getText());
    }

	@Test
    void testMessageStatusConstructorList() {
        Message message = new Message(MessageType.LOGOUT, Status.FAIL, "Logout Failed");
        assertNotNull(message.getList());
    }

    @Test
    void testMessageListConstructorType() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Course1");
        Message message = new Message(MessageType.REPORT, Status.SUCCESS, "Report Created", list);
        assertEquals(MessageType.REPORT, message.getType());
    }

	@Test
    void testMessageListConstructorStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Course1");
        Message message = new Message(MessageType.REPORT, Status.SUCCESS, "Report Created", list);
        assertEquals(Status.SUCCESS, message.getStatus());
    }

	@Test
    void testMessageListConstructorText() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Course1");
        Message message = new Message(MessageType.REPORT, Status.SUCCESS, "Report Created", list);
        assertEquals("Report Created", message.getText());
    }

	@Test
    void testMessageListConstructorListSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Course1");
        Message message = new Message(MessageType.REPORT, Status.SUCCESS, "Report Created", list);
        assertEquals(1, message.getList().size());
    }

	@Test
    void testMessageListConstructorListContent() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Course1");
        Message message = new Message(MessageType.REPORT, Status.SUCCESS, "Report Created", list);
        assertEquals("Course1", message.getList().get(0));
    }


}
