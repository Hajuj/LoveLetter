import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Login_ControllerView {


    @FXML private Button btnSignIn;


    public void handleBtnSign (ActionEvent event) throws Exception {
        Stage primaryStage;
        Parent root;

        primaryStage = (Stage) btnSignIn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Chat.fxml"));

        Scene sceneChat = new Scene(root);
        primaryStage.setScene(sceneChat);
        primaryStage.show();

        }

        /*FXMLLoader loaderChat = new FXMLLoader();
        loaderChat.setLocation(getClass().getResource("Chat.fxml"));
        Parent chatViewParent = loaderChat.load();

        Scene chatViewScene*/

        /*Parent chatViewParent = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Scene chatViewScene = new Scene(chatViewParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(chatViewScene);
        window.show();*/

/*
    //Initializes the controller class.

    @Override
    public void initialize (URL url, ResourceBundle rb){
        // TODO
    } */
}


