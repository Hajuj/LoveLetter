package cards;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    public ArrayList<Card> deckList = new ArrayList<>();
    public ArrayList<Card> shuffledDeckList = new ArrayList<>();
    public ArrayList<Card> discardedCards = new ArrayList<>();

    public Deck() {
        for (int i = 0; i < Guard.numberOfCardsInDeck; i++) {
            Guard guard = new Guard(Guard.cardNumber,Guard.numberOfCardsInDeck,Guard.cardText,Guard.cardName);
            deckList.add(guard);
        }
        for (int i = 0; i <Priest.numberOfCardsInDeck ; i++) {
            Priest priest = new Priest(Priest.cardNumber, Priest.numberOfCardsInDeck, Priest.cardText, Priest.cardName);
            deckList.add(priest);
        }
        for (int i = 0; i <Baron.numberOfCardsInDeck ; i++) {
            Baron baron = new Baron(Baron.cardNumber, Baron.numberOfCardsInDeck, Baron.cardText, Baron.cardName);
            deckList.add(baron);
        }
        for (int i = 0; i <Handmaid.numberOfCardsInDeck ; i++) {
            Handmaid handmaid = new Handmaid(Handmaid.cardNumber, Handmaid.numberOfCardsInDeck, Handmaid.cardText, Handmaid.cardName);
            deckList.add(handmaid);
        }
        for (int i = 0; i <Prince.numberOfCardsInDeck ; i++) {
            Prince prince = new Prince(Prince.cardNumber, Prince.numberOfCardsInDeck, Prince.cardText, Prince.cardName);
            deckList.add(prince);
        }
        for (int i = 0; i <King.numberOfCardsInDeck ; i++) {
            King king = new King(King.cardNumber, King.numberOfCardsInDeck, King.cardText, King.cardName);
            deckList.add(king);
        }
        for (int i = 0; i <Countess.numberOfCardsInDeck ; i++) {
            Countess countess = new Countess(Countess.cardNumber, Countess.numberOfCardsInDeck, Countess.cardText, Countess.cardName);
            deckList.add(countess);
        }
        for (int i = 0; i <Princess.numberOfCardsInDeck ; i++) {
            Princess princess = new Princess(Princess.cardNumber, Princess.numberOfCardsInDeck, Princess.cardText, Princess.cardName);
            deckList.add(princess);
        }
    }

    public void shuffleTheDeck () {
        while (deckList.size() > 0) {
            Random r = new Random();
            int  n = r.nextInt(deckList.size());
            shuffledDeckList.add(deckList.get(n));
            deckList.remove(n);
        }
    }

    public void printTestDeck () {
        for (Card card:deckList
        ) {
            System.out.println(card.cardName);
//            System.out.println(card.cardText);

        }
        System.out.println("====================");
        shuffleTheDeck();
        for (Card card:shuffledDeckList
        ) {
            System.out.println(card.cardName);
//            System.out.println(card.cardText);
        }
    }
}

