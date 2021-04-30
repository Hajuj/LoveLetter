package cards;

import game.Player;

import java.util.ArrayList;

/**
 * The type Countess.
 */
public class Countess extends Card {
    /**
     * The constant cardNumber.
     */
    public static int cardNumber = 7;
    /**
     * The constant numberOfCardsInDeck.
     */
    public static int numberOfCardsInDeck = 1;
    /**
     * The constant cardText.
     */
    public static String cardText = "If you have this card and a King or Prince in your hand, you must discard this card.";
    /**
     * The constant cardName.
     */
    public static String cardName = "Countess";

    /**
     * Instantiates a new Countess.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Countess(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 7);
    }
}
