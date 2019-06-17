package magicthegathering.impl;

import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.LandCard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * class full of usefull methods
 * used for works with arrays
 */
public class ArrayUtils {

    /**
     * filters just lands out of all cards
     * @param cards array of cards
     * @return array of landcards
     */
    public static LandCard[] filterLands(Card[] cards){
        List<LandCard> landCards = new ArrayList<>();
        for(Card card : cards){
            if (card instanceof LandCard){
                landCards.add((LandCard) card);
            }
        }
        return landCards.toArray(new LandCard[landCards.size()]);
    }

    /**
     * filters just creatures out of all cards
     * @param cards array of all cards
     * @return array of creatures
     */
    public static CreatureCard[] filterCreatures(Card[] cards){
        List<CreatureCard> creatureCards = new ArrayList<>();
        for(Card card : cards){
            if(card instanceof CreatureCard){
                creatureCards.add((CreatureCard) card);
            }
        }
        return creatureCards.toArray(new CreatureCard[creatureCards.size()]);
    }

    /**
     * filters cards that are in player hands
     * @param cards all cards
     * @return array of cards in hands
     */
    public static Card[] filterInHand(Card[] cards){
        List<Card> allInHand = new ArrayList<>();
        for (Card card : cards){
            if(!card.isOnTable()){
                allInHand.add(card);
            }
        }
        return allInHand.toArray(new Card[allInHand.size()]);
    }

    /**
     * filters cards that are on table
     * @param cards all cards
     * @return filtered on table cards
     */
    public static Card[] filterOnTable(Card[] cards){
        List<Card> allOnTable = new ArrayList<>();
        for (Card card : cards){
            if(card.isOnTable()){
                allOnTable.add(card);
            }
        }
        return allOnTable.toArray(new Card[allOnTable.size()]);
    }

    /**
     * founds out if there is some dupliace in array
     * using a set because of .contains method
     * @param cards array where are we searching
     * @return true if there is a duplicate false if not
     *
     */
    public static boolean hasDuplicatesExceptNull(Card[] cards){
        Set<Card> setOfCards = new HashSet<>();
        for (Card card : cards){
            if (card==null){
                continue;
            }
            if(setOfCards.contains(card)){
                return true;
            }
            setOfCards.add(card);

        }
        return false;
    }

    /**
     * boolean method declaring if the card is in array of (players) cards
     * @param searchedCard special card -needed
     * @param cards all cards
     * @return true if contains/ false if not
     */
    public static boolean containsCard(Card searchedCard, Card[] cards){
        for(Card card : cards){
            if(searchedCard==card){
                return true;
            }
        }
        return false;
    }

    /**
     * return int number of the position of special card in some array
     * @param searchedCard special card
     * @param cards array of cards
     * @return number of position in array
     */
    public static int findCardIndex(Card searchedCard, Card[] cards){

        for (int i=0; i < cards.length; i++){
            if(cards[i]==searchedCard){
                return i;
            }
        }
        return -1;
    }

    /**
     * removing a card which is probably dying by using dynamic list - we are adding in all wanted cards
     * not putting in just unwanted one
     * @param unwantedCard the card we want to remove
     * @param cards array from where we want to remove card
     * @return new array without the unwanted card
     */
    public static Card[] removeCard(Card unwantedCard, Card[] cards){
        List<Card> cardsAfterRemoving = new ArrayList<>();
        for (Card card : cards){
            if(card !=unwantedCard){
                cardsAfterRemoving.add(card);
            }
        }
        return cardsAfterRemoving.toArray(new Card[cardsAfterRemoving.size()]);
    }
}
