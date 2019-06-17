package magicthegathering.impl;

import magicthegathering.game.Generator;
import magicthegathering.game.Game;
import magicthegathering.game.Player;
import magicthegathering.game.CreatureCard;

/**
 * class represents game
 */
public class GameImpl implements Game {
    private Player[] players = new Player[2];

    /**
     * setting new players into players array
     * @param player1 first place in players array
     * @param player2 second place in players array
     */
    public GameImpl(Player player1,Player player2){
        players[0]=player1;
        players[1]=player2;
    }

    /**
     * setting players card using a generator
     */
    @Override
    public void initGame() {
        players[0].initCards(Generator.generateCards());
        players[1].initCards(Generator.generateCards());
    }

    /**
     * switching between current player and second
     */
    @Override
    public void changePlayer() {
        Player temp = players[0];
        players[0]=players[1];
        players[1]=temp;
    }

    /**
     * unsetting summoing sicknesses of creatures and untaping all cards
     */
    @Override
    public void prepareCurrentPlayerForTurn() {
        getCurrentPlayer().untapAllCards();
        getCurrentPlayer().prepareAllCreatures();
    }

    /**
     * current player
     * @return player which is going to play
     */
    @Override
    public Player getCurrentPlayer() {
        return players[0];
    }

    /**
     * second player
     * @return player who is defending
     */
    @Override
    public Player getSecondPlayer() {
        return players[1];
    }

    /**
     * all attacking creatures are tapped
     * @param creatures creatures which are going to attack
     */
    @Override
    public void performAttack(CreatureCard[] creatures) {
        for (CreatureCard creature : creatures){
            creature.tap();
        }
    }

    /**
     * few conditions - all attacking creatures must be in players cards on table,
     * there cant be a duplicate
     * and creature cant be tapped
     * and creature has not summoing sickness
     * @param attackingCreatures array of attacking creatures
     * @return true if conditions are ok, false if not
     */
    @Override
    public boolean isCreaturesAttackValid(CreatureCard[] attackingCreatures) {
        for(CreatureCard attackingCreature : attackingCreatures){
            if (attackingCreature.hasSummoningSickness() ||
                    !ArrayUtils.containsCard(attackingCreature,players[0].getCreaturesOnTable()) ||
                    ArrayUtils.hasDuplicatesExceptNull(players[0].getCreaturesOnTable()) ||
                    attackingCreature.isTapped()){
                return  false;
            }
        }
        return true;
    }

    /**
     * says if there can be a fight between some creatures
     * conditions - arrays of both must be the same lenght
     * not even one of arrays can have a duplicate except null
     * attacking creatures must be on table of player
     * blocking creatures must be on table of player and cant be tapped
     * @param attackingCreatures array of attacking creatures
     * @param blockingCreatures array of blocking creatures
     * @return true if all conditions are ok, false if not
     */
    @Override
    public boolean isCreaturesBlockValid(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures) {
        if (attackingCreatures.length != blockingCreatures.length ||
                ArrayUtils.hasDuplicatesExceptNull(attackingCreatures) ||
                ArrayUtils.hasDuplicatesExceptNull(blockingCreatures)){
            return false;
        }
        for (CreatureCard attackingCreture : attackingCreatures){
            if (!ArrayUtils.containsCard(attackingCreture,players[0].getCreaturesOnTable())){
                return false;
            }
        }
        for (CreatureCard blockingCreature : blockingCreatures){
            if (blockingCreature==null){
                continue;
            }
            if (!ArrayUtils.containsCard(blockingCreature, players[1].getCreaturesOnTable()) ||
                    blockingCreature.isTapped()){
                return false;
            }
        }
        return true;
    }

    /**
     * conditions if -
     * if blockingcreature on same position in array = null, dmg goes to player
     * if both of creatures (attacking and blockin) has higher power than toughness, both dies
     * if attacking has higher power than blocking tough, blocking dies
     * if blocking has higher than attacking, attacking diesad
     * @param attackingCreatures array of attacking creatures
     * @param blockingCreatures array of blocking creatures
     */
    @Override
    public void performBlockAndDamage(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures) {
        for (int i=0; i<attackingCreatures.length;i++){
            if (blockingCreatures[i] == null){
                players[1].subtractLives(attackingCreatures[i].getPower());
            } else if (attackingCreatures[i].getPower() >= blockingCreatures[i].getToughness() &&
                    blockingCreatures[i].getPower() >= attackingCreatures[i].getToughness()){
                players[0].destroyCreature(attackingCreatures[i]);
                players[1].destroyCreature(blockingCreatures[i]);
            } else if (attackingCreatures[i].getPower() >= blockingCreatures[i].getToughness()){
                players[1].destroyCreature(blockingCreatures[i]);
            } else if (blockingCreatures[i].getPower() >= attackingCreatures[i].getToughness()){
                players[0].destroyCreature(attackingCreatures[i]);
            }
        }
    }
}
