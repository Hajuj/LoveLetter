import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;

import java.net.*;
import java.util.Observable;


public class MyChatClient extends Observable {
    private String hostname;
    private int port;
    private Socket clientSocket;
    private MyChatServer baseServer;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    public ObservableList<String> chatLog;

    //name of the client
    private String userName;


    public MyChatClient(String hostname, int port){
        this.hostname =hostname;
        this.port = port;
    }


    public void execute() {
        try {
            this.userName = getClientNameFromNetwork();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    baseServer.clientNames.add(userName + " - "
                            + clientSocket.getRemoteSocketAddress());
                    setChanged();
                    notifyObservers(this);

                    new ReadThread(socket, this).start();
                    new WriteThread(socket, this).start();

                }

            });
            //System.out.println("Du bist nun connected!");


        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
        setChanged();
        notifyObservers(this);
    }

    String getUserName() {
        return this.userName;
    }

    public String getClientNameFromNetwork() throws IOException {
        return reader.readLine();
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        setChanged();
        notifyObservers(this);
    }

    public static void main(String[] args) {
        MyChatClient client = new MyChatClient("127.0.0.1", 47329);
        client.execute();
    }
}
