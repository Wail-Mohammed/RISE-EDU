package app.Client;

import java.io.*;
import java.net.*;
import app.Shared.*;

class Client {
	public static void main(String[] args) {
	    Socket socket = null;
	    ObjectOutputStream out = null;
	    ObjectInputStream in = null;

	    try {
	        socket = new Socket("localhost", 9898);
	        out = new ObjectOutputStream(socket.getOutputStream());
	        in = new ObjectInputStream(socket.getInputStream());

	        Message loginMsg = new Message(Type.CONNECT, Status.NULL, "Logging in now...");
	        out.writeObject(loginMsg);
	        Message response = (Message) in.readObject();
	        System.out.println("Server: " + response.getText());

	        StudentGUI student = new StudentGUI(socket, in, out);
	        student.processCommands();
	        
	        AdminGUI admin = new StudentGUI(socket, in, out);
	        admin.processCommands();

	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
}