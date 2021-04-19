import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.Socket;

public class MyChatClient {

    private String serverAddress;
    private BufferedReader in;
    private PrintWriter out;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 47329;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            System.out.println("> " + System.in);
            String command = keyboard.readLine();
            if (command.equals("bye")) break;
            out.println(command);
            String serverResponse = input.readLine();
            System.out.println("chatHandler.Server says: " + serverResponse);
        }
        socket.close();
        System.exit(0);

    }
}

