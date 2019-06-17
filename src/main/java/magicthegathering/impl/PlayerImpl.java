package magicthegathering.impl;

import magicthegathering.game.Player;
import magicthegathering.game.Card;
import magicthegathering.game.LandCard;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.ManaType;



/**
 * class represents one player of m.t.g.
 */
public class PlayerImpl implements Player {
    private String playerName;
    private int healthPool;
    private Card[] playerCards;

    /**
     * constructor for making a player
     * setting the players name and healthpool
     * @param playerName player's name
     */
    public PlayerImpl(String playerName){
        this.playerName=playerName;
        this.healthPool=INIT_LIVES;
    }

    /**
     * getting players name
     * @return name
     */
    @Override
    public String getName() {
        return playerName;
    }

    /**
     * getting players health
     * @return health
     */
    @Override
    public int getLife() {
        return healthPool;
    }

    /**
     * changing - subtracting players lives
     * @param lives lives = how many hp we want to subtract
     */
    @Override
    public void subtractLives(int lives) {
        this.healthPool-=lives;
    }

    /**
     * boolean which says if the player is dead or not (health is under or equals to 0)
     * @return
     */
    @Override
    public boolean isDead() {
        return healthPool<=0;
    }

    /**
     * initialising player's cards
     * @param cards to be put into player's hand
     */
    @Override
    public void initCards(Card[] cards) {
        playerCards=cards;
    }

    /**
     * getting the player's in hand cards
     * @return array of in hand cards
     */
    @Override
    public Card[] getCardsInHand() {
        return ArrayUtils.filterInHand(playerCards);
    }

    /**
     * getting the player's on table cards
     * @return array of on table cards
     */
    @Override
    public Card[] getCardsOnTable() {
        return ArrayUtils.filterOnTable(playerCards);
    }

    /**
     * players lands on table
     * @return lands on table
     */
    @Override
    public LandCard[] getLandsOnTable() {
        return ArrayUtils.filterLands(getCardsOnTable());
    }

    /**
     * players cretures on table
     * @return creatures on table
     */
    @Override
    public CreatureCard[] getCreaturesOnTable() {
        return ArrayUtils.filterCreatures(getCardsOnTable());
    }

    /**
     * players lands in hands
     * @return lands in hands
     */
    @Override
    public LandCard[] getLandsInHand() {
        return ArrayUtils.filterLands(getCardsInHand());
    }

    /**
     * players creatures in hands
     * @return creatures in hands
     */
    @Override
    public CreatureCard[] getCreaturesInHand() {
        return ArrayUtils.filterCreatures(getCardsInHand());
    }

    /**
     * untaping all cards which are on the table
     */
    @Override
    public void untapAllCards() {
        for (Card card : getCardsOnTable()){
            card.untap();
        }
    }

    /**
     * preparing all creatures by unsetting summoing sicknesses
     */
    @Override
    public void prepareAllCreatures() {
        for (CreatureCard card : getCreaturesOnTable()){
            card.unsetSummoningSickness();
        }
    }

    /**
     * boolean which says if you can put land on table
     * if its in players hand and is not on table
     * @param landCard land to be put on the table
     * @return true if you can, false if not
     */
    @Override
    public boolean putLandOnTable(LandCard landCard) {
        if(ArrayUtils.containsCard(landCard,getCardsInHand()) && !landCard.isOnTable()){
            landCard.putOnTable();
            return true;
        }
        return false;
    }

    /**
     * taping lands
     *puting on table
     * setting summoing sickness
     * @param creatureCard creature card to be put on the table
     * @return true if you can put false if you cant
     */
    @Override
    public boolean putCreatureOnTable(CreatureCard creatureCard) {
        if(ArrayUtils.containsCard(creatureCard,getCardsInHand()) && !creatureCard.isOnTable() &&
                hasManaForCreature(creatureCard)){
            tapManaForCreature(creatureCard);
            creatureCard.putOnTable();
            creatureCard.setSummoningSickness();

            return true;
        }
        return false;
    }

    /**
     * calculates if player has enough mana - comparing to determined position of int array
     * @param creature creature to be checked
     * @return true if player has mana, false if not
     */
    @Override
    public boolean hasManaForCreature(CreatureCard creature) {
        return(creature.getSpecialCost(ManaType.WHITE)<=calculateUntappedLands()[0] &&
                creature.getSpecialCost(ManaType.RED)<=calculateUntappedLands()[1] &&
                creature.getSpecialCost(ManaType.GREEN)<=calculateUntappedLands()[2] &&
                creature.getSpecialCost(ManaType.BLUE)<=calculateUntappedLands()[3] &&
                creature.getSpecialCost(ManaType.BLACK)<=calculateUntappedLands()[4]);
    }

    /**
     * creating an array size 5 (5 different types)
     * putting into array numbers depending on how many untapped lands of each type we have
     * @return int array saying how many of what type we have
     */
    @Override
    public int[] calculateUntappedLands() {
        int[] lands = new int[5];
        for (LandCard card : getLandsOnTable()){
            if (!card.isTapped()){
                lands[card.getManaType().ordinal()]+=1;
            }
        }
        return lands;
    }

    /**
     * goes through lands and taping the needed ones (land card cant be tapped and we cant tap more than the special
     * cost is
     * @param creature creature which price needs to be paid
     */
    @Override
    public void tapManaForCreature(CreatureCard creature) {
        int counter = 0;
        for(LandCard landCard : getLandsOnTable()){
            if (counter < creature.getSpecialCost(landCard.getManaType()) && !landCard.isTapped()){
                counter += landCard.getManaType().ordinal();
                landCard.tap();
            }
        }
    }

    /**
     * setting new playerCards array
     * @param creature creature to be removed.
     */
    @Override
    public void destroyCreature(CreatureCard creature) {
        playerCards = ArrayUtils.removeCard(creature, playerCards);

    }

    /**
     * to string
     * @return name + healthpool
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Marek(");
        sb.append(healthPool).append(")");
        return sb.toString();
    }
}
