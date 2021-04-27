package chat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * The type chat.Client gui controller.
 */
public class ClientGuiController extends Client {
    private ClientGuiModel model = new ClientGuiModel();
    private ClientApplication application;

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


    /**
     * Instantiates a new chat.Client gui controller.
     *
     * @param application the application
     * @throws IOException the io exception
     */
    /*Konstruktor für GUI Controller*/
    public ClientGuiController(ClientApplication application) throws IOException {
        this.application = application;

        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
        stage.setTitle("LoveLetter Chat Login");
        stage.setScene(new Scene(root, 600, 275));
        stage.show();

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


    @FXML
    public void closeWindow(WindowEvent windowEvent) {
        Platform.exit();
        System.exit(0);
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
            // TODO check the notify method again to fix the name and welcome problem.
            messageField.setDisable(false);
            Platform.runLater(() -> errorLabel.setText("You are connected!"));
            Platform.runLater(() -> nameField.setDisable(true));
        } else {
            Platform.runLater(() -> errorLabel.setText("Please use another name"));
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
        Client.SocketThread socketThread = new GuiSocketThread();
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


    /*Aufruf der chat.Client Methode zum Versenden der Nachricht*/
    @Override
    protected void sendTextMessage(String text) {
        super.sendTextMessage(text);
    }

    /**
     * The type Gui socket thread.
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

        /*Mitteilung falls eine Verbindung zum chat.Server sich geändert hat*/
        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            ClientGuiController.this.notifyConnectionStatusChanged(clientConnected);

        }
    }
}
