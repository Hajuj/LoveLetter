import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Login_Controller {


    public TextField username;

    public Button btn_signIn;

    public Label serverReturn;

    public void initialize() {
        username.clear();
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        MyChatServer chatServer;


        /*btn_signIn.setDisable(true);
        String text = username.getText();*/

        /*if (text.equals("")) {
            btn_signIn.setText("Well... If you want me to say nothing, then that's the way it is.");
            username.setDisable(true);
            return;
        } else if (!Model.getInstance().waseverSaid(text)) {
            btn_signIn.setText("Ok.. here you go: " + text);
        } else {
            btn_signIn.setText("Nah! I already said that!");
        }
        username.clear();

        btn_signIn.setDisable(false);
    }*/


    /*
    //private final MyChatServer chatServer;

    public Login_Controller(MyChatServer chatServer){
        this.chatServer = chatServer;
    }

    @FXML private javafx.scene.control.TextField username;

    @FXML protected void btnSignIn() {
        //Schleife: Rückmeldung von Server

        //Beim Senden soll der Nickname im MyChatServer überprüft und gespeichert werden
        chatServer.addUsername(username.getText());

        //Fehlerrückmeldung

    }
*//*
    @Override
    public void initialize(URL location, ResourceBundle resources) {


    } */

    }


    @Override
    public void start(Stage stage) throws Exception {

    }
}
