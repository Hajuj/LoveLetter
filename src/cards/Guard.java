package cards;

import game.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class Guard extends Card {

    public static int cardNumber = 1;
    public static int numberOfCardsInDeck = 5;
    public static String cardText = "Name a non-guard card and choose a player. If that player has that card, he or she is out of the round.";
    public static String cardName = "Guard";

    public Guard(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    public int promptGuardCardNameSelection() {
        boolean choiceIsOk = false;
        int choice = 0;
        while (!choiceIsOk) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please select a card name:");
            System.out.println("2. Priest");
            System.out.println("3. Baron");
            System.out.println("4. Handmaid");
            System.out.println("5. Prince");
            System.out.println("6. King");
            System.out.println("7. Countess");
            System.out.println("8. Princess");
            choice = scanner.nextInt();
            if (choice < 2 || choice > 8) {
                System.out.println("Please select a number between 2 and 8");
            } else {
                choiceIsOk = true;
            }
        }
        return choice;
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 1);
        int playerChoice = chooseAPlayer(currentRoundsListOfPlayers.size(), currentRoundsListOfPlayers);
        int cardNameChoice = promptGuardCardNameSelection();
        if (currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardNumber == cardNameChoice) {
            currentRoundsListOfPlayers.get(playerChoice).isEliminated = true;
            System.out.println("The chosen player's card was: " + currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardNumber + ". " + currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardName + ".");
            System.out.println("That card matches your choice. The player has been eliminated");
        } else {
            System.out.println("The chosen player's card was: " + currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardNumber + ". " + currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0).cardName + ".");
            System.out.println("The chosen player's card did not match your choice");
        }
    }
}
