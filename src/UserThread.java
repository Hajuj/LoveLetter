import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;


public class UserThread extends Thread {
    private Socket userSocket;
    private MyChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;
    private String username;

    //Thread for multiple clients: connection for each connected client
    public UserThread(Socket userSocket, MyChatServer server) {
        this.userSocket = userSocket;
        this.server = server;
        try{
            InputStream input = userSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = userSocket.getOutputStream();
            writer = new PrintWriter(output, true);

            while (true) {
                username = reader.readLine();
                if (username == null) {
                    return;
                }
                if (!username.isBlank() && !server.getUsernames().contains(username)) {
                    server.addUserNames(username);
                    break;
                } else if (!username.isBlank()) {
                    server.directMessage("This username is already given!", this);
                }
            }

            //Server Messages
            String serverMessage = username + " joined the room.";
            server.broadcast(serverMessage, this);
            server.directMessageWelcome("Welcome " + username,this);


            //Client Messages
            String clientMessage;
            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + username + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
            }
            while(!"bye".equalsIgnoreCase(clientMessage));

            //Server send removeUser
            server.removeUser(username, this);
            serverMessage = username + "left the room";
            server.broadcast(serverMessage, this);

        } catch (IOException e) {
            //System.out.println("Error in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }


/*
    @Override
    public void run() {
        try{
            this.username = getClientNameFromNetwork();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MyChatServer.clientNames.add(username + " - ") + userSocket.getRemoteSocketAddress());

                }
            });
            String inputToServer;
            while (true) {
                inputToServer = reader.readLine();
                MyChatServer.writeToAllSockets(inputToServer);
            }
        } catch (SocketException e) {
            MyChatServer.clientDisconnect(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String getClientNameFromNetwork() throws IOException {
        return reader.readLine();
    }
*/

    public void sendMessage(String message) {
        writer.println(message);
    }
/*
    public Socket setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }*/

}

