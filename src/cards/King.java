package cards;

import game.Player;

import java.util.ArrayList;

public class King extends Card {

    public static int cardNumber = 6;
    public static int numberOfCardsInDeck = 1;
    public static String cardText = "Trade hands with another player of your choice.";
    public static String cardName = "King";

    public King(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        int playerChoice = chooseAPlayer(currentRoundsListOfPlayers.size(),currentRoundsListOfPlayers);
        discardCardAfterUsing(currentPlayer,6);
        currentPlayer.playerCardList.add(currentRoundsListOfPlayers.get(playerChoice).playerCardList.get(0));
        currentRoundsListOfPlayers.get(playerChoice).playerCardList.add(currentPlayer.playerCardList.get(0));
        currentPlayer.playerCardList.remove(0);
        currentRoundsListOfPlayers.get(playerChoice).playerCardList.remove(0);
        System.out.println("The cards in hands between you and Player " + playerChoice + " have been traded");
    }

}
