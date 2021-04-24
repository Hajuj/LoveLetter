import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Start extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            //Model wird Subjekt (erbt von Observable)
            MyChatClient myChatClient = new MyChatClient("127.0.01", 47329);

            //View ist nun auch ein Beobachter (implementiert das IFace Observer)
            MyChatClientApp myChatClientApp = new MyChatClientApp(myChatClient);

            //Subjekt dem Beobachter anfügen
            myChatClient.addObserver(myChatClientApp);

            //FXML laden
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

            //Szene erstellen
            Scene scene_ChatClientApp = new Scene(myChatClientApp);


            //Creating Chat Title
            primaryStage.setTitle("Love Letter");
            primaryStage.setScene(scene_ChatClientApp);
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
