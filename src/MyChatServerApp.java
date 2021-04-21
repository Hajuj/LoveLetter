
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MyChatServerApp extends Application {
    public static ArrayList<Thread> threads;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        threads = new ArrayList<Thread>();
        primaryStage.setTitle("Chat Server");
        primaryStage.setScene(makePortUI(primaryStage));
        primaryStage.show();
    }

    public Scene makePortUI(Stage primaryStage){
        //Creating the root pane and set Properties
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setVgap(10);
        rootPane.setHgap(10);
        rootPane.setAlignment(Pos.CENTER);
        //Creating Text and field for port Number
        Text portText = new Text("Port Number");
        Text errorText = new Text();
        errorText.setFill(Color.RED);
        TextField portTextField = new TextField();
        portText.setStyle("-fx-font: normal Bold 15px 'Edwardian Script ITC' ");
        portText.setFill(Color.BLACK);

        // "Done" button and its click handler When clicked, another method is called
        Button portDoneButton = new Button("Done");
        portDoneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Creating the server and its thread, and run it
                try {
                    MyChatServer server = new MyChatServer(Integer.parseInt(portTextField.getText()));
                    Thread serverThread = (new Thread(String.valueOf(server)));
                    serverThread.setName("Server Thread");
                    serverThread.setDaemon(true);
                    serverThread.start();
                    threads.add(serverThread);

                    //Changing the view of the primary stage
                    primaryStage.hide();
                    primaryStage.setScene(makeServerUI(server));
                    primaryStage.show();
                } catch(IllegalArgumentException | IOException e){
                    errorText.setText("Invalid Port number");

                }
            }
        });
        //Adding the views to the pane
        rootPane.add(portText, 0,0);
        rootPane.add(portTextField, 0, 1);
        rootPane.add(portDoneButton, 0,2);
        rootPane.add(errorText, 0, 5);


        return new Scene(rootPane, 400, 400);
    }
    public Scene makeServerUI(MyChatServer server){
        //Making the root pane and set properties

        GridPane rootPane = new GridPane();
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setPadding(new Insets(20));
        rootPane.setVgap(10);
        rootPane.setHgap(10);

        //Making the server log ListView
        Label logLabel = new Label("Server Log");
        ListView<String> logView = new ListView<String>();
        ObservableList<String> logList = server.serverLog;
        logView.setItems(logList);

        //Making the client ListView
        Label clientLabel = new Label("Clients Connected");
        ListView<String> clientView = new ListView<String>();
        ObservableList<String> clientList = server.clientNames;
        clientView.setItems(clientList);

        //Adding the view to the pane
        rootPane.add(logLabel, 0,0);
        rootPane.add(logView, 0 ,1);
        rootPane.add(clientLabel, 0,2);
        rootPane.add(clientView, 0 ,3);

        //Making Scene and return it,
        return new Scene(rootPane, 400, 600);
    }

}