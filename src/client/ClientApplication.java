import javafx.application.Application;
import javafx.application.Platform;
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

/*
Bevor die ClientApplication gestartet werden kann, muss der Server sich verbinden (Server Klasse)
*/
public class ClientApplication extends Application implements EventHandler {
    private String userName = new String();
    private TextArea messages = new TextArea();
    private Button loginButton = new Button("Login!");
    private TextField nameField = new TextField();
    private ClientGuiController controller;
    private TextArea users = new TextArea();
    private Label yourNameLabel = new Label("Your name:");
    private Label usersOnlineLabel = new Label("Users online:");
    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send!");
    private Label errorLabel = new Label();
    private Stage primaryStage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    /*Funktion um den Chat Verlauf kontinuierlich zu aktualisieren*/
    public synchronized void refreshMessages() {
        Platform.runLater(() -> messages.appendText(controller.getModel().getNewMessage() + "\n"));
    }

    /*Funktion um die Benutzer kontinuierlich zu aktualisieren*/
    public synchronized void refreshUsers() {
        StringBuilder sb = new StringBuilder();
        for (String userName : controller.getModel().getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        Platform.runLater(() -> users.setText(sb.toString()));
    }

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


    /*Design der Stage inklusive der Platzierung aller Elemente*/
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("LoveLetter Chat");

        controller = new ClientGuiController(this);

        loginButton.setOnAction(this);
        sendButton.setOnAction(this);

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

    /*Event Handler für die unterschiedlichen Action Events (bisher 2 Buttons)*/
    @Override
    public void handle(Event event) {
        if (event.getSource() == loginButton) {
            controller.run();
            userName = nameField.getText();
            try {
                controller.connection.send(new Message(MessageType.USER_NAME, nameField.getText()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        if (event.getSource() == sendButton) {

            try {
                controller.connection.send(new Message(MessageType.TEXT, messageField.getText()));
                if (messageField.getText().equals("bye")) {
                    Platform.exit();
                    System.exit(0);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Platform.runLater(() -> messageField.clear());
        }
    }

    /*Getter für Username*/
    public String getUserName() {
        return userName;
    }


    /*Getter und Setter für die PrimaryStage --> nötig für Scene Wechsel*/
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}