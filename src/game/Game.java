package game;

import cards.Card;
import cards.Deck;
import chat.BotClient;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions {

    public final CountDownLatch latch = new CountDownLatch(1);


    public AtomicInteger commandList;


    /**
     * The list of players in the game.
     */
    private PlayerList players;
    /**
     * The deck of cards.
     */
    private final Deck deck;
    /**
     * The input stream.
     */
    private Scanner in;

    public BotClient botClient;

    public BotClient getBotClient() {
        return botClient;
    }

    /**
     * Public constructor for a Game object.
     */
    public Game() {
        this.players = new PlayerList(null);
        this.deck = new Deck();
        this.commandList = new AtomicInteger(0);
    }

    public synchronized void setCommandList(AtomicInteger commandList) {
        this.commandList = commandList;
        this.commandList.notify();
    }



    public synchronized AtomicInteger getCommandList() {
        return commandList;
    }


    public void setPlayers(PlayerList players) {
        this.players = players;
    }

    /**
     * Sets up the players that make up the player list.
     */
    // TODO limit players number from 2 to 4, and change the tokens needed according to the players number.
//    public void setPlayers() {
//        System.out.printUsedPiles("Enter player name (empty when done): ");
//        String name = in.nextLine();
//
//        while (!name.isEmpty()) {
//            if (!this.players.addPlayer(name)) {
//                System.out.println("Player is already in the game");
//            }
//            System.out.printUsedPiles("Enter player name (empty when done): ");
//            name = in.nextLine();
//        }
//    }

    /**
     * The main game loop.
     */
    public void start(BotClient botClient) {
        this.botClient = botClient;
        botClient.sendToAllPlayers("The game has started!");
        // ganz neues Spiel starten.
        while (players.getGameWinner() == null) {
            players.reset();
            setDeck();
            players.dealCards(deck);
            // next player
            while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
                Player currentPlayer = players.getCurrentPlayer();

                if (currentPlayer.hand().hasCards()) {
                    players.printUsedPiles();
                    botClient.sendToAllPlayers(currentPlayer.getName() + "'s turn:");
                    // wenn ein spieler geschützt war aber jetzt er ist dran -> nicht mehr geschützt
                    if (currentPlayer.isProtected()) {
                        currentPlayer.switchProtection();
                    }
                    // spieler zieht eine karte
                    currentPlayer.hand().add(deck.dealCard());

                    // royaltyPos is card 5 oder 6
                    int royaltyPos = currentPlayer.hand().royaltyPos();
                    // wenn ein spieler karte 5 oder 6 hat dann countess werfen
                    if (royaltyPos != -1) {
                        if (royaltyPos == 0 && currentPlayer.hand().peek(1).value() == 7) {
                            playCard(currentPlayer.hand().remove(1), currentPlayer);
                        } else if (royaltyPos == 1 && currentPlayer.hand().peek(0).value() == 7) {
                            playCard(currentPlayer.hand().remove(0), currentPlayer);
                        } else {
                            playCard(getCard(currentPlayer), currentPlayer);
                        }
                        // spieler hat kein Prince 5 oder King 6
                    } else {
                        playCard(getCard(currentPlayer), currentPlayer);
                    }
                }
            }

            Player winner;
            // winner of the round
            if (players.checkForRoundWinner() && players.getRoundWinner() != null) {
                winner = players.getRoundWinner();
            } // if there's is a tie compare the used cards
            else {
                winner = players.compareUsedPiles();
                winner.addBlock();
            }
            // add the winner of the round
            winner.addBlock();
            System.out.println(winner.getName() + " has won this round!");
            players.print();
        }
        // gives the winner of the game
        Player gameWinner = players.getGameWinner();
        System.out.println(gameWinner + " has won the game and the heart of the princess!");

    }

    /**
     * Builds a new full deck and shuffles it.
     */
    private void setDeck() {
        this.deck.buildDeck();
        this.deck.shuffleDeck();
    }

    /**
     * Determines the card used by the player and performs the card's action.
     *
     * @param card the played card
     * @param user the player of the card
     */
    private void playCard(Card card, Player user) {
        int value = card.value();
        user.used().add(card);
        // TODO make it as switch case
        if (value < 4 || value == 5 || value == 6) {
            Player opponent = value == 5 ? getOpponent(in, players, user, true) : getOpponent(in, players, user, false);
            if (value == 1) {
                useGuard(in, opponent);
            } else if (value == 2) {
                usePriest(opponent);
            } else if (value == 3) {
                useBaron(user, opponent);
            } else if (value == 5) {
                usePrince(opponent, deck);
            } else if (value == 6) {
                useKing(user, opponent);
            }
        } else {
            if (value == 4) {
                useHandmaiden(user);
            } else if (value == 8) {
                usePrincess(user);
            }
        }
    }

    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user the current player
     * @return the chosen card
     */
    private Card getCard(Player user) {
        user.hand().print();
        System.out.println();
        // TODO change the 0 or 1 to 1 and 2
        System.out.print("Which card would you like to play (1 for first, 2 for second): ");
        synchronized (commandList){
            try{
                commandList.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int s = getCommandList().get();
        if (!(s == 1) && !(s ==2)) {
            System.out.println("Please enter a valid card position");
            System.out.print("Which card would you like to play (1 for first, 2 for second): ");
        }
        // remove the chosen card
        int idx = s - 1;
        return user.hand().remove(idx);
    }



}
