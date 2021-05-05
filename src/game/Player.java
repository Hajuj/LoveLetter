package game;

import cards.*;

/**
 * Class representing a Player of Love Letter.
 */
public class Player {

    /**
     * The name of the player.z
     */
    // TODO add player ID
    private String name;
    /**
     * Hand of cards the player possesses.
     */
    private Hand hand;
    /**
     * UsedPile of cards the player has used.
     */
    private UsedPile used;
    /**
     * True if the player is protected by a handmaiden, false if not.
     */
    private boolean isProtected;
    /**
     * The number of blocks the player has won.
     */
    // TODO change name letterCount
    private int blockCount;

    /**
     * Public constructor for Player object.
     *
     * @param name
     *          the player name
     */
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.used = new UsedPile();
        this.isProtected = false;
        this.blockCount = 0;
    }

    /**
     * Adds one block to the players block count.
     */
    // TODO change to add addRoundWinner
    public void addBlock() {
        this.blockCount++;
    }

    /**
     * Indicates that a player has lost the game by removing
     * the last card from the hand and placing it in used.
     */
    // TODO CHANGE THE FUCKING NAME!!!!
    public void lose() {
        this.used.add(this.hand.remove(0));
    }

    /**
     * Switches the user's level of protection.
     */
    public void switchProtection() {
        this.isProtected = !this.isProtected;
    }

    /**
     * Gets the hand of the player.
     *
     * @return this.hand
     */
    public Hand hand() {
        return this.hand;
    }

    /**
     * Gets the used pile of the player.
     *
     * @return this.used
     */
    public UsedPile used() {
        return this.used;
    }

    /**
     * Checks to see if the user is protected by a handmaiden.
     *
     * @return true, if the player is protected, false if not
     */
    public boolean isProtected() {
        return this.isProtected;
    }

    /**
     * Getter for the user's block count.
     *
     * @return player's block count
     */
    public int getBlockCount() {
        return this.blockCount;
    }

    /**
     * Getter for the user's name.
     *
     * @return player's name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.blockCount + ")";
    }
}
