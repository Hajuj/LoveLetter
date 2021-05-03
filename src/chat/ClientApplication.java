package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The type Chat.Client Application.
 */
/*
Bevor die Chat.ClientApplication gestartet werden kann, muss der Server.Server sich verbinden (Server.Server Klasse)
*/
public class ClientApplication extends Application {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        System.exit(0);
    }

    /*Design der Stage inklusive der Platzierung aller Elemente*/
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
        stage.setTitle("LoveLetter Chat Login");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Chat.css").toString());
        //stage.setScene(new Scene(root, 600, 275));
        stage.setScene(scene);
        stage.show();
    }




}
