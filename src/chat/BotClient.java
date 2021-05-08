package chat;

import game.Game;
import game.Player;
import game.PlayerList;
import server.ConsoleHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Bot client.
 */
public class BotClient extends Client {
    private final static ArrayList<String> waitingList = new ArrayList<>();
    private final Game currentGame;
    private final PlayerList listOfPlayers = new PlayerList(this);
    private final Map<Player, Integer> currentCards = new ConcurrentHashMap<>();
    private final Map<Player, String> currentOpponent = new ConcurrentHashMap<>();
    private final int numberOfPlayers = 2;
    private boolean gameOn = false;


    /**
     * Instantiates a new chat.Client.
     *
     * @throws IOException the io exception
     */
    public BotClient() throws IOException {
        this.currentGame = new Game();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        Client client = new BotClient();
        client.run();
    }

    /**
     * Start the game boolean.
     *
     * @param newPlayer the new player
     */
    protected void startTheGame(String newPlayer) {
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
                for (Player p : listOfPlayers.getPlayers()) {
                    currentCards.put(p, 10);
                }
                for (Player p : listOfPlayers.getPlayers()) {
                    currentOpponent.put(p, p.getName());
                }
                // currentOpponent = "";

                currentGame.setPlayers(listOfPlayers);
                currentGame.setBotClient(this);
                waitingList.clear();
//                currentGame.start();

                // sadThread because it toke us a long time to make him happy :(
                Thread sadThread = new Thread(currentGame);
                sadThread.start();
            }
        }
    }

    /**
     * Send to all players.
     *
     * @param message the message
     */
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
        // because we love you Thomas <3
        return "bot";
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    /**
     * Gets current cards.
     *
     * @return the current cards
     */
    public Map<Player, Integer> getCurrentCards() {
        return currentCards;
    }

    /**
     * Gets current opponent.
     *
     * @return the current opponent
     */
    public Map<Player, String> getCurrentOpponent() {
        return currentOpponent;
    }

    /**
     * The type Bot socket thread.
     */
    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            String hello = "Hi everyone! To start the game send @bot play";
            BotClient.this.sendTextMessage(hello);
            super.clientMainLoop();
        }


        @Override
        protected void processIncomingMessage(String message) {
            // alles in die console
            ConsoleHelper.writeMessage(message);

            // split name from message
            message = message.replaceAll("\\s+", "");
            String userNameDelimiter = "toyou:";
            String[] split = message.split(userNameDelimiter);
            if (split.length != 2) return;

            String messageWithoutUserName = split[1];

            //   String format = null;
            switch (messageWithoutUserName) {
                case "play":
                    startTheGame(split[0]);
                    break;
                case "score":
                    listOfPlayers.getCurrentScore();
                    break;
                case "start":
                    //TODO Implement start check with notify or ask for number of players
                    gameOn = true;
                    break;
                case "1", "2", "3", "4", "5", "6", "7", "8": {
                    if (listOfPlayers.checkForUser(split[0])) {
                        currentCards.replace(listOfPlayers.getPlayer(split[0]), Integer.parseInt(messageWithoutUserName));
                        synchronized (currentCards) {
                            currentCards.notify();
                        }
                    }
                    break;
                }
                default:
                    if (listOfPlayers.checkForUser(split[0]) /*&& listOfPlayers.checkForUser(messageWithoutUserName)*/) {
                        currentOpponent.replace(listOfPlayers.getPlayer(split[0]), messageWithoutUserName);
                        synchronized (currentOpponent) {
                            currentOpponent.notify();
                        }
                    }
            }
        }

    }

}
