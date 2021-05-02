package Server;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * The type Server.Connection.
 */
public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    /**
     * Instantiates a new Server.Connection.
     *
     * @param socket the socket
     * @throws IOException the IO exception
     */
    /*Verbindung der Sockets*/
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Send.
     *
     * @param message the message
     * @throws IOException the IO exception
     */
    /*Senden der Nachricht*/
    public void send(Message message) throws IOException {
        synchronized (out) {
            out.writeObject(message);
        }
    }

    /**
     * Receive message.
     *
     * @return the message
     * @throws IOException            the IO exception
     * @throws ClassNotFoundException the class not found exception
     */
    /*Empfangen der Nachricht*/
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (in) {
            return (Message) in.readObject();
        }
    }

    /*Trennen der Verbindung*/
    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    /**
     * Gets remote socket address.
     *
     * @return the remote socket address
     */
    /*Getter der Socket Adresse*/
    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }
}
