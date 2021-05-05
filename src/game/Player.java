package game;

import cards.Card;
import cards.Deck;

import java.util.ArrayList;

/**
 * The type Player.
 */
public class Player {
    public String getPlayerName () {
        return playerName;
    }

    public void setPlayerName (String playerName) {
        this.playerName = playerName;
    }

    private String playerName;

    /**
     * The Players id.
     */
    public int playersID = 0;
    /**
     * The Player card list.
     */
    public ArrayList<Card> playerCardList = new ArrayList<>();
    /**
     * The Is eliminated.
     */
    public boolean isEliminated = false;
    /**
     * The Is handmaid online.
     */
    public boolean isHandmaidOnline = false;
    /**
     * The Countess condition.
     */
    public boolean countessCondition = false;

    /**
     * Draw a card.
     *
     * @param theDrawingPlayer the the drawing player
     * @param thisRoundsDeck   the this rounds deck
     */
    public void drawACard(Player theDrawingPlayer, Deck thisRoundsDeck) {
        theDrawingPlayer.playerCardList.add(thisRoundsDeck.shuffledDeckList.get(0));
        thisRoundsDeck.shuffledDeckList.remove(0);
    }

    /**
     * Check the countess condition.
     *
     * @param currentPlayer the current player
     */
    public void checkTheCountessCondition(Player currentPlayer) {
        if (currentPlayer.playerCardList.get(0).cardNumber == 7 || currentPlayer.playerCardList.get(1).cardNumber == 7) {
            if (currentPlayer.playerCardList.get(0).cardNumber == 6 || currentPlayer.playerCardList.get(1).cardNumber == 6) {
                System.out.println("You have both the Countess and the King card in your hand. Press Enter to discard Countess");
                countessCondition = true;
            } else if (currentPlayer.playerCardList.get(0).cardNumber == 5 || currentPlayer.playerCardList.get(1).cardNumber == 5) {
                System.out.println("You have both the Countess and the Prince card in your hand. Press Enter to discard Countess");
                countessCondition = true;
            }
        }
        if (countessCondition) {
            try {
                System.in.read();
            } catch (Exception ignored) {
            }
            for (int i = 0; i < currentPlayer.playerCardList.size(); i++) {
                if (currentPlayer.playerCardList.get(i).cardNumber == 7) {
                    currentPlayer.playerCardList.remove(i);
                }
            }
            System.out.println("Countess has been discarded.");
        }
    }

    /**
     * Print the players hand.
     *
     * @param targetPlayer the target player
     */
    public void printThePlayersHand(Player targetPlayer) {
        for (int i = 0; i < targetPlayer.playerCardList.size(); i++) {
            System.out.println("(This player has " + targetPlayer.playerCardList.get(i).cardName + " card in hand.)");
        }
    }

}
