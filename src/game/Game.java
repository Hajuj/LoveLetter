package game;

import cards.Deck;

import java.util.ArrayList;

/*      Integration mit Chatsystem
        Zuteilung der Connection zu Player 1-4 mit Namen mit busy waiting (while)
        Überarbeiten der Konsolenausgabe in 3 Teile:
        - Infos für den Spieler (Welche Karten zur auswahl etc) sendDirectMessage,
        - Infos für Alle (Punkte, wer ist am zug etc) sendBroadcastMessage
        - Infos für einen Gegner (Karte entwendet etc)
 */

/**
 * The type Game.
 */
public class Game {
    /**
     * Sets up the game.
     */
    //TODO 1: Score für Token and Winner!!!
    //TODO 2: Game-Modus for 2, 3, 4 Players ->
    public void setUpTheGame() {
        PlayRound playRound = new PlayRound();
        Deck currentDeck = new Deck();
        currentDeck.shuffleTheDeck();
        ArrayList<Player> listOfPlayers = playRound.selectNumberOfPlayers();
        playRound.drawCardsAtTheBeginning(listOfPlayers, currentDeck);
        while (currentDeck.shuffledDeckList.size() > 0) {
            for (int i = 0; i < listOfPlayers.size(); i++) {
                int j = i + 1;
                if (listOfPlayers.get(i).isEliminated) {
                    System.out.println("Player " + j + " have already been eliminated. Proceeding to next player.");
                } else {
                    System.out.println("=====NEXT PLAYER=====");
                    System.out.println("Current Player: " + j);
                    playRound.executePlayersPhase(listOfPlayers, listOfPlayers.get(i), currentDeck);
                    listOfPlayers.get(i).printThePlayersHand(listOfPlayers.get(i));
                }
            }
        }

    }
}

