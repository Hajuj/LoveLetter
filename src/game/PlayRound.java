package game;

import cards.Deck;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The type Play round.
 */
public class PlayRound {

    private int numberOfPlayers;

    /**
     *
     *  DAS MACHT JETZT GAME BOT
     * Select number of players array list.
     *
     * @return the array list
     */
    public ArrayList<Player> selectNumberOfPlayers() {
        ArrayList<Player> listOfPlayers = new ArrayList();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select the number of players:");
        numberOfPlayers = scanner.nextInt();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player();
            listOfPlayers.add(player);
            player.playersID = i;
        }
        return listOfPlayers;
    }

    /**
     * End this round.
     */
    public void endThisRound() {
        System.out.println("This round has ended. Time to check each player's card score: ");
    }

    /**
     * Draw cards at the beginning.
     *
     * @param currentRoundsListOfPlayers the current rounds list of players
     * @param currentDeck                the current deck
     */
    public void drawCardsAtTheBeginning(ArrayList<Player> currentRoundsListOfPlayers, Deck currentDeck) {
        for (int i = 0; i < currentRoundsListOfPlayers.size(); i++) {
            currentRoundsListOfPlayers.get(i).drawACard(currentRoundsListOfPlayers.get(i), currentDeck);
        }
    }

    /**
     * Execute players phase.
     *
     * @param currentRoundsListOfPlayers the current rounds list of players
     * @param currentPlayer              the current player
     * @param currentDeck                the current deck
     */
    public void executePlayersPhase(ArrayList<Player> currentRoundsListOfPlayers, Player currentPlayer, Deck currentDeck) {
        currentPlayer.drawACard(currentPlayer, currentDeck);
        currentPlayer.checkTheCountessCondition(currentPlayer);
        currentPlayer.isHandmaidOnline = false;
        if (!currentPlayer.countessCondition) {
            System.out.println("Please select the card you'd like to play: ");
            for (int i = 0; i < currentPlayer.playerCardList.size(); i++) {
                int j = i + 1;
                System.out.println(j + ". " + currentPlayer.playerCardList.get(i).cardName);
                System.out.println("(" + currentPlayer.playerCardList.get(i).cardText + ")");
            }
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            currentPlayer.playerCardList.get(choice - 1).performCardInstruction(currentPlayer, currentRoundsListOfPlayers, currentDeck);
        } else {
            currentPlayer.countessCondition = false;
        }
    }


}
