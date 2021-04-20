import java.io.*;
import java.net.Socket;

public class UserThread extends Thread {
    private Socket socket;
    private MyChatServer server;
    private PrintWriter writer;
    private String username;

    /**
     * @param socket server socket
     * @param server chat server
     */

    public UserThread(Socket socket, MyChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            while (true) {
                username = reader.readLine();
                if (username == null) {
                    return;
                }
                if (!username.isBlank() && !server.getUsernames().contains(username)) {
                    server.addUsername(username);
                    break;
                } else if (!username.isBlank()) {
                    server.directMessage("This username is already given!", this);
                }
            }

            String serverMessage = username + " joined the room";
            server.broadcast(serverMessage, this);
            server.directMessage("Welcome " + username, this);

            String clientMessage;
            do {
                clientMessage = reader.readLine();
                if (!clientMessage.isBlank()) {
                    serverMessage = "[" + username + "]: " + clientMessage;
                    server.broadcast(serverMessage, this);
                }
            } while (!"bye".equalsIgnoreCase(clientMessage));

            server.removeUser(username, this);
            serverMessage = username + " left the room";
            server.broadcast(serverMessage, this);

        } catch (Exception e) {
            System.out.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}