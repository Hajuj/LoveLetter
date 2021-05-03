package Chat;

import Server.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

// TODO 1. Check the window not completely closing after clicking on the x.
// TODO 2. Check the users connected not showing, after writing a false name more than once.
// TODO 3. Check the notifyConnectionStatusChanged() (line 134 here) method again to fix the name and welcome problem.
// TODO 4. Fix message / error not showing, After trying to send a direct message but the name is written false.
// TODO 5. Change the error message, when writing only '@' in chat.

/**
 * The type Chat.Client GUI Controller.
 */
public class ClientGuiController extends Client {
    private ClientGuiModel model = new ClientGuiModel();

    private String userName;

    @FXML
    private TextArea messages;
    @FXML
    private Button loginButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea users;
    @FXML
    private Label yourNameLabel;
    @FXML
    private Label usersOnlineLabel;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label chatLabel;

    /**
     * Instantiates a new Chat.Client.
     *
     * @throws IOException the IO exception
     */
    public ClientGuiController() throws IOException {
    }


    /**
     * Instantiates a new Chat.Client GUI Controller.
     */
    /*Konstruktor für GUI Controller*/
    public void initialize() {

        messageField.setDisable(true);
        messages.setEditable(false);
        users.setEditable(false);

    }

    /**
     * Refresh messages.
     */
    public synchronized void refreshMessages() {
        Platform.runLater(() -> messages.appendText(getModel().getNewMessage() + "\n"));
    }

    /**
     * Send message button.
     *
     * @param event the event
     */
    @FXML
    public void sendMessageButton(ActionEvent event) {
        try {
            connection.send(new Message(MessageType.TEXT, messageField.getText()));
            if (messageField.getText().equals("bye")) {
                Platform.exit();
                System.exit(0);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Platform.runLater(() -> messageField.clear());
    }


    /**
     * Login button.
     *
     * @param event the event
     */
    @FXML
    public void loginButton(ActionEvent event) {
        run();
        userName = nameField.getText();
        try {
            connection.send(new Message(MessageType.USER_NAME, nameField.getText()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * Refresh users.
     */
    /*Funktion um die Benutzer kontinuierlich zu aktualisieren*/
    public synchronized void refreshUsers() {
        StringBuilder sb = new StringBuilder();
        for (String userName : getModel().getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        Platform.runLater(() -> users.setText(sb.toString()));
    }

    /**
     * Notify connection status changed.
     *
     * @param clientConnected the client connected
     */
    /*Funktion um zu überprüfen ob die Verbindung weiterhin besteht*/
    public synchronized void notifyConnectionStatusChanged(boolean clientConnected) {
        if (clientConnected) {
            messageField.setDisable(false);
            Platform.runLater(() -> errorLabel.setText("You are connected!"));
            Platform.runLater(() -> errorLabel.setTextFill(Color.rgb(0, 139, 0)));
            Platform.runLater(() -> nameField.setDisable(true));
        } else {
            Platform.runLater(() -> errorLabel.setText("Please use another name!"));
            Platform.runLater(() -> errorLabel.setTextFill(Color.rgb(255, 0, 0)));
        }

    }


    /**
     * Gets user name.
     *
     * @return the user name
     */
    /*Getter für Username*/
    public String getUserName() {
        return userName;
    }


    /*run Methode für Thread*/
    public void run() {
        SocketThread socketThread = new GuiSocketThread();
        socketThread.start();
    }

    /*Getter Methode für Socketthread*/
    @Override
    protected SocketThread getSocketThread() {
        return new GuiSocketThread();
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    /*Getter Methode für Model*/
    public ClientGuiModel getModel() {
        return model;
    }

    /*Getter Methode für Serverport - fixer Wert von 127.0.0.1*/
    @Override
    protected String getServerAddress() {
        return "127.0.0.1";
    }

    /*Getter Methode für Serverport - fixer Wert von 500*/
    @Override
    protected int getServerPort() {
        return 500;
    }


    /*Aufruf der Chat.Client Methode zum Versenden der Nachricht*/
    @Override
    protected void sendTextMessage(String text) {
        super.sendTextMessage(text);
    }

    /**
     * The type GUI Socket Thread.
     */
    public class GuiSocketThread extends SocketThread {

        /*Verarbeiten der eingehenden Nachricht*/
        @Override
        protected void processIncomingMessage(String message) {
            model.setNewMessage(message);
            refreshMessages();
        }

        /*Mitteilung über neuen Nutzer*/
        @Override
        protected void informAboutAddingNewUser(String userName) {
            model.addUser(userName);
            refreshUsers();
        }

        /*Aktualisieren der Nutzer Liste*/
        @Override
        protected void informAboutDeletingNewUser(String userName) {
            model.deleteUser(userName);
            refreshUsers();
        }

        /*Mitteilung falls eine Verbindung zum Server.Server sich geändert hat*/
        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            super.notifyConnectionStatusChanged(clientConnected);
            ClientGuiController.this.notifyConnectionStatusChanged(clientConnected);

        }
    }
}
