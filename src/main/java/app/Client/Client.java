package app.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import app.Shared.Message;

/*We decided to remove the older version of the Client class because it didn’t match the client-server architecture. 
That version was a console app, it had a main() method, used a Scanner for input, and passed raw sockets and streams 
directly into the GUI. This makes the GUI to handle networking itself, which breaks the UI and the client-server layer.

In the updated vision, the GUI should only talk to the server through Client.send(Message) method. 
The client will act communication, not a user interface. 
 */

public class Client implements AutoCloseable {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Client() {
        this("localhost", 9898);
    }

    public Message send(Message request) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();

            return (Message) in.readObject();
        }
    }

    @Override
    public void close() throws Exception {
        
    }
}