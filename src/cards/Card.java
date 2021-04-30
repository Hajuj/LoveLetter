package cards;

import game.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class Card {
    public int cardNumber;
    public int numberOfCardsInDeck;
    public String cardText;
    public String cardName;

    public Card(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        this.cardNumber = cardNumber;
        this.numberOfCardsInDeck = numberOfCardsInDeck;
        this.cardText = cardText;
        this.cardName = cardName;
    }

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

    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        System.out.println("Sth Went Wrong, this operation should call to specific card's interaction.");
    }

    public void discardCardAfterUsing(Player currentPlayer, int idOfTheCardToRemove) {
        for (int i = 0; i < currentPlayer.playerCardList.size(); i++) {
            if (currentPlayer.playerCardList.get(i).cardNumber == idOfTheCardToRemove) {
                currentPlayer.playerCardList.remove(i);
                break;
            }
        }
    }
}
