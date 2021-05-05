package game;

import cards.*;
import chat.BotClient;

import java.util.LinkedList;

/**
 * Class representing the collective list of players.
 */
public class PlayerList{

    /**
     * The list of players.
     */
    private LinkedList<Player> players;

    private BotClient botClient;

    /**
     * Public constructor for a PlayerList object.
     */
    public PlayerList(BotClient botClient) {
        this.players = new LinkedList<>();
        this.botClient = botClient;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    /**
     * Adds a new Player object with the given name to the PlayerList.
     *
     * @param name
     *          the given player name
     *
     * @return true if the player is not already in the list and can be added, false if not
     */
    public boolean addPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        players.addLast(new Player(name));
        return true;
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
            botClient.sendToAllPlayers(p.getName() + "\n" + p.used().printUsedPiles());
//            p.used().printUsedPiles();
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
            if (p.getBlockCount() == 5) {
                return p;
            }
        }
        return null;
    }

    /**
     * Deals a card to each Player in the list.
     *
     * @param deck
     *          the deck of cards
     */
    public void dealCards(Deck deck) {
        for (Player p : players) {
            p.hand().add(deck.dealCard());
        }
    }

    /**
     * Gets the player with the given name.
     *
     * @param name
     *          the name of the desired player
     *
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
    // TODO all players win if there's still a tie after comparing the used cards. (if else in the for).
    public Player compareUsedPiles() {
        Player winner = players.getFirst();
        for (Player p : players) {
            if (p.used().value() > winner.used().value()) {
                winner = p;
            }
        }
        return winner;
    }

}
