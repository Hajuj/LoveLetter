
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;


public class MyChatServer {
    private static int port = 47329;
    //private Set<UserThread> userThreads = new HashSet<>();
    //private Set<String> usernames =new HashSet<>();

    public static ObservableList<String> serverLog;
    public static ObservableList<String> clientNames;
    private static ArrayList<UserThread> userThreads;
    private static ArrayList<String> usernames;
    private ServerSocket socket;
    private Socket userSocket;


    public MyChatServer(int port) throws IOException {
        MyChatServer.port = port;

        serverLog = FXCollections.observableArrayList();
        clientNames = FXCollections.observableArrayList();
        usernames = new ArrayList<String>();
        userThreads = new ArrayList<UserThread>();
        socket = new ServerSocket(port);
    }

    public Set<String> getUsernames() {
        return (Set<String>) clientNames;
    }

    /*public ArrayList<Socket> getUsernames() {
        return usernames;
    }*/
    public void execute() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverLog = FXCollections.observableArrayList();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUserThread = new UserThread(socket, this);
                userThreads.add(newUserThread);
                newUserThread.start();
            }
        } catch (IOException e) {
            //System.out.println("Error in the Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                //Adding to Log that the Server's listening
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        serverLog.add("Waiting for Client");
                    }
                });

                final Socket clientSocket = socket.accept();

                //System.out.println("New user connected");
                usernames.add(String.valueOf(clientSocket));

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        serverLog.add("User " + clientSocket.getRemoteSocketAddress() + "connected");
                    }
                });

                UserThread newUserThread = new UserThread(userSocket, this);
                Thread clientThread = new Thread(newUserThread);

                userThreads.add(newUserThread);
                newUserThread.setDaemon(true);
                newUserThread.start();
                //MyChatServerApp.threads.add(newUserThread);
            }
        } catch (Exception e) {
            //System.out.println("Error in the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clientDisconnect(UserThread user) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                serverLog.add("User " + user.getClientSocket().getRemoteSocketAddress() + "disconnected");
                usernames.remove(userThreads.indexOf(user));
                clientNames.remove(userThreads.indexOf(user));
                userThreads.remove((userThreads.indexOf(user)));
            }
        });
    }

    public static void writeToAllSockets(String input) {
        for (UserThread userThread : userThreads) {
            userThread.sendMessage(input);
        }
    }

    public String addUserNames(String userName) {
        usernames.add(userName);
        return userName;
    }


    public void broadcast(String message, UserThread excludedUser) {
        for (UserThread user : userThreads) {
            if (user != excludedUser && !message.isBlank()) {
                user.sendMessage(message);
            }
        }
    }

    public void directMessage(String message, UserThread user) {
        user.sendMessage(message);
    }

    public void directMessageWelcome(String message, UserThread user) {
        user.sendMessage(message);
    }


    public void removeUser(String username, UserThread user) {
        boolean removed = usernames.remove(username);
        if (removed) {
            userThreads.remove(user);
            System.out.println("The user " + username + " left the room");
        }
    }


    public static void main(String[] args) throws IOException {
        MyChatServer server = new MyChatServer(port);
        server.execute();
    }
}
