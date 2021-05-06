package cards;

/**
 * Enum with all possible cards.
 */
public enum Card {
    /**
     * Guard card.
     */
    GUARD("Guard", 1),
    /**
     * Priest card.
     */
    PRIEST("Priest", 2),
    /**
     * Baron card.
     */
    BARON("Baron", 3),
    /**
     * Handmaiden card.
     */
    HANDMAIDEN("Handmaiden", 4),
    /**
     * Prince card.
     */
    PRINCE("Prince", 5),
    /**
     * King card.
     */
    KING("King", 6),
    /**
     * Countess card.
     */
    COUNTESS("Countess", 7),
    /**
     * Princess card.
     */
    PRINCESS("Princess", 8);

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
     * Constructor for a card object.
     *
     * @param name  the name of the card
     * @param value the value of the card
     */
    Card(String name, int value) {
        this.name = name;
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
        return this.name + " (" + value + ") ";
    }
}
