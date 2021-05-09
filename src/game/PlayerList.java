package game;

import cards.Deck;
import chat.BotClient;

import java.util.LinkedList;

/**
 * Class representing the collective list of players.
 */
public class PlayerList {

    /**
     * The list of players.
     */
    private final LinkedList<Player> players;


    private final BotClient botClient;

    /**
     * Public constructor for a PlayerList object.
     *
     * @param botClient the bot client
     */
    public PlayerList(BotClient botClient) {
        this.players = new LinkedList<>();
        this.botClient = botClient;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public LinkedList<Player> getPlayers() {
        return players;
    }

    /**
     * Check for user boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean checkForUser(String name) {
        for (Player p : this.getPlayers()) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // checks if all the players (other than current player) are protected.
    public boolean allPlayersProtected(Player user) {
        boolean allProtected = true;
        for (Player p : players) {
            if (!p.isProtected() && !p.equals(user)) {
                allProtected = false;
            }
        }
        return allProtected;
    }

    /**
     * Adds a new Player object with the given name to the PlayerList.
     *
     * @param name the given player name
     */
    public void addPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return;
            }
        }
        players.addLast(new Player(name));
    }


    /**
     * Gets the first player in the list and adds them to end of the list.
     *
     * @return the first player in the list
     */
    public Player getCurrentPlayer() {
        Player current = players.removeFirst();
        players.addLast(current);
        return current;
    }

    /**
     * Resets all players within the list.
     */
    public void reset() {
        for (Player p : players) {
            p.hand().clear();
            p.used().clear();
        }
    }

    /**
     * Prints the used pile of each Player in the list.
     */
    public void printUsedPiles() {
        for (Player p : players) {
            botClient.sendToAllPlayers("The used cards of [" + p.getName() + "] are:" + "\n" + p.used().printUsedPiles());
        }
    }

    /**
     * Prints each Player in the list.
     */
    public void print() {
        System.out.println();
        for (Player p : players) {
            System.out.println(p);
        }
        System.out.println();
    }

    /**
     * Checks the list for a single Player with remaining cards.
     *
     * @return true if there is a winner, false if not
     */
    public boolean checkForRoundWinner() {
        int count = 0;
        for (Player p : players) {
            if (p.hand().hasCards()) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Returns the winner of the round.
     *
     * @return the round winner
     */
    public Player getRoundWinner() {
        for (Player p : players) {
            if (p.hand().hasCards()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the winner of the game.
     *
     * @return the game winner
     */
    public Player getGameWinner() {
        for (Player p : players) {
            if (p.getLetterCount() == botClient.getLoveLetters()) {
                return p;
            }
        }
        return null;
    }

    public void getCurrentScore(Player user) {
        botClient.sendTextMessage("@" + user.getName() + " \nThe score of [" + user.getName() + "] is: " + user.getLetterCount());
    }

    /**
     * Deals a card to each Player in the list.
     *
     * @param deck the deck of cards
     */
    public void dealCards(Deck deck) {
        for (Player p : players) {
            p.hand().add(deck.dealCard());
        }
    }

    /**
     * Gets the player with the given name.
     *
     * @param name the name of the desired player
     * @return the player with the given name or null if there is no such player
     */
    public Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the player with the highest used pile value.
     *
     * @return the player with the highest used pile value
     */
    public Player compareUsedPiles() {
        Player winner = players.getFirst();
        for (Player p : players) {
            if (p.used().value() > winner.used().value()) {
                winner = p;
                botClient.sendToAllPlayers(" The cards were compared because there is a tie. \n " + winner.getName() + " has the highest total of the discard pile and won the round!");
            } else {
                winner = p;
                botClient.sendToAllPlayers(" The cards were compared. There is still a tie. The round won: \n" + winner.getName());
            }
        }
        return winner;
    }


}
