import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {
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
        //primaryStage erstellen mit loginScene
        primaryStage.setTitle("Love Letter");
        primaryStage.setScene(loginScene(primaryStage));
        primaryStage.show();
    }

    private Scene loginScene(Stage primaryStage){
        //Creating the root Pane and set Properties
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setVgap(5);
        rootPane.setHgap(5);
        rootPane.setAlignment(Pos.CENTER);

        //Textfelder und Properties setzen
        TextField userName = new TextField();
        TextField portField = new TextField();

        //Text und Properties setzen
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

        //Button mit Handler
        Button submitClientInfoButton = new Button("Done");
        submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                MyChatClient client;
                try{
                    client = new MyChatClient(Integer.parseInt(userName.getText()));
                    Thread UserThread = new Thread(String.valueOf(client));
                    UserThread.setDaemon(true);
                    UserThread.start();
                    threads.add(UserThread);
                    //Changing the scene of the PrimaryStage
                    primaryStage.close();
                    primaryStage.setScene(chatScreen(client));
                    primaryStage.show();
                } catch (NumberFormatException | IOException e) {
                    errorText.setText("Try again, Invalid Port Number");
                    errorText.setFill(Color.WHITESMOKE);
                }
            }
        });
        //Alles in Komponenten einfügen
        rootPane.add(text, 0, 20);
        rootPane.add(userName, 1,20);
        rootPane.add(submitClientInfoButton, 1,24);
        rootPane.add(portText,0,22);
        rootPane.add(portField,1,22);
        rootPane.add(text1, 1, 0);
        rootPane.setStyle("-fx-background-color: rgb(178,34,34); ");
        rootPane.add(errorText,1,26);

        return new Scene(rootPane, 400,400 );

    }

    public Scene chatScreen(MyChatClient client){
        //root Pane kreieren
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(5);
        rootPane.setVgap(5);

        //Creating the chat's listview and set it#s source to the Client's chatLog ArrayList
        ListView<String> chatListView = new ListView<String>();
        chatListView.setItems(MyChatClient.chatLog);
        //chatListView.getChildren().usern_text,



        return new Scene(rootPane, 400, 400);
    }



}

