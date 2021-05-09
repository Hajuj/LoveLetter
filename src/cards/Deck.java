package cards;


import java.util.Collections;
import java.util.Stack;

/**
 * Class representing a deck of Card objects.
 */
public class Deck {

    /**
     * Stack of Card objects.
     */
    private final Stack<Card> deck;

    /**
     * Public constructor for a Deck object.
     */
    public Deck() {
        this.deck = new Stack<>();
    }

    /**
     * Builds a deck with:
     * 5 Guards
     * 2 Priests
     * 2 Barons
     * 2 Handmaidens
     * 2 Princes
     * 1 King
     * 1 Countess
     * 1 Princess
     */
    public void buildDeck() {
        for (int i = 0; i < 5; i++) {
            deck.push(Card.GUARD);
        }
        for (int i = 0; i < 2; i++) {
            deck.push(Card.PRIEST);
            deck.push(Card.BARON);
            deck.push(Card.HANDMAIDEN);
            deck.push(Card.PRINCE);
        }
        deck.push(Card.KING);
        deck.push(Card.COUNTESS);
        deck.push(Card.PRINCESS);
    }

    /**
     * Shuffles the stack of Card objects
     */
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Pops off the top Card in the Stack.
     *
     * @return the top card of the deck
     */
    public Card dealCard() {
        return deck.pop();
    }

    /**
     * Checks to see if the deck has more Card objects.
     *
     * @return true if the deck has more cards, false if not
     */
    public boolean hasMoreCards() {
        return !deck.isEmpty();
    }
}
