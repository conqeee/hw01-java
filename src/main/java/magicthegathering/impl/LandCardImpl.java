package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.LandCard;
import magicthegathering.game.LandCardType;
import magicthegathering.game.ManaType;

/**
 * class represents land cards
 */
public class LandCardImpl extends AbstractCard implements LandCard {
    private LandCardType landCardType;

    /**
     * constructor of landcardimpl
     * @param landCardType setting card's land type
     */
    public LandCardImpl(LandCardType landCardType){
        this.landCardType=landCardType;
    }

    /**
     * getting land type
     * @return land type of card
     */
    @Override
    public LandCardType getLandType() {
        return landCardType;
    }

    /**
     * returns the mana type
     * @return returns the mana type based on landcard type
     */
    @Override
    public ManaType getManaType() {
        return ManaType.values()[landCardType.ordinal()];
    }

    /**
     * to string method land type written with lower cases
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Land ");
        sb.append(getLandType().toString().toLowerCase()).append(", ");
        sb.append(getManaType().toString());
        return sb.toString();
    }
}
