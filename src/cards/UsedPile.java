package cards;

import java.util.ArrayList;

/**
 * The type Used pile.
 */
public class UsedPile {

    private final ArrayList<Card> used;

    /**
     * Instantiates a new Used pile.
     */
    public UsedPile() {
        this.used = new ArrayList<>();
    }

    /**
     * Adds a card to the player's used pile.
     *
     * @param used the card to add to the used pile
     */
    public void add(Card used) {
        this.used.add(used);
    }

    /**
     * Gets the sum value of the cards in the player's used pile.
     *
     * @return the sum of the player's used pile
     */
    public int value() {
        int value = 0;
        for (Card c : this.used) {
            value += c.value();
        }
        return value;
    }

    /**
     * Clear.
     */
    public void clear() {
        this.used.clear();
    }

    /**
     * Prints the used pile of the current player.
     *
     * @return the string
     */
    public String printUsedPiles() {
        StringBuilder cards = new StringBuilder();
        for (Card c : this.used) {
            cards.append(c.getName()).append("\n");
        }
        return cards.toString();
    }

}