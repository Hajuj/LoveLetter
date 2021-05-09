package server;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * The type Server.Connection.
 *
 * @author Jonas, Mohamad
 */
public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    /**
     * Starts a new Server.Connection.
     *
     * @param socket the socket
     * @throws IOException the IO exception
     */

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Send method for the messages.
     *
     * @param message the message
     * @throws IOException the IO exception
     */

    public void send(Message message) throws IOException {
        synchronized (out) {
            out.writeObject(message);
        }
    }

    /**
     * Receive method for the messages.
     *
     * @return the message
     * @throws IOException            the IO exception
     * @throws ClassNotFoundException the class not found exception
     */

    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (in) {

            try {
                Thread.sleep(111);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (Message) in.readObject();

        }
    }

    /**
     * Closes the input and output.
     * @throws IOException
     */
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
