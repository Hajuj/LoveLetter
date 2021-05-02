package cards;

import game.Player;

import java.util.ArrayList;

/**
 * The type Prince.
 */
public class Prince extends Card {

    /**
     * The constant cardNumber.
     */
    public static int cardNumber = 5;
    /**
     * The constant numberOfCardsInDeck.
     */
    public static int numberOfCardsInDeck = 2;
    /**
     * The constant cardText.
     */
    public static String cardText = "Choose a player (including yourself) to discard his or her hand and draw a new card.";
    /**
     * The constant cardName.
     */
    public static String cardName = "Prince";

    /**
     * Instantiates a new Prince.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Prince(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        int chosenPlayer = chooseAPlayer(currentRoundsListOfPlayers.size(), currentRoundsListOfPlayers);
        discardCardAfterUsing(currentPlayer, 5);
        if (currentRoundsListOfPlayers.get(chosenPlayer).playerCardList.get(0).cardNumber == 8) {
            currentRoundsListOfPlayers.get(chosenPlayer).isEliminated = true;
            System.out.println("Player " + chosenPlayer + " has discarded the Princess! That player is out of this round!");
        }
        currentRoundsListOfPlayers.get(chosenPlayer).playerCardList.clear();
        currentRoundsListOfPlayers.get(chosenPlayer).drawACard(currentRoundsListOfPlayers.get(chosenPlayer), thisRoundsDeck);
        thisRoundsDeck.shuffledDeckList.remove(0);
    }
}
