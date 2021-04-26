import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class ClientGuiModel {
    private final Set<String> allUserNames = new TreeSet<>();
    private String newMessage;

    /*Getter für Usernames*/
    public Set<String> getAllUserNames() {
        return Collections.unmodifiableSet(allUserNames);
    }

    /*Getter für Nachrichten*/
    public String getNewMessage() {
        return newMessage;
    }

    /*Setter für Nachrichten*/
    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    /*Adder für Usernames*/
    public void addUser(String newUserName) {
        allUserNames.add(newUserName);
    }

    /*Reset für Usernames*/
    public void deleteUser(String userName) {
        allUserNames.remove(userName);
    }
}
