package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type chat.Server.
 */
public class Server {
    private final static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    /*Main Methode mit vorerst festen Werten (Hostname, Port Nummer)*/
    public static void main(String[] args) {
        int port = 5000;
        ConsoleHelper.writeMessage("Port Nummer: " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Server läuft!");
            while (true) {
                // warten auf chat.Client Socket
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Es gab leider einen Fehler beim Server." + e.getMessage());
        }
    }

    /**
     * Send broadcast message.
     *
     * @param message the message
     */
    /*Senden der Nachricht an alle User*/
    public static void sendBroadcastMessage(Message message) {
        for (Connection connection : connectionMap.values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Fehler beim Schicken zu Client " + connection.getRemoteSocketAddress());
            }
        }
    }

    /**
     * Send broadcast message except user.
     *
     * @param message        the message
     * @param userConnection the userConnection
     */
    /*Senden der Nachricht an alle User außer sich selbst*/
    public static void sendBroadcastMessageExceptUser(Message message, Connection userConnection) {

        for (Connection connection : connectionMap.values()) {
            if (!connection.equals(userConnection)) {
                try {
                    connection.send(message);
                } catch (IOException e) {
                    ConsoleHelper.writeMessage("Error with " + connection.getRemoteSocketAddress());
                }
            }
        }
    }

    /**
     * Send direct message.
     *
     * @param message        the message
     * @param userConnection the user connection
     */
    /*Senden der Nachricht an einen spezifischen User*/
    public static void sendDirectMessage(Message message, Connection userConnection) {
        try {
            userConnection.send(message);
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error with " + userConnection.getRemoteSocketAddress());
        }
    }

    /*Thread Handler mit run Methode - Herstellen der Verbindung und Willkommensnachricht*/
    private static class Handler extends Thread {
        private final Socket socket;

        /**
         * Instantiates a new Handler.
         *
         * @param socket the socket
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("chat.Client Socket " + socket.getRemoteSocketAddress() + " connected.");

            String userName = null;

            try (Connection connection = new Connection(socket)) {
                userName = serverHandshake(connection);

                // Mitteilung im Chat, dass ein neuer User da ist
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));

                //  Information, dass ein neuer User da ist (Im Fenster der User, die Online sind)
                notifyUsers(connection, userName);

                // Starte MainLoop für Nachrichten senden und empfangen
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

        /*Handshake Serverseitig: Überprüfen der Informationen und stetiges aktualisieren mit busy waiting*/
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));

                Message message = connection.receive();

                String userName = message.getData();

                if (userName.isBlank()) {
                    ConsoleHelper.writeMessage("User " + socket.getRemoteSocketAddress() + "hat keinen Namen eingegeben");
                    continue;
                }

                if (connectionMap.containsKey(userName)) {
                    ConsoleHelper.writeMessage("User mit Nickname " + userName + " ist schon im Chat");
                    continue;
                }

                if (userName.contains("@") || userName.contains(" ")) {
                    ConsoleHelper.writeMessage("UserName darf keine @ or Leerzeichen enthalten");
                    continue;
                }
                connectionMap.put(userName, connection);
                System.out.println("user " + userName + " ist da");
                connection.send(new Message(MessageType.NAME_ACCEPTED));
                sendBroadcastMessageExceptUser(new Message(MessageType.TEXT, userName + " joined the room!"), connection);
                sendDirectMessage(new Message(MessageType.TEXT, "Welcome " + userName + "!"), connection);
                return userName;
            }
        }

        /*Informieren der User über eine neue Person im Chat */
        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String name : connectionMap.keySet()) {
                if (name.equals(userName))
                    continue;
                connection.send(new Message(MessageType.USER_ADDED, name));
            }
        }

        /*Busy Waiting Loop für das Schließen der Verbindung*/
        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            do {
                Message message = connection.receive();

                if (message.getType() == MessageType.TEXT && !message.getData().isBlank()) {
                    String data = message.getData();


                    if (data.equals("bye")) {
                        connection.close();
                        connectionMap.remove(userName);
                        sendBroadcastMessage(new Message(MessageType.TEXT, userName + " left the room"));
                    } else if (data.charAt(0) == '@') {
                        try {
                            String usernameDirect = data.substring(1, data.indexOf(" "));
                            if (connectionMap.containsKey(usernameDirect) && !usernameDirect.equals(userName)) {
                                String directData = data.substring(data.indexOf(" ") + 1);
                                sendDirectMessage(new Message(MessageType.TEXT, userName + " : " + data), connection);
                                sendDirectMessage(new Message(MessageType.TEXT, userName + " to you : " + directData), connectionMap.get(usernameDirect));
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            sendDirectMessage(new Message(MessageType.TEXT, "Error bei direct messaging"), connection);
                        }
                    } else {
                        sendBroadcastMessage(new Message(MessageType.TEXT, userName + " : " + data));
                    }
                }
            } while (true);
        }
    }
}
