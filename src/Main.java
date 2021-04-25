import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

public class Main extends Application {
    private ArrayList<Thread> threads;

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        super.stop();
        for (Thread thread: threads){
            thread.interrupt();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Text-Feld für Status-Nachrichten
        final Text msg = new Text();

        threads = new ArrayList<Thread>();


        try {
            primaryStage.setTitle("LoveLetter Chat");

            //Alle Elemente in einem GridPane (zentral)
            GridPane rootPane = new GridPane();
            rootPane.setPadding(new Insets(20));
            rootPane.setVgap(10);
            rootPane.setHgap(10);
            rootPane.setAlignment(Pos.CENTER);

            //Szene für Login
            Scene sceneLogin = new Scene(rootPane);

            //Szene für die Bühne holen
            primaryStage.setScene(sceneLogin);

            //TextField erstellt
            TextField nameField = new TextField();

            //Label erstellt
            Label nameLabel = new Label("Nickname: ");
            Label errorLabel = new Label();

            //Style
            nameLabel.setStyle("-fx-font-size: 20px");

            //Button mit Handler
            Button submitClientInfoButton = new Button("Login");
            submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent Event) {
                    // TODO Auto-generated method stub
                    /* Instantiate the client class and start it's thread */
                    MyChatClient client;
                    try {
                        client = new MyChatClient("127.0.0.1", 47329);
                        Thread clientThread = new Thread((Runnable) client);
                        clientThread.setDaemon(true);
                        clientThread.start();
                        threads.add(clientThread);

                        //primaryStage: Szene ändern
                        primaryStage.close();
                        primaryStage.setScene(new Scene(new ChatScreen()));
                        primaryStage.setTitle("LoveLetters Chat");
                        primaryStage.show();
                    }
                    catch(ConnectException e){
                        errorLabel.setTextFill(Color.RED);
                        errorLabel.setText("Invalid host name, try again");
                    }
                    catch ( IOException e) {
                        // TODO Auto-generated catch block
                        errorLabel.setTextFill(Color.RED);
                        errorLabel.setText("Invalid port number, try again");
                    }
                }
            });

            //Alle Elemente ins Grid
            rootPane.add(nameField, 0, 0);
            rootPane.add(nameLabel, 1, 0);
            rootPane.add(submitClientInfoButton, 0, 3, 2, 1);
            rootPane.add(errorLabel, 0, 4);

            // Button zum Grid hinzufügen
            rootPane.add(submitClientInfoButton, 1, 3);


            // Die Größe der Bühne auf die Größe der Szene mappen
            primaryStage.sizeToScene();

            // primaryStage erstellen
            primaryStage.show();

            /* Szene erstellen und zurückgeben
            return new Scene(rootPane, 400, 400); */


    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main-Methode für die Ausführbarkeit ohne JavaFX Launcher
    public static void main(String[] args) {
        launch(args);
    }
}

