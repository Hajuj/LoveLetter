package cards;

//import model.Card;

import java.util.ArrayList;

public class Hand {

    private final ArrayList<Card> hand;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Peeks the card held by the player.
     *
     * @param idx the index of the Card to peek
     * @return the card held by the player
     */
    public Card peek(int idx) {
        return this.hand.get(idx);
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void add(Card card) {
        this.hand.add(card);
    }

    /**
     * Removes the card at the given index from the hand.
     *
     * @param idx the index of the card
     * @return the card at the given index
     */
    public Card remove(int idx) {
        return this.hand.remove(idx);
    }

    /**
     * Finds the position of a royal card in the hand.
     *
     * @return the position of a royal card, -1 if no royal card is in hand
     */
    public int royaltyPos() {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).value() == 5 || hand.get(i).value() == 6) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks to see if a player still has cards remaining.
     *
     * @return true, if the player has at least one card, false if not
     */
    public boolean hasCards() {
        return !this.hand.isEmpty();
    }

    /**
     * Clears the player hand.
     */
    public void clear() {
        this.hand.clear();
    }

    /**
     * Prints the cards making up the current player's hand.
     */
    // TODO make name more clear
    public String print() {
        String carten = "";
        for (Card c : this.hand) {
            carten = carten + c.toString();
        }
        return carten;
    }


}
