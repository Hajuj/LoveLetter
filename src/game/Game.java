package game;

import cards.Card;
import cards.Deck;
import chat.BotClient;

/**
 * The main game class. Contains methods for running the game.
 *
 * @author Altug, Chiara, Jonas, Mohamad, Viktoria
 */
public class Game extends GameActions implements Runnable {

    /**
     * The deck of cards.
     */
    public final Deck deck;
    /**
     * The list of players in the game.
     */
    public PlayerList players;
    /**
     * The input stream.
     */


    public BotClient botClient;

    /**
     * Public constructor for a Game object.
     */
    public Game() {
        this.players = new PlayerList(null);
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

    /**
     * The main game loop.
     * Sets up the game.
     * Deals cards and declares turns.
     * Edge game conditions :
     * Royalty Position and used pile comparison.
     *
     *
     * @throws InterruptedException the interrupted exception
     *
     * @author Mohamad, Jonas
     */
    public void start() throws InterruptedException {
        Player winner = null;
        Player playerTurn;
        botClient.sendToAllPlayers("The game has started!");
        // ganz neues Spiel starten.
        while (players.getGameWinner() == null) {
            players.reset();
            setDeck();
            players.dealCards(deck);
            // next player
            while (deck.hasMoreCards() && !players.checkForRoundWinner()) {
                if (winner != null) {
                    players.getWinner(winner);
                    winner = null;
                }
                playerTurn = players.getCurrentPlayer();
                if (playerTurn.hand().hasCards()) {
                    players.printUsedPiles();
                    botClient.sendToAllPlayers(playerTurn.getName() + "'s turn:");
                    // wenn ein spieler gesch??tzt war aber jetzt er ist dran -> nicht mehr gesch??tzt
                    if (playerTurn.isProtected()) {
                        playerTurn.switchProtection();
                    }
                    // spieler zieht eine karte
                    playerTurn.hand().add(deck.dealCard());

                    // checks if all the players (other than current player) are protected.
                    boolean allProtected = players.allPlayersProtected(playerTurn);
                    // royaltyPos is card 5 oder 6
                    int royaltyPos = playerTurn.hand().royaltyPos();
                    // not all players are protected.
                    if (!allProtected) {


                        // wenn ein spieler karte 5 oder 6 hat dann countess werfen
                        if (royaltyPos != -1) {
                            if (royaltyPos == 0 && playerTurn.hand().peek(1).value() == 7) {
                                playCard(playerTurn.hand().remove(1), playerTurn, false);
                                botClient.sendToAllPlayers("The Countess is discarded because player [" + playerTurn + "] has a King or a Prince.");
                            } else if (royaltyPos == 1 && playerTurn.hand().peek(0).value() == 7) {
                                playCard(playerTurn.hand().remove(0), playerTurn, false);
                                botClient.sendToAllPlayers("The Countess is discarded because player [" + playerTurn + "] has a King or a Prince.");
                            } else {
                                playCard(getCard(playerTurn), playerTurn, false);
                            }
                            // spieler hat kein Prince 5 oder King 6
                        } else {
                            playCard(getCard(playerTurn), playerTurn, false);
                        }
                    }

                    // all players are protected but you might have Countess, Prince or Princess where you can still play.
                    else {
                        // if you have a royal card and a countess, countess will be removed automatically.
                        if (royaltyPos != -1) {
                            if (royaltyPos == 0 && playerTurn.hand().peek(1).value() == 7) {
                                playCard(playerTurn.hand().remove(1), playerTurn, true);
                                botClient.sendToAllPlayers("The Countess is discarded because player [" + playerTurn + "] has a King or a Prince.");
                            } else if (royaltyPos == 1 && playerTurn.hand().peek(0).value() == 7) {
                                playCard(playerTurn.hand().remove(0), playerTurn, true);
                                botClient.sendToAllPlayers("The Countess is discarded because player [" + playerTurn + "] has a King or a Prince.");
                                // if you have a royal card, but the other card is not a countess.
                            } else {
                                playCard(getCard(playerTurn), playerTurn, true);
                            }
                            // if you don't have a royal card.
                        } else {
                            playCard(getCard(playerTurn), playerTurn, true);
                        }
                    }
                }
            }
//            Player winner;
            // winner of the round
            if (players.checkForRoundWinner() && players.getRoundWinner() != null) {
                winner = players.getRoundWinner();
            } // if there's is a tie compare the used cards
            else {
                winner = players.compareUsedPiles();
                if (botClient.getGameTie()) {
                    for (Player n : players.getPlayers()) {
                        if (!n.equals(winner)) {
                            n.addRoundWinner();
                        }
                    }
                }
            }
            // add the winner of the round
            winner.addRoundWinner();
            botClient.sendToAllPlayers(winner.getName() + " has won this round!\n" + "\n"+"### NEW ROUND ### \n");
            botClient.sendToAllPlayers(players.print());
        }
        // gives the winner of the game
        Player gameWinner = players.getGameWinner();
        botClient.sendToAllPlayers(gameWinner + " has won the game and the heart of the princess!");
        botClient.setGameOn(false);
        botClient.sendToAllPlayers("To start a new game please reactivate the Bot!");
        botClient.sendTextMessage("bye");
    }

    /**
     * Builds a new full deck and shuffles it.
     *
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
     *
     * @author Mohamad, Jonas
     */
    private void playCard(Card card, Player user, boolean allProtected) {
        int value = card.value();
        user.used().add(card);
        if (!allProtected) {
            if (value < 4 || value == 5 || value == 6) {
                Player opponent = value == 5 ? getOpponent(players, user, true) : getOpponent(players, user, false);
                switch (value) {
                    case 1 -> useGuard(botClient, user, opponent);
                    case 2 -> usePriest(botClient, user, opponent);
                    case 3 -> useBaron(botClient, user, opponent);
                    case 5 -> usePrince(opponent, deck);
                    case 6 -> useKing(user, opponent);
                }
            } else {
                switch (value) {
                    case 4 -> useHandmaiden(botClient, user);
                    case 8 -> usePrincess(botClient, user);
                }
            }
        } else if (value == 4 || value == 5 || value == 8) {
            switch (value) {
                case 4 -> useHandmaiden(botClient, user);
                case 5 -> usePrince(getOpponent(players, user, true), deck);
                case 8 -> usePrincess(botClient, user);
            }
        } else {
            botClient.sendTextMessage("@" + user.getName() + " All players are protected. Your card is discarded without calling its effect.");
        }
    }


    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user the current player
     * @return the chosen card
     *
     * @author Viktoria, Altug
     */
    private Card getCard(Player user) {
        botClient.sendTextMessage("@" + user.getName() + " " + user.hand().printHand() + " \n Which card would you like to play (1 for first, 2 for second): ");
        synchronized (botClient.getCurrentCards()) {
            try {
                botClient.getCurrentCards().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int idx = botClient.getCurrentCards().get(user);
        while (!(idx == 1 || idx == 2)) {


            botClient.sendTextMessage("@" + user.getName() + " " + user.hand().printHand() + " \n Wrong number - Which card would you like to play (1 for first, 2 for second): ");

            synchronized (botClient.getCurrentCards()) {
                try {
                    botClient.getCurrentCards().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            idx = botClient.getCurrentCards().get(user);
        }
        botClient.getCurrentCards().replace(user, 10);
        return user.hand().remove(idx - 1);

    }

    /**
     * Useful method for obtaining a chosen target from the player list.
     *
     * @param playerList the list of players
     * @param user       the player choosing an opponent
     * @return the chosen target player
     *
     * @author Viktoria, Mohamad
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

    /**
     * run method.
     */

    @Override
    public void run() {
        try {
            this.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
