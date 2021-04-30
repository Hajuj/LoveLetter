import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection = new Connection(new Socket(getServerAddress(), getServerPort()));;
    private volatile boolean clientConnected;

    public Client () throws IOException {
    }

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

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();

                if (message.getType() == MessageType.NAME_REQUEST) { // ask the name

                    String name = getUserName();
                    connection.send(new Message(MessageType.USER_NAME, name));
                    this.notifyConnectionStatusChanged(false);

                } else if (message.getType() == MessageType.NAME_ACCEPTED) { // server accepted the name
                    notifyConnectionStatusChanged(true);
                    this.notifyConnectionStatusChanged(true);
                    return;

                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

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

        protected void processIncomingMessage(String message) {
            // in die console
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage("Welcome " + userName + "!");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("Username  '" + userName + "' left.");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    protected void sendTextMessage(String text) {
        try {


            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error beim Schicken");
            clientConnected = false;
        }
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

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



}
