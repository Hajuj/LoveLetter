package chat;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The type chat.Client application.
 */
/*
Bevor die chat.ClientApplication gestartet werden kann, muss der chat.Server sich verbinden (chat.Server Klasse)
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
