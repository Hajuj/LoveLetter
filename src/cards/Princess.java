package cards;

import game.Player;

import java.util.ArrayList;

public class Princess extends Card {
    public static int cardNumber = 8;
    public static int numberOfCardsInDeck = 1;
    public static String cardText = "If you discard this card, you are out of this round.";
    public static String cardName = "Princess";

    public Princess(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer,8);
        System.out.println("You have discarded a Princess card! You are out of this round!");
        currentPlayer.isEliminated = true;
    }
}

