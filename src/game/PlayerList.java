package game;

import cards.Deck;
import chat.BotClient;

import java.util.LinkedList;

/**
 * Class representing the collective list of players.
 *
 * @author Jonas, Mohamad, Viktoria
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

    /**
     * getter for a winner.
     * @param winner  for the winner
     */

    public void getWinner(Player winner) {
        int getWinner = players.indexOf(winner);
        Player firstOne = players.getFirst();
        players.set(0, winner);
        players.set(getWinner, firstOne);
    }

    /**
     * Checks if all the players except the current player are protected by the card Handmaid
     * and being used to avoid getting stuck in a if clause.
     * @param user  for the user
     * @return  the user
     */
    public boolean allPlayersProtected(Player user) {
        boolean allProtected = true;
        for (Player p : players) {
            if (!p.isProtected() && !p.equals(user) && p.hand().hasCards()) {
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
     * @return the Score after Round
     */

    public String print() {
        String roundScore = "";
        for (Player p : players) {
            roundScore = roundScore + p.toString() + "\n";
        }
        return roundScore;
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

    /**
     * Prints the score of the current player.
     * @param user for the user
     */

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
     * Used to determine who wins in case of a tie of a comparison of the hands.
     *
     * @return the player with the highest used pile value
     */
    public Player compareUsedPiles() {
        Player winner = players.getFirst();
        for (Player p : players) {
            if (p.used().value() > winner.used().value()) {
                winner = p;
                botClient.setGameTie(false);
                botClient.sendToAllPlayers(" The used cards were compared because there is no more cards in the Deck. \n " + winner.getName() + " has the highest total of the discard pile and won the round!");
            } else {
                winner = p;
                botClient.setGameTie(true);
                botClient.sendToAllPlayers(" The cards were compared. There is still a tie. All players won the round: \n" + winner.getName());
            }
        }
        return winner;
    }

}
