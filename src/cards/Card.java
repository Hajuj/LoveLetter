package cards;

/**
 * Enum with all possible cards.
 */
public enum Card {
    GUARD("Guard", 1),
    PRIEST("Priest", 2),
    BARON("Baron", 3),
    HANDMAIDEN("Handmaiden", 4),
    PRINCE("Prince", 5),
    KING("King", 6),
    COUNTESS("Countess", 7),
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
        return this.name + " (" + value + ")";
    }
}
