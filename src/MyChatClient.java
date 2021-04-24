import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;

import java.net.*;
import java.util.Observable;


public class MyChatClient extends Observable {
    private String hostname;
    private int port;

    /*
    private Socket clientSocket;
    private MyChatServer baseServer;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    public ObservableList<String> chatLog;

    //name of the client
    private String userName;
*/
    public MyChatClient(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;

/*
        clientSocket = new Socket(hostname, port);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        chatLog = FXCollections.observableArrayList();
        this.userName = getUserName();
        writer.println(getUserName()); */
    }


    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("You are now connected to the server!");

            new ReadThread(socket).start();
            new WriteThread(socket, this).start();


            /*this.userName = getClientNameFromNetwork();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    baseServer.clientNames.add(userName + " - "
                            + clientSocket.getRemoteSocketAddress());

                    new ReadThread(socket, this).start();
                    new WriteThread(socket, this).start();

                }
            });
            //System.out.println("Du bist nun connected!"); */


        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

    }

    void setUserName(String userName) {
        /*this.userName = userName;
        setChanged();
        notifyObservers();*/
    }
    /*

    String getUserName() {
        return this.userName;
    }

    public String getClientNameFromNetwork() throws IOException {
        return reader.readLine();
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        setChanged();
        notifyObservers();
    }
*/
    public static void main(String[] args) throws IOException {
        MyChatClient client = new MyChatClient("127.0.0.1", 47329);
        client.execute();
    }


}
