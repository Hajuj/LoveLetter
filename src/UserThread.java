import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class UserThread extends Thread {
    private MyChatClient client;
    private MyChatServer server;

    private Socket userSocket;

    private static PrintWriter writer;
    private BufferedReader reader;
    //name of user
    private String username;

    //Thread for multiple clients: connection for each connected client
    public UserThread(Socket userSocket, MyChatServer server) {
        this.userSocket = userSocket;
        this.server = server;
    }

    /*public UserThread(MyChatClient client) {
        this.client = client;
    }*/

    @Override
    public void run() {
        try {
            InputStream input = userSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            writer = new PrintWriter(userSocket.getOutputStream(), true);

            while (true) {
                username = reader.readLine();
                if (username == null) {
                    return;
                }
                if (!username.isBlank() && !server.getUsernames().contains(username)) {
                    server.addUseNames(username);
                    System.out.println(server.getUsernames());
                    break;
                }
            }
            //Server Messages
            String serverMessage = username + " joined the room.";
            server.broadcast(serverMessage, this);
            server.directMessage("Welcome " + username,this);

            //Client Messages
            String clientMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + username + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
            } while(!"bye".equalsIgnoreCase(clientMessage));

            server.removeUser(username, this);
            serverMessage = username + "left the room";
            server.broadcast(serverMessage, this);

        } catch (Exception e) {
            System.out.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message) {
        writer.println(message);
    }

    public Socket getClientSocket() {
        return userSocket;
    }

}

