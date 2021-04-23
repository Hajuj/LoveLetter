import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;


public class MyChatClientApp extends Application implements Observer {
    private ArrayList<Thread> threads;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        for(Thread thread: threads){
            thread.interrupt();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        threads = new ArrayList<Thread>();
        //Creating Chat Title
        primaryStage.setTitle("Love Letter");
        primaryStage.setScene(makeInitScene(primaryStage));
        primaryStage.show();
    }

    private Scene makeInitScene(Stage primaryStage){
        //Creating the root Pane and set Properties
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.CENTER);

        //Creating a Text Fields and set Properties
        TextField nickNameField = new TextField();
        TextField portField = new TextField();

        //Creating a Text and set Properties
        Text text = new Text("Nickname");
        text.setStyle("-fx-font: normal Bold 15px 'Edwardian Script ITC' ");
        text.setFill(Color.BLANCHEDALMOND);

        Text text1 = new Text("Welcome to LoveLatter Chat");
        text1.setFont(Font.font( "" , FontPosture.ITALIC, 20));
        text1.setFill(Color.BLANCHEDALMOND);


        Text portText = new Text("Port Number");
        portText.setStyle("-fx-font: normal Bold 15px 'Edwardian Script ITC' ");
        portText.setFill(Color.BLANCHEDALMOND);

        Text errorText = new Text();

        //Creating a Button and its handler
        Button submitClientInfoButton = new Button("Done");
        submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    MyChatClient client = new MyChatClient(nickNameField.getText(), Integer.parseInt(portField.getText());
                    Thread UserThread = new Thread(String.valueOf(client));
                    UserThread.setDaemon(true);
                    //client.addObserver( );

                    UserThread.start();
                    threads.add(UserThread);
                    //Changing the scene of the PrimaryStage
                    primaryStage.close();
                    primaryStage.setScene(makeChatUI(client));
                    primaryStage.show();
                } catch (NumberFormatException e) {
                    errorText.setText("Try again, Invalid Port Number");
                    errorText.setFill(Color.WHITESMOKE);
                }
            }
        });
        //Adding the Components to the root pane arguments
        rootPane.add(text, 0, 20);
        rootPane.add(nickNameField, 1,20);
        rootPane.add(submitClientInfoButton, 1,24);
        rootPane.add(portText,0,22);
        rootPane.add(portField,1,22);
        rootPane.add(text1, 1, 0);
        rootPane.setStyle("-fx-background-color: rgb(178,34,34); ");
        rootPane.add(errorText,1,26);

        return new Scene(rootPane, 400,400 );

    }

    public Scene makeChatUI(MyChatClient client){
        //Creating the root pane and set Properties
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(5);
        rootPane.setVgap(5);

        //Creating the chat's listview and set it#s source to the Client's chatLog ArrayList
        ListView<String> chatListView = new ListView<String>();
        chatListView.setItems(client.chatLog);

        //Creating chat textbox and send a message to the server
        TextField chatTextField = new TextField();
        chatTextField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                UserThread.sendMessage(chatTextField.getText());
                chatTextField.clear();
            }
        });

        /* Add the components to the root pane */
        rootPane.add(chatListView, 0, 0);
        rootPane.add(chatTextField, 0, 1);

        return new Scene(rootPane, 400, 400);
    }

    @Override
    public void update(Observable o, Object arg) {
        return;
    }
}
