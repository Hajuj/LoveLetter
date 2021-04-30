package cards;

import game.Player;

import java.util.ArrayList;

/**
 * The type Handmaid.
 */
public class Handmaid extends Card {

    /**
     * The constant cardNumber.
     */
    public static int cardNumber = 4;
    /**
     * The constant numberOfCardsInDeck.
     */
    public static int numberOfCardsInDeck = 2;
    /**
     * The constant cardText.
     */
    public static String cardText = "Until your next turn, ignore all effects from other players' cards.";
    /**
     * The constant cardName.
     */
    public static String cardName = "Handmaid";

    /**
     * Instantiates a new Handmaid.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Handmaid(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 4);
        currentPlayer.isHandmaidOnline = true;
    }
}
