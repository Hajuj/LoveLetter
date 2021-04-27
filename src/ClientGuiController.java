import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * The type Client gui controller.
 */
public class ClientGuiController extends Client {
    private ClientGuiModel model = new ClientGuiModel();
    private ClientApplication application;
    private String userName;
    private TextArea messages = new TextArea();
    private Button loginButton = new Button("Login!");
    private TextField nameField = new TextField();
    private TextArea users = new TextArea();
    private Label yourNameLabel = new Label("Your name:");
    private Label usersOnlineLabel = new Label("Users online:");
    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send!");
    private Label errorLabel = new Label();
    private Stage primaryStage = new Stage();


    /**
     * Instantiates a new Client gui controller.
     *
     * @param application the application
     * @throws IOException the io exception
     */
    /*Konstruktor für GUI Controller*/
    public ClientGuiController(ClientApplication application) throws IOException {
        super();
        this.application = application;


        Stage primaryStage = new Stage();
        primaryStage.setTitle("LoveLetter Chat");

        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(30));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(10);
        rootPane.setVgap(10);

        rootPane.setStyle("-fx-background-color: #244687");

        usersOnlineLabel.setFont(Font.font("Vordana", 18));
        yourNameLabel.setFont(Font.font("Vordana", 18));
        errorLabel.setFont(Font.font("Vordana", 18));
        messages.setFont(Font.font("Vordana", 15));
        users.setFont(Font.font("Vordana", 18));

        messages.setStyle("-fx-opacity: 1.0;");
        users.setStyle("-fx-opacity: 1.0;");

        usersOnlineLabel.setTextFill(Color.WHITE);
        yourNameLabel.setTextFill(Color.WHITE);
        errorLabel.setTextFill(Color.WHITE);

        messageField.setDisable(true);
        messages.setEditable(false);
        users.setDisable(true);


        rootPane.add(yourNameLabel, 0, 3);
        rootPane.add(nameField, 1, 3);
        rootPane.add(loginButton, 2, 3);

        rootPane.add(errorLabel, 1, 2);


        rootPane.add(usersOnlineLabel, 0, 4);
        rootPane.add(users, 1, 4);

        rootPane.add(messages, 1, 0);
        rootPane.add(messageField, 1, 1);
        rootPane.add(sendButton, 2, 1);


        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                run();
                userName = nameField.getText();
                try {
                    connection.send(new Message(MessageType.USER_NAME, nameField.getText()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
        });



        Scene scene = new Scene(rootPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    public synchronized void refreshMessages() {
        Platform.runLater(() -> messages.appendText(getModel().getNewMessage() + "\n"));
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



    /*Aufruf der Client Methode zum Versenden der Nachricht*/
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

        /*Mitteilung falls eine Verbindung zum Server sich geändert hat*/
        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            notifyConnectionStatusChanged(clientConnected);

        }
    }
}
