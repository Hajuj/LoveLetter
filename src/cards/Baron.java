package cards;

import game.Player;

import java.util.ArrayList;

/**
 * The type Baron.
 */
public class Baron extends Card {
    /**
     * The constant cardNumber.
     */
    public static int cardNumber = 3;
    /**
     * The constant numberOfCardsInDeck.
     */
    public static int numberOfCardsInDeck = 2;
    /**
     * The constant cardText.
     */
    public static String cardText = "You and that player secretly compare your hands. The player with the lower number is knocked out of the round. In case of a tie, nothing happens..";
    /**
     * The constant cardName.
     */
    public static String cardName = "Baron";

    /**
     * Instantiates a new Baron.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Baron(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 3);
        int playerChoice = chooseAPlayer(currentRoundsListOfPlayers.size(), currentRoundsListOfPlayers);
        int yourScore = currentPlayer.playerCardList.get(0).cardNumber;
        int opponentsScore = currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardNumber;
        System.out.println("The chosen player's card value is: " + opponentsScore + ".");
        System.out.println("Your card's value is: " + yourScore + ".");
        if (yourScore > opponentsScore) {
            currentRoundsListOfPlayers.get(playerChoice).isEliminated = true;
            System.out.println("You have higher card value than your opponent. The opponent is eliminated!");
        } else if (yourScore < opponentsScore) {
            currentPlayer.isEliminated = true;
            System.out.println("You have lower card value than your opponent. You are eliminated!");
        } else {
            System.out.println("Draw - neither player is eliminated.");
        }
    }
}
