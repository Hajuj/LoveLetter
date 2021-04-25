import java.io.IOException;

public class ClientGuiController extends Client {
    private ClientGuiModel model = new ClientGuiModel();
    private ClientApplication application;

    public ClientGuiController (ClientApplication application) throws IOException {
        super();
        this.application = application;
    }

    @Override
    public void run () {
        SocketThread socketThread = new GuiSocketThread();

        socketThread.start();

    }

    @Override
    protected SocketThread getSocketThread() {
        return new GuiSocketThread();
    }

    public ClientGuiModel getModel() {
        return model;
    }

    @Override
    protected String getServerAddress() {
        return "127.0.0.1";
    }

    @Override
    protected int getServerPort() {
        return 500;
    }

    @Override
    protected String getUserName() {
        return application.getUserName();
    }


    @Override
    protected void sendTextMessage(String text) {
        super.sendTextMessage(text);
    }

    public class GuiSocketThread extends SocketThread {
        @Override
        protected void processIncomingMessage(String message) {
            // message
            model.setNewMessage(message);
            application.refreshMessages();
        }

        @Override
        protected void informAboutAddingNewUser(String userName) {
            //new user added
            model.addUser(userName);
            application.refreshUsers();
        }

        @Override
        protected void informAboutDeletingNewUser(String userName) {
            //refresh userlist
            model.deleteUser(userName);
            application.refreshUsers();
        }

        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            application.notifyConnectionStatusChanged(clientConnected);
        }
    }
}