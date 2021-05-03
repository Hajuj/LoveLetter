package cards;

import game.Player;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The type Card.
 */
public class Card {
    /**
     * The Card number.
     */
    public int cardNumber;
    /**
     * The Number of cards in deck.
     */
    public int numberOfCardsInDeck;
    /**
     * The Card text.
     */
    public String cardText;
    /**
     * The Card name.
     */
    public String cardName;

    /**
     * Instantiates a new Card.
     *
     * @param cardNumber          the card number
     * @param numberOfCardsInDeck the number of cards in deck
     * @param cardText            the card text
     * @param cardName            the card name
     */
    public Card(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        this.cardNumber = cardNumber;
        this.numberOfCardsInDeck = numberOfCardsInDeck;
        this.cardText = cardText;
        this.cardName = cardName;
    }

    /**
     * Choose a player int.
     *
     * @param numberOfPlayers the number of players
     * @param listOfPlayers   the list of players
     * @return the int
     */
    //TODO: choose A Player, you can't choose yourselve
    public int chooseAPlayer(int numberOfPlayers, ArrayList<Player> listOfPlayers) {
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            System.out.println("Please select a player:");
            for (int i = 1; i < numberOfPlayers + 1; i++) {
                System.out.println(i + ". Player " + i);
            }
            int selection = scanner.nextInt();
            selection--;
            loop = listOfPlayers.get(selection).isHandmaidOnline;
            if (!loop) {
                int printSelection = selection + 1;
                System.out.println("You've Chosen Player " + printSelection);
                return selection;
            } else {
                System.out.println("You've chosen a player protected by the Handmaid. Please select another player.");
            }

        }
        return -1;
    }

    /**
     * Perform card instruction.
     *
     * @param currentPlayer              the current player
     * @param currentRoundsListOfPlayers the current rounds list of players
     * @param thisRoundsDeck             the this rounds deck
     */
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        System.out.println("Sth Went Wrong, this operation should call to specific card's interaction.");
    }

    /**
     * Discard card after using.
     *
     * @param currentPlayer       the current player
     * @param idOfTheCardToRemove the id of the card to remove
     */
    public void discardCardAfterUsing(Player currentPlayer, int idOfTheCardToRemove) {
        for (int i = 0; i < currentPlayer.playerCardList.size(); i++) {
            if (currentPlayer.playerCardList.get(i).cardNumber == idOfTheCardToRemove) {
                currentPlayer.playerCardList.remove(i);
                break;
            }
        }
    }
}
