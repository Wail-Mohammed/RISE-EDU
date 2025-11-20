package app.Client;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;
import app.Shared.*;

class Client {
	public static void main(String[] args) {
	    Socket socket = null;
	    ObjectOutputStream out = null;
	    ObjectInputStream in = null;
	    Scanner sc = new Scanner(System.in);

	    try {
	        socket = new Socket("localhost", 9898);
	        out = new ObjectOutputStream(socket.getOutputStream());
	        in = new ObjectInputStream(socket.getInputStream());
	        
	        System.out.println("Connected to server.");
	        
	        System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            
            ArrayList<String> loginCredentials = new ArrayList<>();
            loginCredentials.add(username);
            loginCredentials.add(password);

	        Message loginMsg = new Message(Type.LOGIN, Status.NULL, "Logging in now...");
	        out.writeObject(loginMsg);
	        
	        Message response = (Message) in.readObject();
	        System.out.println("Server: " + response.getText());
	        
	        //Launching the GUI
	        if (response.getUserType() === UserType.STUDENT) {
	        	System.out.println("Launching Student Dashboard...");
                gui = new StudentGUI(socket, in, out, username);
	        	
	        } else if (response.getUserType() === UserType.STUDENT) {
	        	System.out.println("Launching Admin Dashboard...");
                gui = new AdminGUI(socket, in, out, username);
	        	
	        } 
		        if (gui != null) {
		        	
	                gui.processCommands();
	            }
	        
	        else {
	        	System.out.println("Login failed. Exiting...");
	        }

	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    finally {
	    	// To close socket and scanner
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
	    }
	}
}