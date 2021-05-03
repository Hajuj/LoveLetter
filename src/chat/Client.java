package chat;

import server.*;
import server.Connection;
import server.ConsoleHelper;
import server.Message;
import server.MessageType;

import java.io.IOException;
import java.net.Socket;



/**
 * The type Chat.Client.
 */
/*Chat.Client Class für Socket Verbindungen der Threads*/
public class Client {
    /**
     * The Server.Connection.
     */
    protected Connection connection = new Connection(new Socket(getServerAddress(), getServerPort()));
    private volatile boolean clientConnected;

    /**
     * Instantiates a new Chat.Client.
     *
     * @throws IOException the IO exception
     */
    public Client() throws IOException {
    }

    /**
     * Send text message.
     *
     * @param text the text
     */
    /*Nachricht senden an alle*/
    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error beim Senden");
            clientConnected = false;
        }
    }

    /**
     * Run methode.
     */
    /*Run Methode für die Chat.Client Verbindung per Sockets*/
    public void run() {
        SocketThread socketThread = getSocketThread();
        // thread ist daemon
        socketThread.setDaemon(true);
        socketThread.start();

        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("Fehler");
            return;
        }

        if (clientConnected)
            ConsoleHelper.writeMessage("You are connected");
        else
            ConsoleHelper.writeMessage("Fehler");

        while (clientConnected) {
            String text = ConsoleHelper.readString();

            if (shouldSendTextFromConsole())
                sendTextMessage(text);
        }
    }

    /**
     * Gets server address.
     *
     * @return the server address
     */
    /*Getter Methoden für IP, Port und Name - Vorerst aber fest definiert bei "127.0.0.1" und 500*/
    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Server.Server IP:");
        return ConsoleHelper.readString();
    }

    /**
     * Gets server port.
     *
     * @return the server port
     */
    protected int getServerPort() {
        ConsoleHelper.writeMessage("Server.Server Port:");
        return ConsoleHelper.readInt();
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    protected String getUserName() {
        ConsoleHelper.writeMessage("Your name:");
        return ConsoleHelper.readString();
    }

    /**
     * Gets socket thread.
     *
     * @return the socket thread
     */
    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    /**
     * Should send text from console boolean.
     *
     * @return the boolean
     */
    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    /**
     * The type Socket thread.
     */
    /*Run Methode für Handshake und Mainloop*/
    public class SocketThread extends Thread {
        @Override
        public void run() {
            try {
                clientHandshake();
                clientMainLoop();

            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }

        /**
         * Chat.Client Handshake.
         *
         * @throws IOException            the IO exception
         * @throws ClassNotFoundException the class not found exception
         */
        /*client Handshake um die Nachrichten zu synchronisieren*/
        protected void clientHandshake() throws IOException, ClassNotFoundException {
            String name = null;
            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.NAME_REQUEST) { // ask the name
                    if (name == null || !name.equals(getUserName())) {
                        name = getUserName();
                        this.notifyConnectionStatusChanged(false);
                    }
                } else if (message.getType() == MessageType.NAME_ACCEPTED) { // server accepted the name
                    this.notifyConnectionStatusChanged(true);
                    return;
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        /**
         * Chat.Client main loop.
         *
         * @throws IOException            the IO exception
         * @throws ClassNotFoundException the class not found exception
         */
        /*Verwaltung der Art von Informationen die über den Server.Server laufen*/
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                } else if (MessageType.USER_ADDED == message.getType()) {
                    informAboutAddingNewUser(message.getData());
                } else if (MessageType.USER_REMOVED == message.getType()) {
                    informAboutDeletingNewUser(message.getData());
                } else {
                    throw new IOException("Unexpected Server.MessageType");
                }
            }
        }

        /**
         * Process incoming message.
         *
         * @param message the message
         */
        /* Weitergabe der Nachricht*/
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        /**
         * Inform about adding new user.
         *
         * @param userName the user name
         */
        /*Begrüßung bei Anmeldung */
        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage("Welcome " + userName + "!");
        }

        /**
         * Inform about deleting new user.
         *
         * @param userName the user name
         */
        /*Benachrichtigung über Abmeldung */
        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("Username  '" + userName + "' left.");
        }

        /**
         * Notify connection status changed.
         *
         * @param clientConnected the client connected
         */
        /*Benachrichtigung über Anmeldung */
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }
}
