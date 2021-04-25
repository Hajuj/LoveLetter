import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class ChatScreen extends Parent {
    private MyChatClient myChatClient;
    private Alert infoAlert;

    public Scene ChatScreen() {

        // Model
        ChatModel cModel = new ChatModel();

        //Controller
        ChatController chatC;

        //GridPane erstellen
        GridPane rootPane = new GridPane();
        rootPane.setPadding(new Insets(20));
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setHgap(5);
        rootPane.setVgap(5);


        //Chat Listview und Source vom Client's chatLog ArrayList setzen
        ListView<String> chatListView = new ListView<String>();
        chatListView.setItems(MyChatClient.chatLog);


        return new Scene(rootPane, 400, 400);


        }

    }




