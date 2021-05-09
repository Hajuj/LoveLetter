package chat;

import game.Game;
import game.Player;
import game.PlayerList;
import server.ConsoleHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO Add in README FILE: No Lying implemented - all actions are automatically executed if necessary
//Hinweis: Doch keine JAR erstellen -- Aber optional möglich //final date 11:59
// Vortrag - vorstellen und Vorzüge ausarbeiten
// Pro Kontra der letzten drei Projektwochen

// TODo Autoren in JavaDocs nennen --> Experten für den Code


/**
 * The type Bot client.
 */
public class BotClient extends Client {
    private final static ArrayList<String> waitingList = new ArrayList<>();
    private final Game currentGame;
    private final PlayerList listOfPlayers = new PlayerList(this);
    private final Map<Player, Integer> currentCards = new ConcurrentHashMap<>();
    private final Map<Player, String> currentOpponent = new ConcurrentHashMap<>();
    private final int maxNumberOfPlayers = 4;
    // TODO Maxnumber = 4

    private int numberOfPlayers;
    public int loveLetters;

    public int getLoveLetters() {
        return loveLetters;
    }

    public void setLoveLetters(int loveLetters) {
        this.loveLetters = loveLetters;
    }

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
            this.sendTextMessage("@" + newPlayer + " you are in the wait list");
        }

        if (waitingList.size()>1) {
        for (String u : waitingList) {
            this.sendTextMessage("@" + u + " If you do not want to wait for more players, write @bot start");
        }}
    }


    // TODO Re-Format
    protected void printHelpMessage(String newPlayer){
        String helpMessage = "Hi " + newPlayer + "! I'm a LoveLetter Bot!" + "I can: \n";
        helpMessage += "@bot play   -  play the LoveLetters game \n";
        helpMessage += "@bot start   -  start the LoveLetters game \n";
        helpMessage += "@bot score   -  actual LoveLetters score \n";
        helpMessage += "@bot help   -  to see all the commands I can \n";
        this.sendTextMessage("@" + newPlayer + " " + helpMessage);
    }

    protected void startTheAction(String newPlayer){
        if (waitingList.size() < 2 || gameOn) {
            this.sendTextMessage("@" + newPlayer + " please wait for other players");
        } else {
            numberOfPlayers = waitingList.size();
            // Max 4 Players
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

            //loveLetters switch case
            switch (numberOfPlayers) {
                case 2 -> setLoveLetters(7);
                case 3 -> setLoveLetters(5);
                case 4 -> setLoveLetters(4);
            }

            currentGame.setPlayers(listOfPlayers);
            currentGame.setBotClient(this);
            waitingList.clear();
            gameOn = true;

            // sadThread because it toke us a long time to make him happy :(
            Thread sadThread = new Thread(currentGame);
            sadThread.start();
        }
    }

    public void stop() {
        System.exit(0);
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
                case "help":
                    printHelpMessage(split[0]);
                    break;
                case "play":
                    startTheGame(split[0]);
                    break;
                case "score":
                    listOfPlayers.getCurrentScore(listOfPlayers.getCurrentPlayer());
                    break;
                case "start":
                    startTheAction(split[0]);

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