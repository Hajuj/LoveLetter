import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;

public class ClientApplication extends Application implements EventHandler {
    private String userName = new String();
    private TextArea messages = new TextArea();
    private Button button = new Button("Login!");
    private TextField textField = new TextField();
    private ClientGuiController controller;
    private TextArea users = new TextArea();
    private Label yourNameLabel = new Label("Your name:");
    private Label usersOnlineLabel = new Label("Users online:");
    private TextField messageField = new TextField();
    private Button sendButton = new Button("Send!");
    private Label errorLabel = new Label();
    private Stage primaryStage = new Stage();


    public String getUserName () {
        return userName;
    }


    public static void main (String[] args) {
        launch(args);
    }

    public synchronized void refreshMessages () {
        Platform.runLater(() -> messages.appendText(controller.getModel().getNewMessage() + "\n"));
    }

    public synchronized void refreshUsers () {
        StringBuilder sb = new StringBuilder();
        for (String userName : controller.getModel().getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        Platform.runLater(() -> users.setText(sb.toString()));
    }

    public synchronized void notifyConnectionStatusChanged (boolean clientConnected) {
        if (clientConnected) {
            messageField.setDisable(!clientConnected);
            Platform.runLater(()->  errorLabel.setText("You are connected!"));
            Platform.runLater(() -> textField.setDisable(true));
        } else {
            Platform.runLater(()-> errorLabel.setText("Please use another name"));
        }

    }

    public Stage getPrimaryStage () {
        return primaryStage;
    }

    @Override
    public void start (Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("LoveLetter Chat");

        controller  = new ClientGuiController(this);

        button.setOnAction(this);
        sendButton.setOnAction(this);

        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(30));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(10);
        rootPane.setVgap(10);

        rootPane.setStyle("-fx-background-color: darkred");

        rootPane.add(button,2, 3);
        rootPane.add(users, 1, 4);
        rootPane.add(errorLabel,1, 2);
        rootPane.add(textField, 1, 3);
        rootPane.add(messages, 1, 0);
        rootPane.add(yourNameLabel, 0, 3);
        rootPane.add(usersOnlineLabel, 0, 4);
        rootPane.add(messageField, 1, 1);
        rootPane.add(sendButton, 2, 1);



        usersOnlineLabel.setFont(Font.font("Vordana", 18));
        yourNameLabel.setFont(Font.font("Vordana", 18));
        errorLabel.setFont(Font.font("Vordana", 18));

        usersOnlineLabel.setTextFill(Color.WHITE);
        yourNameLabel.setTextFill(Color.WHITE);
        errorLabel.setTextFill(Color.WHITE);


        messageField.setDisable(true);
        messages.setEditable(false);
        users.setDisable(true);

        messages.setStyle("-fx-opacity: 0.5;");
        users.setStyle("-fx-opacity: 0.5;");

        messages.setFont(Font.font("Vordana", 15));
        users.setFont(Font.font("Vordana", 18));
        Scene scene = new Scene(rootPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle (WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    @Override
    public void handle (Event event) {
        if (event.getSource() == button) {
            controller.run();
            userName = textField.getText();
            try {
                controller.connection.send(new Message(MessageType.USER_NAME, textField.getText()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        if (event.getSource() == sendButton){

            try {
                controller.connection.send(new Message(MessageType.TEXT, messageField.getText()));
                if (messageField.getText().equals("bye")){
                    Platform.exit();
                    System.exit(0);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Platform.runLater(() -> messageField.clear());
        }
    }
}
