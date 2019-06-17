package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.ManaType;

import java.util.Arrays;

/**
 * class represents creature card
 */
public class CreatureCardImpl extends AbstractCard implements CreatureCard {
    private String creatureName;
    private ManaType[] creatureCost;
    private int power;
    private int toughness;
    private boolean summoingSickness;

    /**
     * constructor making a creature card
     * @param creatureName name of creature
     * @param creatureCost cost made of mana type
     * @param power the attack of creature
     * @param toughness defence of creature
     */
    public CreatureCardImpl(String creatureName,ManaType[] creatureCost, int power, int toughness){
        this.creatureName = creatureName;
        this.creatureCost = creatureCost;
        this.power = power;
        this.toughness = toughness;
    }

    /**
     * gets number of sum of all lands needed
     * @return int number
     */
    @Override
    public int getTotalCost() {
        return creatureCost.length;
    }

    /**
     * gets how many special types of mana creature costs
     * @param mana mana declares whast kind of mana it counts
     * @return int number of special manatype neeeded
     */
    @Override
    public int getSpecialCost(ManaType mana) {
        int counter=0;
        for (ManaType specialManaType : creatureCost){
            if (specialManaType == mana){
                counter++;
            }
        }
        return counter;
    }

    /**
     * name
     * @return creature's name
     */
    @Override
    public String getName() {
        return creatureName;
    }

    /**
     * poweer
     * @return creature's power
     */
    @Override
    public int getPower() {
        return power;
    }

    /**
     * toughness
     * @return creature's toughness
     */
    @Override
    public int getToughness() {
        return toughness;
    }

    /**
     * boolean method detecting if the monster has summoing sickness
     * @return true if it has flase if it has not
     */
    @Override
    public boolean hasSummoningSickness() {
        return summoingSickness;
    }

    /**
     * setting summoingSickness
     */
    @Override
    public void setSummoningSickness() {
        this.summoingSickness=true;
    }

    /**
     * unsetting summoing sickness (summoingSickness = false)
     */
    @Override
    public void unsetSummoningSickness() {
        this.summoingSickness=false;
    }

    /**
     *
     * @return to string name + cost + power / toughness + if it has not summoing sickness "can attack" + if it
     * is tapped "TAPPED"
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(creatureName).append(" ");
        sb.append(Arrays.toString(creatureCost)).append(" ");
        sb.append(power).append(" / ");
        sb.append(toughness);
        sb.append(!hasSummoningSickness() ? " can attack" : "");
        sb.append(isTapped() ? " TAPPED" : "");
        return sb.toString();
    }
}

