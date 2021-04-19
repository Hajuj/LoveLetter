package Chat1904;


import java.io.*;
import java.net.Socket;

import java.net.*;
public class MyChatClient {
    private String hostname;
    private int port;
    private String userName;

    public MyChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Du bist nun connected!");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }


    public static void main(String[] args) {
        MyChatClient client = new MyChatClient("127.0.0.1", 47329);
        client.execute();
    }
}
