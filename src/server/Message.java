import java.io.Serializable;

public class Message implements Serializable {
    private final MessageType type;
    private final String data;

    /*Nachrichten ohne Data String*/
    public Message(MessageType type) {
        this.type = type;
        this.data = null;
    }

    /*Nachrichten mit Data String*/
    public Message(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    /*Getter für Data*/
    public String getData() {
        return data;
    }

    /*Getter für Type*/
    public MessageType getType() {
        return type;
    }
}
