import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;




public class MyChatServer {
    private static int port = 47329;
    private Set<UserThread> userThreads = new HashSet<>();
    private Set<String> usernames = new HashSet<>();

    public MyChatServer(int port) {
        MyChatServer.port = port;
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Chat server is online!");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUserThread = new UserThread(socket, this);
                userThreads.add(newUserThread);
                newUserThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error in the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyChatServer server = new MyChatServer(port);
        server.execute();
    }

    public void addUsername(String username) {
        usernames.add(username);
    }

    public void broadcast(String message, UserThread excludedUser) {
        for (UserThread user : userThreads) {
            if (user != excludedUser) {
                user.sendMessage(message);
            }
        }
    }

    public void directMessage(String message, UserThread user) {
            user.sendMessage(message);
    }

    public void removeUser(String username, UserThread user) {
        boolean removed = usernames.remove(username);
        if (removed) {
            userThreads.remove(user);
            System.out.println("The user " + username + " quitted");
        }
    }
}