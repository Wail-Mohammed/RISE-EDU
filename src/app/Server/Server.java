package app.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

import app.Shared.Message;
import app.Shared.Status;
import app.Shared.Type;

public class Server {

    private static SystemManager manager;
    private static DataManager dataManager;
    private static University university;
    
	public static void main(String[] args) throws Exception {
    	
    	// Initializing system manager and data manager to load and handle data
        manager = SystemManager.getInstance();
        dataManager = new DataManager();

        // Loading data from files
        university = dataManager.loadDataFromFiles();
        manager.loadUniversity(university);
        
        // adding shutdown hook to save data when shutting down the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is shutting down. Saving data...");
            dataManager.saveDataToFiles(university);
            System.out.println("Data is saved. Exiting...");
        }));
    	
        // Starting the server
        try (var listener = new ServerSocket(9898)) {
            System.out.println("The server is running...");
            var pool = Executors.newFixedThreadPool(100);
            while (true) {
                pool.execute(new ClientHandler(listener.accept()));
            }
        }
    }
	
	// ClientHandler to handle multiple clients at once 
	private static int clientCount = 0;

    private static class ClientHandler implements Runnable {
    	private Socket socket;
        private SystemManager manager;
        private User currentUser;

        ClientHandler(Socket socket, SystemManager manager) {
            this.socket = socket;
            this.manager = manager;
        }
        
        @Override
        public void run() {
        	clientCount+= 1;
            System.out.println("Client " + clientCount + " is connected at : " + socket);
            
            try {
                var in = new ObjectInputStream(socket.getInputStream());
                var out = new ObjectOutputStream(socket.getOutputStream());

                Message message = (Message) in.readObject();
                
                //Attempting to login sending a message of type LOGIN if so we proceed to command while loop
                if (message.getType().equals(Type.LOGIN)) {
                    
                    //SystemManager will process login
                    System.out.println("Processing Client's LOGIN request");
                    String username = message.getList().get(0);
                    String password = message.getList().get(1);
                    Message response = manager.authenticateUser(username, password);
                    out.writeObject(response);
                    out.flush();

                	//Looping through different commands/features
                	if (response.getStatus() == Status.SUCCESS) {
                        this.currentUser = manager.getUniversity().getUser(username);
                        System.out.println("User " + this.currentUser.getUsername() + " has successfully logged in");

                	    while (true) {
                		    message  = (Message) in.readObject();
                            Type type = message.getType();
                            Message msgResponse = null;

                            switch (type) {
                            //For students
                                case ENROLL_COURSE:
                                    System.out.println("System is Processing ENROLL Request...");
                                    msgResponse = manager.processEnrollment(this.currentUser.getUsername(), message.getText());
                                    break;
                                
                                case DROP_COURSE:
                                    System.out.println("System is Processing DROP Request...");
                                    msgResponse = manager.processDrop(this.currentUser.getUsername(), message.getText());
                                    break;

                                case VIEW_SCHEDULE:
                                    System.out.println("System is Processing VIEW_SCHEDULE Request...");
                                    msgResponse = manager.getStudentSchedule(this.currentUser.getUsername());
                                    break;
                                case VIEW_WAITLIST:
                                    System.out.println("System is Processing VIEW_WAITLIST Request...");
                                    msgResponse = manager.getWaitlist(this.currentUser.getUsername(), message.getText());
                                    break;
                                
                                case CHECK_PREREQS:
                                    System.out.println("System is Processing CHECK_PREREQS Request...");
                                    msgResponse = manager.checkPrerequisites(message.getText());
                                    break;
                                
                                case LIST_COURSES:
                                    System.out.println("System is Processing LIST_COURSES Request...");
                                    msgResponse = manager.getAllCourses();
                                    break;
                                    
                                //For admin
                                case CREATE_COURSE:
                                    System.out.println("System is Processing Admin CREATE_COURSE Request...");
                                    msgResponse = manager.createCourse(message.getList());
                                    break;
                                case EDIT_COURSE:
                                	System.out.println("System is Processing Admin EDIT_COURSE Request...");
                                	msgResponse = manager.editCourse(message.getList());
                                    break;
                                case REMOVE_COURSE:
                                	System.out.println("System is Processing Admin REMOVE_COURSE Request...");
                                	msgResponse = manager.deleteCourse(message.getText());
                                    break;
                                case GET_REPORT:
                                	System.out.println("System is Processing Admin GET_REPORT Request...");
                                	msgResponse = manager.getReport();
                                    break;
                                case VIEW_STUDENTS:
                                	System.out.println("System is Processing Admin VIEW_STUDENTS Request...");
                                	msgResponse = manager.getAllStudents();
                                    break;
                                case ADD_HOLD:
                                	System.out.println("System is Processing Admin ADD_HOLD Request...");
                                    String studentUserAdd = message.getList().get(0);
                                    String reasonAdd = message.getList().get(1);
                                    msgResponse = manager.placeHoldOnAccount(studentUserAdd, reasonAdd);
                                    break;
                                case REMOVE_HOLD:
                                	System.out.println("System is Processing Admin REMOVE_HOLD Request...");
                                    String studentUserRemove = message.getList().get(0);
                                    String reasonRemove = message.getList().get(1);
                                    msgResponse = manager.removeHoldOnAccount(studentUserRemove, reasonRemove);
                                    break;
                                
                                case LOGOUT:
                                    System.out.println("Logging out " + this.currentUser.getUsername() + "...");
                			        this.currentUser = null;
                			        msgResponse = new Message(Type.LOGOUT, Status.SUCCESS, "Logout successful.");
                			        break;
                                
                                default:
                                	msgResponse = new Message(Type.CONNECT, Status.FAIL, "Unknown request type.");
                            }
                		    
                            out.writeObject(msgResponse);
                            out.flush();

                            if (type == Type.LOGOUT) {
                                break;
                            }
                	    }
                    } else {
                        System.out.println("Login failed. Closing connection.");
                    }
                } else {
                    System.out.println("Error: The client did not send LOGIN message first. Closing connection.");
                    Message failedMessage = new Message(message.getType(), Status.FAIL, "Login is required to use this service");
                    out.writeObject(failedMessage);
                }
            } catch (EOFException e) {
                System.out.println("Client " + clientCount + " disconnected cleanly.");
            } catch (Exception e) {
                System.out.println("Error for client " + clientCount + ": " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // ignore
                }
                System.out.println("Closed Client Connection " + clientCount + " at Socket: " + socket);
            }
        }
    }
}

