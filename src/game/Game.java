package game;

import cards.Card;
import cards.Deck;
import chat.BotClient;


/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions implements Runnable {

    /**
     * The deck of cards.
     */
    private final Deck deck;
    /**
     * The list of players in the game.
     */
    private PlayerList players;
    /**
     * The input stream.
     */

    private BotClient botClient;

    /**
     * Public constructor for a Game object.
     */
    public Game() {
        //TODO wieso ist botClient immer null? ersetzen mit null?
        this.players = new PlayerList(botClient);
        this.deck = new Deck();
    }

    /**
     * Sets players.
     *
     * @param players the players
     */
    public void setPlayers(PlayerList players) {
        this.players = players;
    }

    /**
     * Sets bot client.
     *
     * @param botClient the bot client
     */
    public void setBotClient(BotClient botClient) {
        this.botClient = botClient;
    }

    // /**
    //   * Sets up the players that make up the player list.
//     */
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
     *
     * @throws InterruptedException the interrupted exception
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
                Player playerTurn = players.getCurrentPlayer();

                if (playerTurn.hand().hasCards()) {
                    players.printUsedPiles();
                    botClient.sendToAllPlayers(playerTurn.getName() + "'s turn:");
                    // wenn ein spieler geschützt war aber jetzt er ist dran -> nicht mehr geschützt
                    if (playerTurn.isProtected()) {
                        playerTurn.switchProtection();
                    }
                    // spieler zieht eine karte
                    playerTurn.hand().add(deck.dealCard());

                    // royaltyPos is card 5 oder 6
                    int royaltyPos = playerTurn.hand().royaltyPos();
                    // wenn ein spieler karte 5 oder 6 hat dann countess werfen
                    if (royaltyPos != -1) {
                        if (royaltyPos == 0 && playerTurn.hand().peek(1).value() == 7) {
                            playCard(playerTurn.hand().remove(1), playerTurn);
                        } else if (royaltyPos == 1 && playerTurn.hand().peek(0).value() == 7) {
                            playCard(playerTurn.hand().remove(0), playerTurn);
                        } else {
                            playCard(getCard(playerTurn), playerTurn);
                        }
                        // spieler hat kein Prince 5 oder King 6
                    } else {
                        playCard(getCard(playerTurn), playerTurn);
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
                winner.addRoundWinner();
            }
            // add the winner of the round
            winner.addRoundWinner();
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
            Player opponent = value == 5 ? getOpponent(players, user, true) : getOpponent(players, user, false);
            if (value == 1) {
                useGuard(botClient, user, opponent);
            } else if (value == 2) {
                usePriest(botClient, user, opponent);
            } else if (value == 3) {
                useBaron(botClient, user, opponent);
            } else if (value == 5) {
                usePrince(opponent, deck);
            } else if (value == 6) {
                useKing(user, opponent);
            }
        } else {
            if (value == 4) {
                useHandmaiden(botClient, user);
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
        botClient.sendTextMessage("@" + user.getName() + " " + user.hand().printHand() + " \n Which card would you like to play (1 for first, 2 for second): ");
        /*int index=0;
        for(String s : user.hand()){
            botClient.sendTextMessage("@" + user.getName() + " " + String.valueOf(index++)+": "+s);
        }*/


        // TODO change the 0 or 1 to 1 and 2
//        String cardPosition = in.nextLine();
//        while (!cardPosition.equals("1") && !cardPosition.equals("2")) {
//            botClient.sendTextMessage("@" + " Please enter a valid card position");
//            botClient.sendTextMessage("@" + " Which card would you like to play (1 for first, 2 for second): ");
//            cardPosition = in.nextLine();
//        }
        // remove the chosen card
//        int idx = Integer.parseInt(cardPosition) - 1;
        synchronized (botClient.getCurrentCards()) {
            try {
                botClient.getCurrentCards().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


//        botClient.getCurrentCards().wait();

        int idx = botClient.getCurrentCards().get(user);
        botClient.getCurrentCards().replace(user, 10);
        return user.hand().remove(idx - 1);


    }

    /**
     * Useful method for obtaining a chosen target from the player list.
     *
     * @param playerList the list of players
     * @param user       the player choosing an opponent
     * @return the chosen target player
     */
    private Player getOpponent(PlayerList playerList, Player user, boolean isPrince) {
        Player opponent = null;
        boolean validTarget = false;
        while (!validTarget) {
            botClient.sendTextMessage("@" + user.getName() + " Who would you like to target: ");
            synchronized (botClient.getCurrentOpponent()) {
                try {
                    botClient.getCurrentOpponent().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            opponent = playerList.getPlayer(botClient.getCurrentOpponent().get(user));
            if (opponent == null) {
                botClient.sendTextMessage("@" + user.getName() + " This player is not in the game.");

            } else if (opponent.isProtected()) {
                botClient.sendTextMessage("@" + user.getName() + " This player is protected by a handmaiden.");

            } else if (opponent.getName().equals(user.getName()) && !isPrince) {
                botClient.sendTextMessage("@" + user.getName() + " You cannot target yourself.");

            } else if (!opponent.hand().hasCards()) {
                botClient.sendTextMessage("@" + user.getName() + " This player is eliminated.");

            } else {
                validTarget = true;
            }
        }
        return opponent;
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
