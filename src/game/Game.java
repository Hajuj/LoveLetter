package game;

import cards.Card;
import cards.Deck;
import chat.BotClient;

import java.util.ArrayList;
import java.util.Arrays;

// TODO applying the prince to a player who has the Princess won't make him lose the game.
// TODO if all players are protected, and a player tries to play one of these cards:
//      Guard, Priest, Baron or King, the game should skip for the next round automatically,
//      since the player can't choose himself or others (because they are protected).
// TODO limit players number from 2 to 4, and change the tokens needed according to the players number.

/**
 * The main game class. Contains methods for running the game.
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

    public boolean checkProtection() {
        Player current = players.getCurrentPlayer();
        boolean res = false;
        for(Player player : players.getPlayers()) {
            if (!player.equals(current)) {
                if(!player.isProtected()) {
                    res = false;
                } else {
                    res = true;
                }
            }
        }
        return res;
    }

    /**
     * The main game loop.
     *
     * @throws InterruptedException the interrupted exception
     */
    public void start() throws InterruptedException {
//        this.botClient = botClient;
        botClient.sendToAllPlayers("### The game has started! ###");
        // ganz neues Spiel starten.
        while (players.getGameWinner() == null) {
            players.reset();
            setDeck();
            players.dealCards(deck);
            // next player
            while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
                Player playerTurn = players.getCurrentPlayer();
                //Player opponentNoHandmaid = players.getCurrentOpponent();

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

                    /*Unlike other cards, which take effect when discarded, the text on the Countess
                    applies while she is in your hand. In fact, the only time it doesn't apply
                    is when you discard her. If you ever have the Countess and either the King or
                    Prince in your hand, you must discard the Countess. You do not have to reveal
                    the other card in your hand. Of course, you can also discard the Countess even
                    if you do not have a royal family member in your hand.
                    The Countess likes to play mind games....*/
                    if (royaltyPos != -1) {
                        if (royaltyPos == 0 && playerTurn.hand().peek(1).value() == 7) {
                            playCard(playerTurn.hand().remove(1), playerTurn);
                        } else if (royaltyPos == 1 && playerTurn.hand().peek(0).value() == 7) {
                            playCard(playerTurn.hand().remove(0), playerTurn);
                        } else {
                            playCard(getCard(playerTurn), playerTurn);
                        }
                        // spieler hat kein Prince 5 oder King 6
                    } //TODO: Opponent with Handmaid ?
                     /* else if (opponentNoHandmaid == null) {
                         boolean index = true;
                         if (index){
                             playCard(playerTurn.hand().remove(0), playerTurn);
                         } else {playCard(playerTurn.hand().remove(1), playerTurn);}
                    } */ else {
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
            botClient.sendToAllPlayers(winner.getName() + " has won this round!");
            botClient.sendToAllPlayers(winner.getName() + "\n" + "\n ### new Round ### \n");
            players.print();
        }
        // gives the winner of the game
        Player gameWinner = players.getGameWinner();
        botClient.sendToAllPlayers(gameWinner + " has won the game and the heart of the princess!");
        botClient.setGameOn(false);
        // botClient.stop();

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
            if (value == 1 && !checkProtection()) {
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
                usePrincess(botClient, user);
            }
        }
    }




    /*private void playCard(Card card, Player user) {
        int value = card.value();
        user.used().add(card);
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
    }*/


    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user the current player
     * @return the chosen card
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
