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
    private static University university;
    
    private static ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws Exception {
        
        //Initialize project business logic and data manager logic, system manager is a facade that handles the core logic and processes.
        manager = SystemManager.getInstance();
        dataManager = new DataManager();

        //Load the data
        System.out.println("Loading data from files...");
        university = dataManager.loadDataFromFiles();
        manager.loadUniversity(university);
        
        //When shutting down the server we also save from memory to the files.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Server is shutting down. Saving data...");
            dataManager.saveDataToFiles(university);
            System.out.println("Data saved. Goodbye.");
        }));
        
        //Here we start the server to listen for client connections, once connected we pass it to the clientHandler
        
//        try (ServerSocket listener = new ServerSocket(9999, 50, InetAddress.getByName("0.0.0.0"))){
        try (ServerSocket listener = new ServerSocket(9898, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("---------------------------------------------");
            System.out.println("The RISE-EDU Server is running.");
//            System.out.println("Listening on Port: 9999");
          System.out.println("Listening on Port: 9898");

            
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
                        
                        response = manager.authenticateUser(username, password);
                        
                        if (response.getStatus() == Status.SUCCESS) {
                            this.currentUser = manager.findUser(username);
                            System.out.println("User '" + username + "' is logged in.");
                        }
                    } 
                    else if (type == MessageType.LOGOUT) {
                        System.out.println("Client is logging out...");
                        response = new Message(MessageType.LOGOUT, Status.SUCCESS, "Logged out successfully.");
                    }
                    else {
                        // to pass the requests to the server (system manager)
                        switch (type) {
	                        case ADD_USER:
	                            response = manager.addUser(message.getList());
	                            break;
                            case ENROLL_COURSE:
                                response = manager.processEnrollment(currentUser.getUsername(), message.getText());
                                break;
                            case DROP_COURSE:
                                response = manager.processDrop(currentUser.getUsername(), message.getText());
                                break;
                            case VIEW_SCHEDULE:
                                response = manager.getStudentSchedule(currentUser.getUsername());
                                break;
                            case LIST_COURSES:
                                response = manager.getAllCourses();
                                break;
                            case VIEW_HOLD:
                                response = manager.getStudentHolds(currentUser.getUsername());
                                break;
                            case CREATE_COURSE:
                                response = manager.createCourse(message.getList());
                                break;
                            case REMOVE_COURSE:
                                response = manager.deleteCourse(message.getText());
                                break;
                            case VIEW_STUDENTS:
                                response = manager.getAllStudents();
                                break;
                            case VIEW_ADMINS:
                                response = manager.getAllAdmins();
                                break;
                            case GET_REPORT:
                                response = manager.getReport();
                                break;
                            case ADD_HOLD:
                                response = manager.placeHoldOnAccount(message.getList().get(0), message.getList().get(1));
                                break;
                            case REMOVE_HOLD:
                                response = manager.removeHoldOnAccount(message.getList().get(0), message.getList().get(1));
                                break;
                            case WITHDRAW_STUDENT:
                                Message dropResult = manager.processDrop(message.getList().get(0), message.getList().get(1));
                                // Create new message with correct message type
                                response = new Message(MessageType.WITHDRAW_STUDENT, dropResult.getStatus(), dropResult.getText());
                                break;
                            case LIST_ENROLLMENT:
                                response = manager.getEnrollmentList(message.getText());
                                break;
                            case EDIT_COURSE:
                                response = manager.editCourse(message.getList());
                                break;
                            case VIEW_UNIVERSITIES:
                                response = manager.getAllUniversities();
                                break;
                            default:
                                response = new Message(type, Status.FAIL, "Unknown request.");
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