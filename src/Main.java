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
    public void start(Stage primaryStage) {

        // Text-Feld für Status-Nachrichten
        final Text msg = new Text();

        // ID wird gesetzt für CSS
        //msg.setId("msg-text");
        threads = new ArrayList<Thread>();
        primaryStage.setTitle("LoveLetter Chat");
        primaryStage.setScene(makeInitScene(primaryStage));
        primaryStage.show();

    }

        public Scene makeInitScene(Stage primaryStage) {
            /* Make the root pane and set properties */

            GridPane rootPane = new GridPane();
            rootPane.setPadding(new Insets(20));
            rootPane.setVgap(10);
            rootPane.setHgap(10);
            rootPane.setAlignment(Pos.CENTER);

            /* Make the text fields and set properties */
            TextField nameField = new TextField();

            /* Make the labels and set properties */
            Label nameLabel = new Label("Name ");
            Label errorLabel = new Label();
            /* Make the button and its handler */
            Button submitClientInfoButton = new Button("Login");
            submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent Event) {
                    // TODO Auto-generated method stub
                    /* Instantiate the client class and start it's thread */
                    MyChatClient client;
                    try {
                        client = new MyChatClient("127.0.0.1", 47329);
                        Thread clientThread = new Thread(client);
                        clientThread.setDaemon(true);
                        clientThread.start();
                        threads.add(clientThread);

                        /* Change the scene of the primaryStage */
                        primaryStage.close();
                        primaryStage.setScene(makeChatUI(client));
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

            rootPane.add(nameField, 0, 0);
            rootPane.add(nameLabel, 1, 0);
            rootPane.add(submitClientInfoButton, 0, 3, 2, 1);
            rootPane.add(errorLabel, 0, 4);
            /* Make the Scene and return it */
            return new Scene(rootPane, 400, 400);


        /*
        try {
            primaryStage.setTitle("LoveLetters");

            // Alles in eine GridPane, die zentral ausgerichtet ist:
            GridPane gp = new GridPane();
            gp.setHgap(10);
            gp.setVgap(10);
            gp.setAlignment(Pos.CENTER);
            gp.setPadding(new Insets(25));

            // Die Szene f�r den Login-Screen
            Scene scene = new Scene(gp);

            // Die Szene auf die B�hne holen
            primaryStage.setScene(scene);

            // Überschrift, Labels und Textfelder generieren:
            Text t_welcome = new Text("Registration!");
            t_welcome.setId("ueberschrift");

            Label l_user = new Label("Nutzername: ");

            TextField tf_user = new TextField();

            // Als Beispiel: Ein Inline-CC-Style setzen:
            // Stellt den spezifischten Selektor dar und somit �berlagert diese Regel die anderen
            l_user.setStyle("-fx-font-size: 20px");

            // Alle Elemente ins Grip packen
            //gp.add(t_welcome, 0, 0, 2, 1);
            gp.add(l_user,0,1);
            gp.add(tf_user, 1, 1);
            gp.add(msg, 1, 4);

            // Den Button definieren:
            Button login_button = new Button("Einloggen");

            // Den Button einen Event Handler anheften (mittels anonymer innerer Klasse):
            login_button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    if(tf_user.getText().equals("")){
                        msg.setText("Es wurde kein Nutzername eingegeben!");
                    }
                    else{
                        //msg.setText("Nutzer eingeloggt!");
                        primaryStage.setScene(new Scene(new ChatScreen()));
                        primaryStage.setTitle("LoveLetters Chat");
                        primaryStage.sizeToScene();
                    }

                }
            });

            // Button zum Grid hinzufügen
            gp.add(login_button, 1, 3);


            // Die Größe der Bühne auf die Größe der Szene mappen
            primaryStage.sizeToScene();

            // Das Ganze wird anzeigt!
            primaryStage.show();*/


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Main-Methode für die Ausführbarkeit ohne JavaFX Launcher
    public static void main(String[] args) {
        launch(args);
    }
}

