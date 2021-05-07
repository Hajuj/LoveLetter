package chat;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * The type Chat.Client GUI Model.
 */
public class ClientGuiModel {
    private final Set<String> allUserNames = new TreeSet<>();
    private String newMessage;

    /**
     * Gets all user names.
     *
     * @return all user names
     */
    /*Getter für Usernames*/
    public Set<String> getAllUserNames() {
        return Collections.unmodifiableSet(allUserNames);
    }

    /**
     * Gets new message.
     *
     * @return the new message
     */
    /*Getter für Nachrichten*/
    public String getNewMessage() {
        return newMessage;
    }

    /**
     * Sets new message.
     *
     * @param newMessage the new message
     */
    /*Setter für Nachrichten*/
    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    /**
     * Add user.
     *
     * @param newUserName the new user name
     */
    /*Adder für Usernames*/
    public void addUser(String newUserName) {
        allUserNames.add(newUserName);
    }

    /**
     * Delete user.
     *
     * @param userName the user name
     */
    /*Reset für Usernames*/
    public void deleteUser(String userName) {
        allUserNames.remove(userName);
    }
}
