package main.java.app.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class UserInterface {
    protected Socket socket;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    protected String username;

    public UserInterface(Socket socket, ObjectInputStream in, ObjectOutputStream out, String username) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.username = username;
    }

    public abstract void processCommands();
}
