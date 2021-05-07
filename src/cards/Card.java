package cards;

/**
 * Enum with all possible cards.
 */
// TODO Beschreibung bzw. Text der Karten-Funktion überprüfen
public enum Card {
    /**
     * Guard card.
     */
    GUARD("Guard","guess the card of your chosen opponent",1),
    /**
     * Priest card.
     */
    PRIEST("Priest","show the opponent's card",2),
    /**
     * Baron card.
     */
    BARON("Baron","compare the card with your chosen opponent" ,3),
    /**
     * Handmaiden card.
     */
    HANDMAIDEN("Handmaiden","protect yourself for a round" ,4),
    /**
     * Prince card.
     */
    PRINCE("Prince","chose one player or yourself - discard the card and draws a new one" ,5),
    /**
     * King card.
     */
    KING("King", "trade the card with your chosen opponent",6),
    /**
     * Countess card.
     */
    COUNTESS("Countess","discard the card, if you have King or Prince too" ,7),
    /**
     * Princess card.
     */
    PRINCESS("Princess","if you discard it, you knocked out of the round" ,8);

    /**
     * All possible card names.
     */
    public static final String[] CARD_NAMES = {
            "guard",
            "priest",
            "baron",
            "handmaiden",
            "prince",
            "king",
            "countess",
            "princess"
    };
    /**
     * The name of the card.
     */
    private final String name;
    /**
     * The value of the card.
     */
    private final int value;
    /**
     * The describe of the card
     */
    private final String describe;

    /**
     * Constructor for a card object.
     *
     * @param name  the name of the card
     * @param value the value of the card
     */
    Card(String name, String describe, int value) {
        this.name = name;
        this.describe = describe;
        this.value = value;
    }

    /**
     * Getter for the value of the card.
     *
     * @return the card value
     */
    public int value() {
        return this.value;
    }

    /**
     * Getter for the name of the card.
     *
     * @return the card name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "\n ->" + this.name  + " (" + value + ") " + " - " + describe;
    }
}
