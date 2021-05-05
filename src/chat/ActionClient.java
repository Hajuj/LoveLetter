package chat;

import game.Game;
import server.ConsoleHelper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static chat.BotClient.currentGame;

public class ActionClient extends Client {


    /**
     * Instantiates a new chat.Client.
     *
     * @throws IOException the io exception
     */
    public ActionClient() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Client client = new ActionClient();
        client.run();
    }


    @Override
    protected SocketThread getSocketThread() {
        return new ActionSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "action";
    }

    public class ActionSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            String hello = "Hi everyone! To play a move send @action <<action>>";
            ActionClient.this.sendTextMessage(hello);
            super.clientMainLoop();
        }


        // TODO eingaben vom user durch den bot einlesen
        // TODO ignore the spaces after @bot + letter case
        @Override
        protected void processIncomingMessage(String message) {

                ConsoleHelper.writeMessage(message);

                // split name from message
                String commandDelimiter = "to you : ";
                String[] split = message.split(commandDelimiter);
                if (split.length != 2) return;
                AtomicInteger newCommand = new AtomicInteger(Integer.parseInt(split[1]));
                currentGame.setCommandList(newCommand);


            }
        }


}
