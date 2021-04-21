import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
/*
public class myClientApplication2 extends Application {

    public ArrayList<Thread> threads = new ArrayList<>();
    //private Object MyChatServer;


    //wenn Programm Parameter enthält;
    //führt erst init() dann start(...) aus;
    //wartet dann auf das Programmende und führt stop () aus;
    //sollte nicht überschrieben werden;
    public static void main(String[] args) {
        launch(args);
    }

    public static Scene scene;

    @Override
    public void stop() throws Exception {
        // Auto-generated method stub
        super.stop();
        for (Thread thread: threads){
            thread.interrupt();
        }
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(myClientApplication2.class.getResource("Login.fxml"));
        scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("LoveLetters");
        primaryStage.setScene(scene);
        scene.getStylesheets().add("Login.css");
        primaryStage.show();



        /* try {

            primaryStage.setTitle("LoveLetters");

            threads = new ArrayList<Thread>();

            //Load Model: MyChatServer.java
            MyChatServer model = new MyChatServer();

            //Load FXML-Datei: Login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            //Load Controller: Login_Controller.java & deklarieren
            Login_Controller controller;
            controller = new Login_Controller(model);
            loader.setController(controller);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("LoveLetters");
            //primaryStage.setX(10);
            primaryStage.show();

        } /*catch(Exception e) {
            e.printStackTrace();
        }
    }
            */
