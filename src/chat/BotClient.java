
package chat;
import game.Game;
import game.Player;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BotClient extends Client {
    private Game currentGame;
    private static ArrayList<String> currentPlayerList = new ArrayList<>();
    private boolean gameOn = false;
    private final int numberOfPlayers = 4;

    /**
     * Instantiates a new chat.Client.
     *
     * @throws IOException the io exception
     */
    public BotClient () throws IOException {
        this.currentGame = new Game();
    }


    //hier ANZAHL DER PLAYERS
    protected boolean startTheGame(String newPlayer){
        currentPlayerList.add(newPlayer);
        if (currentPlayerList.size() < numberOfPlayers || gameOn){
            currentPlayerList.add(newPlayer);
            BotClient.this.sendTextMessage("@" + newPlayer + " you are in the waitlist");
        } else {
            ArrayList<Player> listOfPlayers = new ArrayList<>();
            for (int i = 0; i < numberOfPlayers; i++) {
                Player player = new Player();
                player.playersID = i;
                player.setPlayerName(newPlayer);
                listOfPlayers.add(player);
            }
            currentGame.setUpTheGame();
        }
        return false;
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



        @Override
        protected void processIncomingMessage(String message) {
            // Выводим текст сообщения в консоль
            ConsoleHelper.writeMessage(message);

            // Отделяем отправителя от текста сообщения
            String userNameDelimiter = ": ";
            String[] split = message.split(userNameDelimiter);
            if (split.length != 2) return;

            String messageWithoutUserName = split[1];

            String format = null;
            switch (messageWithoutUserName) {
                case "play":
                    startTheGame(split[0]);
                    break;

            }
            if (format != null) {
                BotClient.this.sendTextMessage("@" + split[0] + " you startes a game!");
            }
        }
    }
}