package game;

import cards.Deck;
import chat.BotClient;

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

    private BotClient botClient;
    private ArrayList<Player> listOfPlayers;

    public void setUpTheGame(ArrayList<Player> listOfPlayers, BotClient botClient) {
        this.botClient = botClient;
        PlayRound playRound = new PlayRound(botClient);
        Deck currentDeck = new Deck();
        currentDeck.shuffleTheDeck();
        this.listOfPlayers = listOfPlayers;
        playRound.drawCardsAtTheBeginning(listOfPlayers, currentDeck);
        while (currentDeck.shuffledDeckList.size() > 0) {
            for (int i = 0; i < listOfPlayers.size(); i++) {
                int j = i + 1;
                if (listOfPlayers.get(i).isEliminated) {
                    botClient.sendTextMessage("@" + listOfPlayers.get(i).getPlayerName() + " you have already been eliminated.");
     //             System.out.println("Player " + listOfPlayers.get(i).getPlayerName() + " have already been eliminated. Proceeding to next player.");
                } else {
                   // System.out.println("=====NEXT PLAYER=====");
                   // System.out.println("Current Player: " + listOfPlayers.get(i).getPlayerName());
                    botClient.sendTextMessage(("@" + listOfPlayers.get(i).getPlayerName() + "your turn!"));
                    playRound.executePlayersPhase(listOfPlayers, listOfPlayers.get(i), currentDeck);
                    listOfPlayers.get(i).printThePlayersHand(listOfPlayers.get(i));
                }
            }
        }

    }
}

