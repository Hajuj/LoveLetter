import java.io.IOException;

public class ClientGuiController extends Client {
    private ClientGuiModel model = new ClientGuiModel();
    private ClientApplication application;

    /*Konstruktor für GUI Controller*/
    public ClientGuiController(ClientApplication application) throws IOException {
        super();
        this.application = application;
    }

    /*run Methode für Thread*/
    @Override
    public void run() {
        SocketThread socketThread = new GuiSocketThread();
        socketThread.start();
    }

    /*Getter Methode für Socketthread*/
    @Override
    protected SocketThread getSocketThread() {
        return new GuiSocketThread();
    }

    /*Getter Methode für Model*/
    public ClientGuiModel getModel() {
        return model;
    }

    /*Getter Methode für Serverport - fixer Wert von 127.0.0.1*/
    @Override
    protected String getServerAddress() {
        return "127.0.0.1";
    }

    /*Getter Methode für Serverport - fixer Wert von 500*/
    @Override
    protected int getServerPort() {
        return 500;
    }

    /*Getter Methode für Username*/
    @Override
    protected String getUserName() {
        return application.getUserName();
    }

    /*Aufruf der Client Methode zum Versenden der Nachricht*/
    @Override
    protected void sendTextMessage(String text) {
        super.sendTextMessage(text);
    }

    public class GuiSocketThread extends SocketThread {

        /*Verarbeiten der eingehenden Nachricht*/
        @Override
        protected void processIncomingMessage(String message) {
            model.setNewMessage(message);
            application.refreshMessages();
        }

        /*Mitteilung über neuen Nutzer*/
        @Override
        protected void informAboutAddingNewUser(String userName) {
            model.addUser(userName);
            application.refreshUsers();
        }

        /*Aktualisieren der Nutzer Liste*/
        @Override
        protected void informAboutDeletingNewUser(String userName) {
            model.deleteUser(userName);
            application.refreshUsers();
        }

        /*Mitteilung falls eine Verbindung zum Server sich geändert hat*/
        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            application.notifyConnectionStatusChanged(clientConnected);
        }
    }
}
