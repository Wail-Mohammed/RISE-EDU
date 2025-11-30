package app.Client;

import java.io.*;
import java.net.Socket;
import app.Shared.Message;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client() {
    }

     //Connects to the server
    public void connect(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

     //Sends a request over the connection.
    public Message send(Message message) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Not connected to server.");
        }
        
        // Write message
        out.writeObject(message);
        out.flush();
        
        // Wait for response on the same connection
        return (Message) in.readObject();
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}