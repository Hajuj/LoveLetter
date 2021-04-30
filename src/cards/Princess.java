package cards;

import game.Player;

import java.util.ArrayList;

/**
 * The type Princess.
 */
public class Princess extends Card {
    /**
     * The constant cardNumber.
     */
    public static int cardNumber = 8;
    /**
     * The constant numberOfCardsInDeck.
     */
    public static int numberOfCardsInDeck = 1;
    /**
     * The constant cardText.
     */
    public static String cardText = "If you discard this card, you are out of this round.";
    /**
     * The constant cardName.
     */
    public static String cardName = "Princess";

    /**
     * Instantiates a new Princess.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Princess(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 8);
        System.out.println("You have discarded a Princess card! You are out of this round!");
        currentPlayer.isEliminated = true;
    }
}

