package chat;

import game.Game;
import game.Player;
import game.PlayerList;
import server.ConsoleHelper;

import java.io.IOException;
import java.util.ArrayList;

public class BotClient extends Client {
    private final Game currentGame;
    private final ArrayList<String> waitingList = new ArrayList<>();
    private boolean gameOn = false;
    private final PlayerList listOfPlayers = new PlayerList(this);

    /**
     * Instantiates a new chat.Client.
     *
     * @throws IOException the io exception
     */
    public BotClient() throws IOException {
        this.currentGame = new Game();
    }

    public static void main(String[] args) throws IOException {
        Client client = new BotClient();
        client.run();
    }

    //hier ANZAHL DER PLAYERS
    protected void startTheGame(String newPlayer) {
        if (waitingList.contains(newPlayer)) {
            this.sendTextMessage("@" + newPlayer + " you are already in the wait list");
        } else {
            waitingList.add(newPlayer);
            int numberOfPlayers = 2;
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

            /*  Add If Loop: use second escape Character - e.g. "Action" in  << @bot Action 1 >>
                to see whether it is a Command from the Players. Check first to see whether a
                game is already being played. If so append the "1" to commandList.
                Then replace scanner in with commandList (+busy waiting while commandList is empty)
            */
            if (message.contains("to you : Action")) {

                ConsoleHelper.writeMessage(message);

                // split name from message
                String commandDelimiter = "to you : Action";
                String[] split = message.split(commandDelimiter);

                String newCommand = split[1];

                currentGame.setCommandList(Integer.parseInt(newCommand));
            } else {


                // alles in die console
                ConsoleHelper.writeMessage(message);

                // split name from message
                String userNameDelimiter = "to you : ";
                String[] split = message.split(userNameDelimiter);
                if (split.length != 2) return;

                String messageWithoutUserName = split[1];

                if (messageWithoutUserName.equals("play")) {
                    startTheGame(split[0]);
                }

            }
        }

    }
}
