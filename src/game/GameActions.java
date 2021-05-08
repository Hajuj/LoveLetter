package game;

import cards.Card;
import cards.Deck;
import chat.BotClient;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * The possible player actions to be taken during the game.
 */
abstract class GameActions {


    /**
     * When you discard the Guard, choose a player and name a number (other than 1).
     * If that player has that number in their hand, that player is knocked out of the round.
     * If all other players still in the round cannot be chosen
     * (eg. due to Handmaid or Sycophant), this card is discarded without effect.
     *
     * @param botClient the bot client
     * @param user      the user
     * @param opponent  the targeted player
     */
    void useGuard(BotClient botClient, Player user, Player opponent) {
        ArrayList<String> cardNames = new ArrayList<>(Arrays.asList(Card.CARD_NAMES));
        cardNames.remove(0);
        botClient.sendTextMessage("@" + user.getName() + " Which card would you like to guess: ");
        int index = 2;
        for (String s : cardNames) {
            botClient.sendTextMessage("@" + user.getName() + " " + (index++) + ": " + s);
        }
        botClient.sendTextMessage("@" + user.getName() + " " + "Please write the Number of the Card!");
        synchronized (botClient.getCurrentCards()) {
            try {
                botClient.getCurrentCards().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int card = botClient.getCurrentCards().get(user) - 2;
        String cardName = cardNames.get(card);
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
            opponent.discardCard();
            botClient.sendToAllPlayers(opponent.getName() + " is eliminated!");

        } else {
            botClient.sendTextMessage("@" + user.getName() + " You have guessed incorrectly.");
        }
    }

    /**
     * When you discard the Priest, you can look at another player’s hand.
     * Do not reveal the hand to any other players.
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
     * When you discard the Baron, choose another player still in the round.
     * You and that player secretly compare your hands. The player with the lower number
     * is knocked out of the round. In case of a tie, nothing happens.
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
            opponent.discardCard();
            botClient.sendToAllPlayers(opponent.getName() + " is eliminated!");
        } else if (cardComparison < 0) {
            botClient.sendTextMessage("@" + user.getName() + " You have lost the comparison.");
            user.discardCard();
        } else {
            botClient.sendTextMessage("@" + user.getName() + " You have the same card!");
        }
    }

    /**
     * When you discard the Handmaid, you are immune to the effects of other players’ cards
     * until the start of your next turn. If all players other than the player
     * whose turn it is are protected by the Handmaid, the player must choose him-
     * or herself for a card’s effects, if possible.
     *
     * @param botClient the bot client
     * @param user      the current player
     */
    void useHandmaiden(BotClient botClient, Player user) {
        //TODO Prio 1: Bei Zwei Spielern führt es zu einer endlosschleife
        botClient.sendTextMessage("@" + user.getName() + " You are now protected until your next turn.");
        user.switchProtection();
    }

    /**
     * When you discard Prince Arnaud, choose one player still in the round
     * (including yourself). That player discards his or her hand
     * (but doesn't apply its effect, unless it is the Princess, see page 8)
     * and draws a new one. If the deck is empty and the player cannot draw a card,
     * that player draws the card that was removed at the start of the round.
     * If all other players are protected by the Handmaid, you must choose yourself.
     *
     * @param opponent the targeted player
     * @param d        the deck of cards
     */
    void usePrince(Player opponent, Deck d) {
        opponent.discardCard();
        if (d.hasMoreCards()) {
            opponent.hand().add(d.dealCard());
        }
    }

    /**
     * When you discard King Arnaud IV, trade the card in your hand with the card
     * held by another player of your choice. You cannot trade with a player who is
     * out of the round.
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
     * If you discard the Princess—no matter how or why—she has tossed your letter
     * into the fire. You are immediately knocked out of the round. If the Princess
     * was discarded by a card effect, any remaining effects of that card do not
     * apply (you do not draw a card from the Prince, for example). Effects tied to
     * being knocked out the round still apply (eg. Constable, Jester), however.
     *
     * @param user the current player
     */
    void usePrincess(BotClient botClient, Player user) {
        user.discardCard();
        botClient.sendToAllPlayers(user.getName() + " is eliminated!");

    }

}
