
package chat;
import game.*;
import server.*;

import java.io.IOException;
import java.util.ArrayList;

public class BotClient extends Client {
    private Game currentGame;
    private static ArrayList<String> waitingList = new ArrayList<>();
    private boolean gameOn = false;
    private final int numberOfPlayers = 2;
    private PlayerList listOfPlayers = new PlayerList(this);

    /**
     * Instantiates a new chat.Client.
     *
     * @throws IOException the io exception
     */
    public BotClient () throws IOException {
        this.currentGame = new Game();
    }


    //hier ANZAHL DER PLAYERS
    protected boolean startTheGame(String newPlayer) {
        if (waitingList.contains(newPlayer)) {
            this.sendTextMessage("@" + newPlayer + " you are already in the wait list");
        } else {
            waitingList.add(newPlayer);
            if (waitingList.size() < numberOfPlayers || gameOn) {
                this.sendTextMessage("@" + newPlayer + " you are in the wait list");
            } else {
                for (int i = 0; i < numberOfPlayers; i++) {
                    listOfPlayers.addPlayer(waitingList.get(i));
                }
                currentGame.setPlayers(listOfPlayers);
                currentGame.start(this);
                gameOn = true; // TODO remove all players from the waiting list.
            }
        }
        return true;
    }

    public void sendToAllPlayers(String message) {
        for (Player player : listOfPlayers.getPlayers()) {
            this.sendTextMessage("@" + player.getName() + " " + message);

        }
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "bot";
    }

    public static void main(String[] args) throws IOException {
        Client client = new BotClient();
        client.run();
    }



    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            String hello = "Hi everyone! To start the game send @bot play";
            BotClient.this.sendTextMessage(hello);
            super.clientMainLoop();
        }


        // TODO eingaben vom user durch den bot einlesen
        // TODO ignore the spaces after @bot + letter case
        @Override
        protected void processIncomingMessage(String message) {
            // alles in die console
            ConsoleHelper.writeMessage(message);

            // split name from message
            String userNameDelimiter = "to you : ";
            String[] split = message.split(userNameDelimiter);
            if (split.length != 2) return;

            String messageWithoutUserName = split[1];

            String format = null;
            switch (messageWithoutUserName) {
                case "play":
                    startTheGame(split[0]);
                    break;
            }

        }
    }
}
