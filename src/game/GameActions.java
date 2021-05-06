package game;

import cards.Card;
import cards.Deck;
import chat.BotClient;

import java.util.ArrayList;
import java.util.Arrays;

// TODO eingaben mit Guard

/**
 * The possible player actions to be taken during the game.
 */
abstract class GameActions {

    // TODO beschreibung der Funktionen von jeder Karte

    /**
     * Allows the user to guess a card that a player's hand contains (excluding another guard).
     * If the user is correct, the opponent loses the round and must lay down their card.
     * If the user is incorrect, the opponent is not affected.
     *
     * @param botClient the bot client
     * @param user      the user
     * @param opponent  the targeted player
     */
    void useGuard(BotClient botClient, Player user, Player opponent) {
        ArrayList<String> cardNames = new ArrayList<>(Arrays.asList(Card.CARD_NAMES));
        cardNames.remove(0);
        botClient.sendTextMessage("@" + user.getName() + " Which card would you like to guess (other than Guard): ");
        int index = 1;
        for (String s : cardNames) {
            botClient.sendTextMessage("@" + user.getName() + " " + (index++) + ": " + s);
        }
        // TODO nicht den Guard ausgeben


        synchronized (botClient.getCurrentCards()) {
            try {
                botClient.getCurrentCards().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int card = botClient.getCurrentCards().get(user);

        String cardName = cardNames.get(card);


        // TODO change from cardName to cardNumber
        while (!cardNames.contains(cardName.toLowerCase()) || cardName.equalsIgnoreCase("guard")) {
            botClient.sendTextMessage("@" + user.getName() + " Invalid card name \n Which card would you like to guess (other than Guard): ");

            synchronized (botClient.getCurrentCards()) {
                try {
                    botClient.getCurrentCards().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int newCard = botClient.getCurrentCards().get(user);

            cardName = cardNames.get(newCard);
        }

        Card opponentCard = opponent.hand().peek(0);
        if (opponentCard.getName().equalsIgnoreCase(cardName)) {
            botClient.sendTextMessage("@" + user.getName() + " You have guessed correctly!");

            opponent.lose();
        } else {
            botClient.sendTextMessage("@" + user.getName() + " You have guessed incorrectly.");

        }
    }

    /**
     * Allows the user to peek at the card of an opposing player.
     *
     * @param botClient the bot client
     * @param user      the user
     * @param opponent  the targeted player
     */
    void usePriest(BotClient botClient, Player user, Player opponent) {
        Card opponentCard = opponent.hand().peek(0);
        botClient.sendTextMessage("@" + user.getName() + " " + opponent.getName() + " shows you a " + opponentCard);

    }

    /**
     * Allows the user to compare cards with an opponent.
     * If the user's card is of higher value, the opposing player loses the round and their card.
     * If the user's card is of lower value, the user loses the round and their card.
     * If the two players have the same card, their used pile values are compared in the same manner.
     *
     * @param botClient the bot client
     * @param user      the initiator of the comparison
     * @param opponent  the targeted player
     */
    void useBaron(BotClient botClient, Player user, Player opponent) {
        Card userCard = user.hand().peek(0);
        Card opponentCard = opponent.hand().peek(0);

        int cardComparison = Integer.compare(userCard.value(), opponentCard.value());
        if (cardComparison > 0) {
            botClient.sendTextMessage("@" + user.getName() + " You have won the comparison!");

            opponent.lose();
            botClient.sendTextMessage(opponent + " is eliminated!");


        } else if (cardComparison < 0) {
            botClient.sendTextMessage("@" + user.getName() + " You have lost the comparison.");

            user.lose();
        } else {
            botClient.sendTextMessage("@" + user.getName() + " You have the same card!");

            // it is not in the rules
//            if (opponent.used().value() > user.used().value()) {
//                System.out.println("You have lost the used pile comparison");
//                user.lose();
//            } else {
//                System.out.println("You have won the used pile comparison");
//                opponent.lose();
//            }
        }
    }

    /**
     * Switches the user's protection for one turn. This protects them from being targeted.
     *
     * @param botClient the bot client
     * @param user      the current player
     */
    void useHandmaiden(BotClient botClient, Player user) {
        //TODO Bei Zwei Spielern führt es zu einer endlosschleife
        botClient.sendTextMessage("@" + user.getName() + " You are now protected until your next turn.");

        user.switchProtection();
    }

    /**
     * Makes an opposing player lay down their card in their used pile and draw another.
     *
     * @param opponent the targeted player
     * @param d        the deck of cards
     */
    void usePrince(Player opponent, Deck d) {
        opponent.lose();
        if (d.hasMoreCards()) {
            opponent.hand().add(d.dealCard());
        }
    }

    /**
     * Allows the user to switch cards with an opponent.
     * Swaps the user's hand for the opponent's.
     *
     * @param user     the initiator of the swap
     * @param opponent the targeted player
     */
    void useKing(Player user, Player opponent) {
        Card userCard = user.hand().remove(0);
        Card opponentCard = opponent.hand().remove(0);
        user.hand().add(opponentCard);
        opponent.hand().add(userCard);
    }

    /**
     * If the princess is played, the user loses the round and must lay down their hand.
     *
     * @param user the current player
     */
    void usePrincess(Player user) {
        user.lose();
    }

}
