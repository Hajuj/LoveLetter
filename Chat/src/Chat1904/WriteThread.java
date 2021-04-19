package Chat1904;

import java.io.*;
import java.net.*;


public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private MyChatClient client;

    public WriteThread(Socket socket, MyChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Was ist dein Nickname? ");
        String userName = null;
        try {
            userName = consoleReader.readLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        client.setUserName(userName);
        writer.println(userName);

        String text = "";

        do {
            text = "";
            try {
                text = consoleReader.readLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (!text.equals("")) {
            writer.println(text); }

        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
