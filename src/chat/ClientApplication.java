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
public class ClientApplication extends Application {

    /**
     * starts before the method start - Lifecycle of JavaFX
     */
    @Override
    public void init() {
        System.out.println("init!");
    }

    /**
     * starts before the application will close - Lifecycle of JavaFX
     */
    @Override
    public void stop() {
        System.out.println("Stage is closing");
        System.exit(0);
    }

    /*Design der Stage inklusive der Platzierung aller Elemente*/

    /**
     * Design of the Stage including the set of the scene with fxml-File and CSS-File
     * @param stage the window of the application
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Chat.fxml")));
        stage.setTitle("LoveLetter Chat Login");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("Chat.css")).toString());
        //stage.setScene(new Scene(root, 600, 275));
        stage.setScene(scene);
        stage.show();
        System.out.println("start!");
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
