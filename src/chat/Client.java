package chat;

import server.*;

import java.io.IOException;
import java.net.Socket;


// TODO Check the users connected not showing, after writing a false name more than once.
// TODO Check the notifyConnectionStatusChanged() (line 134 here) method again to fix the name and welcome problem.
// TODO Fix message/error not showing, After trying to send a direct message but the name is not written correctly.
// TODO Change the error message, when writing only '@' in chat.

/**
 * The type chat.Client.
 */
/*chat.Client Class für Socket Verbindungen der Threads*/
public class Client {
    /**
     * The server.Connection.
     */
    protected Connection connection = new Connection(new Socket(getServerAddress(), getServerPort()));
    private volatile boolean clientConnected;

    /**
     * Instantiates a new chat.Client.
     *
     * @throws IOException the io exception
     */
    public Client() throws IOException {
    }

    /**
     * Send text message at every user in chat.
     *
     * @param text the text
     */
    /*Nachricht senden an alle*/
    public void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error beim Senden");
            clientConnected = false;
        }
    }

    /**
     * Run.
     */
    /*Run Methode für die chat.Client Verbindung per Sockets*/
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
        ConsoleHelper.writeMessage("server.Server IP: 127.0.0.1");
        return "127.0.0.1";
    }

    /**
     * Gets server port.
     *
     * @return the server port
     */
    protected int getServerPort() {
        ConsoleHelper.writeMessage("server.Server Port: 5000");
        return 5000;
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
    /*Run Methode für Handshake und MainLoop*/
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
         * chat.Client handshake.
         *
         * @throws IOException            the io exception
         * @throws ClassNotFoundException the class not found exception
         */
        /*client Handshake um die Nachrichten zu synchronisieren*/
        protected void clientHandshake() throws IOException, ClassNotFoundException {
            String name = "";
            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.NAME_REQUEST) { // ask the name
                    if (!name.equals(getUserName())) {
                        name = getUserName();
                        connection.send(new Message(MessageType.USER_NAME, name));
                        this.notifyConnectionStatusChanged(false);
                    }
                } else if (message.getType() == MessageType.NAME_ACCEPTED) { // server accepted the name
                    this.notifyConnectionStatusChanged(true);
                    return;

                } else {
                    throw new IOException("Unexpected server.MessageType");
                }
            }
        }

        /**
         * chat.Client main loop.
         *
         * @throws IOException            the io exception
         * @throws ClassNotFoundException the class not found exception
         */
        /*Verwaltung der Art von Informationen die über den server.Server laufen*/
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
                    throw new IOException("Unexpected server.MessageType");
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
