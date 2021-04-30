package cards;
import game.Player;

import java.util.ArrayList;

public class Handmaid extends Card {

    public static int cardNumber = 4;
    public static int numberOfCardsInDeck = 2;
    public static String cardText = "Until your next turn, ignore all effects from other players' cards.";
    public static String cardName = "Handmaid";

    public Handmaid(int cardNumber, int numberOfCardsInDeck, String cardText, String cardName) {
        super(cardNumber, numberOfCardsInDeck, cardText, cardName);
    }

    @Override
    public void performCardInstruction(Player currentPlayer, ArrayList<Player> currentRoundsListOfPlayers, Deck thisRoundsDeck) {
        discardCardAfterUsing(currentPlayer, 4);
        currentPlayer.isHandmaidOnline = true;
    }
}
