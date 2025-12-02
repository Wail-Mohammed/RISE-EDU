package app.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.models.User;
import app.models.University;
import app.Shared.Message;
import app.Shared.Status;
import app.Shared.MessageType;


public class Server {

    private static SystemManager manager;
    private static DataManager dataManager;
    
    private static ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws Exception {
        
        //Initialize project business logic and data manager logic, system manager is a facade that handles the core logic and processes.
        manager = SystemManager.getInstance();
        dataManager = new DataManager();

        //Load the data 
        System.out.println("Loading data from files...");
        ArrayList<University> allUniversities = dataManager.loadAllUniversities();
        
        if (allUniversities.isEmpty()) {
            System.out.println("No universities found. Creating default university 'RISE-EDU'...");
            University defaultUniversity = new University("RISE-EDU");
            manager.loadUniversity(defaultUniversity);
        } else {
            for (University university : allUniversities) {
                System.out.println("Loaded: " + university.getUniversityName());
                manager.loadUniversity(university);
            }
        }
        
        //When shutting down the server, we also save from memory to the files.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is shutting down. Saving all data...");
            // to save every existing university files to its own folder
            for (String uniName : manager.getAllUniversityNames()) {
                University university = manager.getUniversity(uniName);
                if (university != null) {
                    dataManager.saveDataToFiles(university);
                }
            }
            System.out.println("Data saved. Goodbye.");
        }));
        
        //Here we start the server to listen for client connections, once connected we pass it to the clientHandler
        
        try (ServerSocket listener = new ServerSocket(9999, 50, InetAddress.getByName("0.0.0.0"))){
//        try (ServerSocket listener = new ServerSocket(9898, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("---------------------------------------------");
            System.out.println("The RISE-EDU Server is running.");
            System.out.println("Listening on Port: 9999");
//          System.out.println("Listening on Port: 9898");

            
            //printing the ip address of current network so others can use it to connect to this server.
            findandPrintCurrentIPAddress();             
            System.out.println("---------------------------------------------");

            while (true) {
                pool.execute(new ClientHandler(listener.accept(), manager));
            }
        }
    }

    //To find the real ip address
    private static void findandPrintCurrentIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        System.out.println("This is the Server IP Address: " + addr.getHostAddress() + "  (" + networkInterface.getDisplayName() + ")");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not detect IP address: " + e.getMessage());
        }
    }
    
    // ClientHandler that takes in requests from the client and pass back responses from the server.
    private static int clientCount = 0;

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private SystemManager manager;
        private User currentUser; 
        private University currentUniversity;
        private final int clientId;

        ClientHandler(Socket socket, SystemManager manager) {
            this.clientId = ++clientCount;
			this.socket = socket;
            this.manager = manager;
        }
        
        @Override
        public void run() {
            System.out.println("Client " + this.clientId  + " connected from: " + socket.getInetAddress());
            
            try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
                Message message;
                while ((message = (Message) in.readObject()) != null) {
                    MessageType type = message.getType();
                    Message response = null;

                    if (type == MessageType.LOGIN) {
                        System.out.println("Processing a LOGIN Type request...");
                        String username = message.getList().get(0);
                        String password = message.getList().get(1);
                        String userType =  message.getList().get(2);
                        String uniName =  message.getList().get(3);
                        
						response = manager.authenticateUser(username, password, userType, uniName);
                        
                        if (response.getStatus() == Status.SUCCESS) {
                            this.currentUniversity = manager.getUniversity(uniName);
                            this.currentUser = manager.findUser(currentUniversity, username);
                            System.out.println("User '" + username + "' is logged in to " + uniName);
                        }
                    } 
                    else if (type == MessageType.LOGOUT) {
                        System.out.println("Client is logging out...");
                        response = new Message(MessageType.LOGOUT, Status.SUCCESS, "Logged out successfully.");
                    }
                    else if (type == MessageType.ADD_UNIVERSITY) {
                        System.out.println("Processing ADD_UNIVERSITY request...");
                        String newUniversityName = message.getText(); 
                        response = manager.createNewUniversity(newUniversityName);
                    }
                    else {
                    	if (currentUniversity == null) { //passing university to all methods
                            response = new Message(MessageType.LOGIN, Status.FAIL, "Error: No University Selected");
                        } else {
                        // to pass the requests to the server (system manager)
                        switch (type) {
	                        case ADD_USER:
	                            response = manager.addUser(currentUniversity, message.getList());
	                            break;
	                        //case ADD_UNIVERSITY:
	                        //    String name = message.getText(); 
	                        //    response = manager.createNewUniversity(name);
	                        //    break;
                            case ENROLL_COURSE:
                                response = manager.processEnrollment(currentUniversity, currentUser.getUsername(), message.getText());
                                break;
                            case DROP_COURSE:
                                response = manager.processDrop(currentUniversity, currentUser.getUsername(), message.getText());
                                break;
                            case VIEW_SCHEDULE:
                                response = manager.getStudentSchedule(currentUniversity, currentUser.getUsername());
                                break;
                            case LIST_COURSES:
                                response = manager.getAllCourses(currentUniversity);
                                break;
                            case VIEW_HOLD:
                                response = manager.getStudentHolds(currentUniversity, currentUser.getUsername());
                                break;
                            case CREATE_COURSE:
                                response = manager.createCourse(currentUniversity, message.getList());
                                break;
                            case REMOVE_COURSE:
                                response = manager.deleteCourse(currentUniversity, message.getText());
                                break;
                            case VIEW_STUDENTS:
                                response = manager.getAllStudents(currentUniversity);
                                break;
                            case VIEW_ADMINS:
                                response = manager.getAllAdmins(currentUniversity);
                                break;
                            case VIEW_STUDENT_SCHEDULE:
                                // Args: studentId in message.getText()
                                response = manager.getStudentScheduleByStudentId(currentUniversity, message.getText());
                                break;
                            case GET_REPORT:
                                response = manager.getReport(currentUniversity);
                                break;
                            case ADD_HOLD:
                                response = manager.placeHoldOnAccount(currentUniversity, message.getList().get(0), message.getList().get(1));
                                break;
                            case REMOVE_HOLD:
                                response = manager.removeHoldOnAccount(currentUniversity, message.getList().get(0), message.getList().get(1));
                                break;
                            case WITHDRAW_STUDENT:
                                Message dropResult = manager.processDropByStudentId(currentUniversity, message.getList().get(0), message.getList().get(1));
                                response = new Message(MessageType.WITHDRAW_STUDENT, dropResult.getStatus(), dropResult.getText());
                                break;
                            case LIST_ENROLLMENT:
                                response = manager.getEnrollmentList(currentUniversity, message.getText());
                                break;
                            case EDIT_COURSE:
                                response = manager.editCourse(currentUniversity, message.getList());
                                break;
                            case VIEW_UNIVERSITIES:
                            	response = new Message(MessageType.VIEW_UNIVERSITIES, Status.SUCCESS, "Available", new ArrayList<>(manager.getAllUniversityNames()));                                break;
                            default:
                                response = new Message(type, Status.FAIL, "Unknown request.");
                        }
                    }
                    }

                    out.writeObject(response);
                    out.flush();
                    
                    if (type == MessageType.LOGOUT) break;
                }
            } catch (EOFException e) {
                System.out.println("Client " + this.clientId  + " disconnected.");
            } catch (Exception e) {
                System.err.println("Error handling client " + this.clientId  + ": " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }
}