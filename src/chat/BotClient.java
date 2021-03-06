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
 *
 * @author Altug, Jonas, Mohamad, Viktoria
 */
public class BotClient extends Client {
    private final static ArrayList<String> waitingList = new ArrayList<>();
    private final Game currentGame;
    private final PlayerList listOfPlayers = new PlayerList(this);
    private final Map<Player, Integer> currentCards = new ConcurrentHashMap<>();
    private final Map<Player, String> currentOpponent = new ConcurrentHashMap<>();

    /**
     * Number of Tokens depending on Player Number
     */
    public int loveLetters;

    /**
     * Returns the loveLetters of a player
     *
     * @return the loveLetters
     */

    public int getLoveLetters() {
        return loveLetters;
    }

    /**
     * loveLetter setter
     * @param loveLetters for the loveLetters
     */

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
        int maxNumberOfPlayers = 4;
        if (waitingList.size()== maxNumberOfPlayers){startTheAction(newPlayer);}
        else if (waitingList.size()>1) {
            for (String u : waitingList) {
                this.sendTextMessage("@" + u + " If you do not want to wait for more players, write @bot start");
            }}

    }

    /**
     * Prints command lines to give the bot commands.
     * @param newPlayer for the new Player
     */

    protected void printHelpMessage(String newPlayer){
        String helpMessage = "\n Hi  " + newPlayer + "!  I'm a LoveLetter Bot! " + " I can: \n";
        helpMessage += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n";
        helpMessage += "# \t"+ "@bot play"+"\t"+ "\t"+"> \t play the LoveLetters game \t \t  #\n";
        helpMessage += "# \t"+ "@bot start"+"\t"+"\t"+ "> \t start the LoveLetters game \t  #\n";
        helpMessage += "# \t"+ "@bot score"+"\t"+"\t"+ "> \t current LoveLetters score \t \t  #\n" ;
        helpMessage += "# \t"+ "@bot help" +"\t"+ "\t"+"> \t see all the commands I can \t  #\n";
        helpMessage += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \n";

        this.sendTextMessage("@" + newPlayer + " " + helpMessage);
    }

    /**
     * Starts all the fun .
     * Puts players in waitingList if there is less than 1.
     * Adds every player to the waiting list.
     * Sets the game conditions according to the player number.
     * @param newPlayer for the new Player
     */

    protected void startTheAction(String newPlayer){
        if (waitingList.size() < 2 || gameOn) {
            this.sendTextMessage("@" + newPlayer + " please wait for other players");
        } else {
            int numberOfPlayers = waitingList.size();
            // Max 4 Players
            for (String s : waitingList) {
                listOfPlayers.addPlayer(s);
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

    /**
     * Attribute gameTie if there is a tie
     */
    public boolean gameTie;

    /**
     * Tie situation setter
     * @param gameTie for the gameTie
     */

    public void setGameTie(boolean gameTie) {
        this.gameTie = gameTie;
    }

    /**
     * getter for the gameTie situation.
     * @return the gameTie
     */

    public boolean getGameTie() {
        return gameTie;
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

    /**
     * Situation of the game going on.
     * @param gameOn for the boolean value
     */

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

        /**
         * Actions of bot commands.
         * Splits the message to receive the command.
         * For the command Help:Prints the commands .
         * For the command Play:Sets up the game.
         * For the command Score:Gives the score.
         * FAor the command Start:Starts the game.
         * @param message the message
         */


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
                    listOfPlayers.getCurrentScore(listOfPlayers.getPlayer(split[0]));
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
                    if (listOfPlayers.checkForUser(split[0]) ) {
                        currentOpponent.replace(listOfPlayers.getPlayer(split[0]), messageWithoutUserName);
                        synchronized (currentOpponent) {
                            currentOpponent.notify();
                        }
                    }
            }
        }

    }

}
