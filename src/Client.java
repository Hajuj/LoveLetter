import java.io.IOException;
import java.net.Socket;

/*Client Class für Socket Verbindungen der Threads*/
public class Client {
    protected Connection connection = new Connection(new Socket(getServerAddress(), getServerPort()));
    private volatile boolean clientConnected;

    public Client() throws IOException {
    }

    /*Nachricht senden an alle*/
    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error beim Senden");
            clientConnected = false;
        }
    }

    /*Run Methode für die Client Verbindung per Sockets*/
    public void run() {
        SocketThread socketThread = getSocketThread();
        // thread ist daemon
        socketThread.setDaemon(true);
        socketThread.run();

        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("Fehler ");
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

    /*Getter Methoden für IP, Port und Name - Vorerst aber fest definiert bei "127.0.0.1" und 500*/
    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Server IP:");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        ConsoleHelper.writeMessage("Server Port:");
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        ConsoleHelper.writeMessage("Your name:");
        return ConsoleHelper.readString();
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

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

        /*client Handshake um die Nachrichten zu synchronisieren*/
        protected void clientHandshake() throws IOException, ClassNotFoundException {
            // TODO maybe make it smarter? eliminate busy waiting -> synchronize block rather than while.
            String name = null;
            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.NAME_REQUEST) { // ask the name
                    if (name == null || !name.equals(getUserName())) {
                        name = getUserName();
                        connection.send(new Message(MessageType.USER_NAME, name));
                        this.notifyConnectionStatusChanged(false);
                    }
                    /*TBD neue Funktion um direkte Nachrichten zu senden*/
//                    name = getUserName();
//                    connection.send(new Message(MessageType.USER_NAME, name));
//                    this.notifyConnectionStatusChanged(false);

                } else if (message.getType() == MessageType.NAME_ACCEPTED) { // server accepted the name
                    notifyConnectionStatusChanged(true);
                    this.notifyConnectionStatusChanged(true);
                    return;

                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        /*Verwaltung der Art von Informationen die über den Server laufen*/
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
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        /* Weitergabe der Nachricht*/
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        /*Begrüßung bei Anmeldung */
        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage("Welcome " + userName + "!");
        }

        /*Benachrichtigung über Abmeldung */
        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("Username  '" + userName + "' left.");
        }

        /*Benachrichtigung über Anmeldung */
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }
}
