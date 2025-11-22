package Client;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import app.Client.Client;
import app.Shared.Message;
import app.Shared.MessageType;
import app.Shared.Status;

public class ClientTester {

	@Test
    void sendWithoutConnectFails() {
        Client client = new Client();
        assertThrows(IOException.class, () -> client.send(new Message(MessageType.LOGIN, Status.NULL, "")));
    }

    @Test
    void disconnectSafe() {
        Client client = new Client();
        client.disconnect();
        assertTrue(true);
    }

}
