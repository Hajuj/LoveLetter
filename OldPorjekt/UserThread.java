import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;



public class UserThread extends Thread {
    private Socket socket;
    private MyChatServer server;
    private PrintWriter writer;

    /**
     * 
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

            String username = reader.readLine();
            server.addUsername(username);
 
            String serverMessage = "New user connected: " + username;
            server.broadcast(serverMessage, this);
            
            String clientMessage; 

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + username + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
            } while(!"bye".equalsIgnoreCase(clientMessage));

            server.removeUser(username, this);
            serverMessage = username + "has left";
            server.broadcast(serverMessage, this);

        } catch (Exception e) {
            System.out.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.write(message);
    }

}
