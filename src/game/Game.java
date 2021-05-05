package game;

import cards.*;
import chat.BotClient;

import java.util.Scanner;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions implements Runnable {

    /**
     * The list of players in the game.
     */
    private PlayerList players;
    /**
     * The deck of cards.
     */
    private Deck deck;
    /**
     * The input stream.
     */
    private Scanner in;

    private BotClient botClient;

    /**
     * Public constructor for a Game object.
     */
    public Game() {
        this.players = new PlayerList(botClient);
        this.deck = new Deck();
    }

    public void setPlayers(PlayerList players) {
        this.players = players;
    }

    public void setBotClient(BotClient botClient) {
        this.botClient = botClient;
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
    public void start() throws InterruptedException {
//        this.botClient = botClient;
        botClient.sendToAllPlayers("The game has started!");
        // ganz neues Spiel starten.
        while (players.getGameWinner() == null) {
            players.reset();
            setDeck();
            players.dealCards(deck);
            // next player
            while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
                Player turn = players.getCurrentPlayer(); // TODO turn -> playerTurn

                if (turn.hand().hasCards()) {
                    players.printUsedPiles();
                    botClient.sendToAllPlayers(turn.getName() + "'s turn:");
                    // wenn ein spieler geschutzt war aber jetzt er ist dran -> nicht mehr geschutzt
                    if (turn.isProtected()) {
                        turn.switchProtection();
                    }
                    // spieler zieht eine karte
                    turn.hand().add(deck.dealCard());

                    // royaltePos is card 5 oder 6
                    int royaltyPos = turn.hand().royaltyPos();
                    // wenn ein spieler karte 5 oder 6 hat dann countess werfen
                    if (royaltyPos != -1) {
                        if (royaltyPos == 0 && turn.hand().peek(1).value() == 7) {
                            playCard(turn.hand().remove(1), turn);
                        } else if (royaltyPos == 1 && turn.hand().peek(0).value() == 7) {
                            playCard(turn.hand().remove(0), turn);
                        }
                        else {
                            playCard(getCard(turn), turn);
                        }
                        // spieler hat kein Prince 5 oder King 6
                    } else {
                        playCard(getCard(turn), turn);
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
     * @param card
     *          the played card
     * @param user
     *          the player of the card
     */
    private void playCard(Card card, Player user) {
        int value = card.value();
        user.used().add(card);
        // TODO make it as switch case
        if (value < 4 || value == 5 || value == 6) {
            Player opponent = value == 5 ? getOpponent(botClient, players, user, true):getOpponent(botClient, players, user, false);
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
     * @param user
     *      the current player
     *
     * @return the chosen card
     */
    private Card getCard(Player user) throws InterruptedException {
        botClient.sendTextMessage("@" + user.getName() + " " + user.hand().print());
//        System.out.println();
        // TODO change the 0 or 1 to 1 and 2
        botClient.sendTextMessage("@" + user.getName() + " Which card would you like to play (1 for first, 2 for second): ");
//        String cardPosition = in.nextLine();
//        while (!cardPosition.equals("1") && !cardPosition.equals("2")) {
//            botClient.sendTextMessage("@" + " Please enter a valid card position");
//            botClient.sendTextMessage("@" + " Which card would you like to play (1 for first, 2 for second): ");
//            cardPosition = in.nextLine();
//        }
        // remove the chosen card
//        int idx = Integer.parseInt(cardPosition) - 1;
        synchronized (botClient.getCurrentcards()){
            try{
                botClient.getCurrentcards().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        botClient.getCurrentcards().wait();

                int idx = botClient.getCurrentcards().get(user);
                botClient.getCurrentcards().replace(user, 10);
                return user.hand().remove(idx - 1);


    }

    @Override
    public void run() {
        try {
            this.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
