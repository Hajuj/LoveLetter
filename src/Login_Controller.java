import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login_Controller{
    private MyChatClient myChatClient;
    private MyChatServer myChatServer;

    @FXML private TextField userNameID;
    @FXML private Button btnSignIn;
    @FXML private Text errorTextID;

    public void initialize() {
        userNameID.clear();
    }

     public void handleBtnSign (ActionEvent event) throws IOException {
        this.myChatClient = myChatClient;
        this.myChatServer = myChatServer;
        myChatClient.setUserName(userNameID.getText());
        String error = "This username is already given!";

        while (true) {
            if (userNameID.getText().isEmpty()) {
                errorTextID.setText("Es wurde kein Nutzername eingegeben!");
            } else if (error == myChatServer.directMessage()) {
                errorTextID.setText("");
            }
        }
        Parent chatViewParent = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Scene chatViewScene = new Scene(chatViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    }