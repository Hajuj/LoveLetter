
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int port = 500;
        ConsoleHelper.writeMessage("Portnummer: " + port);
        //int port = ConsoleHelper.readInt();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Server läuft!");

            while (true) {
                // warte auf Client Socket
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Es gab leider einen Fehler beim Server.");
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Client Socket " + socket.getRemoteSocketAddress() + " connected.");

            String userName = null;

            try (Connection connection = new Connection(socket)) {
                userName = serverHandshake(connection);

                // Brodcasting für alle, dass ein neuer User da ist
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));

                //  say an alle about new user
                notifyUsers(connection, userName);

                // user messages
                serverMainLoop(connection, userName);

            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Error in communication with  " + socket.getRemoteSocketAddress());
            }

            if (userName != null) {
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
            }

            ConsoleHelper.writeMessage("Socket " + socket.getRemoteSocketAddress() + " closed.");
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));

                Message message = connection.receive();

                String userName = message.getData();

                if (userName.isEmpty()) {
                    ConsoleHelper.writeMessage("User " + socket.getRemoteSocketAddress() + "hat keinen Namen eingegeben");
                    continue;
                }

                if (connectionMap.containsKey(userName)) {
                    ConsoleHelper.writeMessage("User mit Nickname " + userName + " ist schon im Chat" );
                    continue;
                }
                connectionMap.put(userName, connection);
                System.out.println("user " + userName + " ist da" );
                connection.send(new Message(MessageType.NAME_ACCEPTED));
                sendBroadcastMessageExceptUser(new Message(MessageType.TEXT, userName + " joined the room!"), connection);
                sendDirectMessage(new Message(MessageType.TEXT, "Welcome " + userName + "!"), connection);
                return userName;
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String name : connectionMap.keySet()) {
                if (name.equals(userName))
                    continue;
                connection.send(new Message(MessageType.USER_ADDED, name));
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.TEXT) {
                    String data = message.getData();

                    if (message.getData().equals("bye")){
                        connection.close();
                        connectionMap.remove(userName);
                        sendBroadcastMessage(new Message(MessageType.TEXT, userName + " left the room"));
                    } else {
                        sendBroadcastMessage(new Message(MessageType.TEXT, userName + " : " + data));
                    }
                }
            }
        }
    }

    public static void sendBroadcastMessage(Message message) {
        // schickt Message zu Users
        for (Connection connection : connectionMap.values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Fehler beim Schicken zu Client " + connection.getRemoteSocketAddress());
            }
        }
    }

    public static void sendBroadcastMessageExceptUser(Message message, Connection userconnection) {
        // Send a message to all except userconnection

        for (Connection connection : connectionMap.values()) {
            if (!connection.equals(userconnection)) {
                try {
                    connection.send(message);
                } catch (IOException e) {
                    ConsoleHelper.writeMessage("Error with " + connection.getRemoteSocketAddress());
                }
            }
        }
    }


    public static void sendDirectMessage(Message message, Connection userConnection) {
        // Send only an user userConnection
        try {
            userConnection.send(message);
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error with " + userConnection.getRemoteSocketAddress());
        }
    }
}
