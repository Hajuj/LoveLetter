package server;

import java.io.Serializable;

/**
 * The type Server.Message.
 *
 * @author Jonas, Viktoria
 */
public class Message implements Serializable {

    /**
     * Instantiates a new type of Message.
     */
    private final MessageType type;

    /**
     * Instantiates a new String of Data.
     */
    private final String data;

    /**
     * Instantiates a new Server.Message.
     *
     * @param type the type
     */
    /*Nachrichten ohne Data String*/
    public Message(MessageType type) {
        this.type = type;
        this.data = null;
    }

    /**
     * Instantiates a new Server.Message.
     *
     * @param type the type
     * @param data the data
     */
    /*Nachrichten mit Data String*/
    public Message(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Getter for data.
     *
     * @return the data
     */
    /*Getter für Data*/
    public String getData() {
        return data;
    }

    /**
     * Getter for type.
     *
     * @return the type
     */
    /*Getter für Type*/
    public MessageType getType() {
        return type;
    }
}
