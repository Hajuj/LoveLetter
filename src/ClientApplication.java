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

/**
 * The type Client application.
 */
/*
Bevor die ClientApplication gestartet werden kann, muss der Server sich verbinden (Server Klasse)
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


    /*Design der Stage inklusive der Platzierung aller Elemente*/
    @Override
    public void start(Stage stage) throws Exception {

        ClientGuiController controller = new ClientGuiController(this);

    }

}
