package cards;

import game.Player;

import java.util.ArrayList;

/**
 * The type Priest.
 */
public class Priest extends Card {

    /**
     * The constant cardNumber.
     */
    public static int cardNumber = 2;
    /**
     * The constant numberOfCardsInDeck.
     */
    public static int numberOfCardsInDeck = 2;
    /**
     * The constant cardText.
     */
    public static String cardText = "Look at another player's hand";
    /**
     * The constant cardName.
     */
    public static String cardName = "Priest";

    /**
     * Instantiates a new Priest.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Priest(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 2);
        int playerChoice = chooseAPlayer(currentRoundsListOfPlayers.size(), currentRoundsListOfPlayers);
        System.out.println("The chosen player's card in hand is: " + currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardName + ".");
    }
}


