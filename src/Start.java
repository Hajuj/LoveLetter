import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*Start soll die Login.fxml aufrufen. */

public class Start extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            /*//Model wird Subjekt (erbt von Observable)
            MyChatClient myChatClient = new MyChatClient("127.0.01", 47329);

            //View ist nun auch ein Beobachter (implementiert das IFace Observer)
            MyChatClientApp myChatClientApp = new MyChatClientApp(myChatClient);

            //Subjekt dem Beobachter anfügen
            myChatClient.addObserver(myChatClientApp); */

            //FXML laden
            FXMLLoader login = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = login.load();

            //Szene erstellen
            Scene loginScene = new Scene(root);


            //Creating Chat Title
            primaryStage.setTitle("Love Letter");
            primaryStage.setScene(loginScene);
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
