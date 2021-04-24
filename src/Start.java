import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*Start soll die Login.fxml aufrufen. */

public class Start extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
            /*//Model wird Subjekt (erbt von Observable)
            MyChatClient myChatClient = new MyChatClient("127.0.01", 47329);

            //View ist nun auch ein Beobachter (implementiert das IFace Observer)
            MyChatClientApp myChatClientApp = new MyChatClientApp(myChatClient);

            //Subjekt dem Beobachter anfügen
            myChatClient.addObserver(myChatClientApp);

            //FXML laden
            FXMLLoader login = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = login.load();

            //Szene erstellen
            Scene loginScene = new Scene(root);


            //Creating Chat Title
            primaryStage.setTitle("Love Letter");
            primaryStage.setScene(loginScene);
            primaryStage.show(); */


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
