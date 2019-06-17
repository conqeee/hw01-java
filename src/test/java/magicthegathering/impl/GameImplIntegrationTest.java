package magicthegathering.impl;

import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.Game;
import magicthegathering.game.ManaType;
import magicthegathering.game.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Zuzana Wolfova
 */
public class GameImplIntegrationTest {

    private Player first;
    private Player second;
    private Game game;
    private CreatureCard creature1;
    private CreatureCard creature2;
    private CreatureCard creature3;
    private CreatureCard creature4;

    @Before
    public void setUp() {
        first = new PlayerImpl("Marek");
        second = new PlayerImpl("Zuzka");
        game = new GameImpl(first, second);

        creature1 = new CreatureCardImpl("Artifact creature",
                new ManaType[]{},
                0,
                2);
        creature2 = new CreatureCardImpl("Hybrid creature",
                new ManaType[]{},
                1,
                1);
        creature3 = new CreatureCardImpl("Strong creature",
                new ManaType[]{},
                1,
                2);
        creature4 = new CreatureCardImpl("Weak creature",
                new ManaType[]{},
                0,
                1);
        first.initCards(new Card[]{creature1, creature2});
        second.initCards(new Card[]{creature3, creature4});
    }

    @Test
    public void creatureAttackNotBelongingToCurrentPlayer() {
        CreatureCard c = new CreatureCardImpl("Rogue",
                new ManaType[]{ManaType.BLACK},
                2,
                1);
        c.putOnTable();
        c.unsetSummoningSickness();
        assertFalse(game.isCreaturesAttackValid(new CreatureCard[]{c}));
    }

    @Test
    public void correctAttack() {
        prepareCreatureForAttack(creature1);
        assertTrue(game.isCreaturesAttackValid(new CreatureCard[]{creature1}));
    }

    private void prepareCreatureForAttack(CreatureCard creature) {
        game.getCurrentPlayer().putCreatureOnTable(creature);
        creature.unsetSummoningSickness();
    }

    @Test
    public void blockHasDifferentArraysLength() {
        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{null, null}));
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{}));
    }

    @Test
    public void attackOrBlockHasDuplicateCreatures() {
        prepareAttackAndBlock();

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature1},
                new CreatureCard[]{null, null}));

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2, creature2},
                new CreatureCard[]{null, null, creature3}));

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature3, creature3}));
    }

    private void prepareAttackAndBlock() {
        prepareAttack(game.getCurrentPlayer());
        prepareAttack(game.getSecondPlayer());
    }

    private void prepareAttack(Player p) {
        for (CreatureCard c : p.getCreaturesInHand()) {
            p.putCreatureOnTable(c);
            c.unsetSummoningSickness();
        }
    }

    @Test
    public void attackOrBlockCreaturesAreNotOnTable() {
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{null}));
        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{creature3}));
    }

    @Test
    public void attackOrBlockCreaturesDoesNotBelongToPlayer() {
        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{new CreatureCardImpl("Orc", new ManaType[]{}, 1, 1)},
                new CreatureCard[]{null}));

        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1},
                new CreatureCard[]{new CreatureCardImpl("Orc", new ManaType[]{}, 1, 1)}));
    }

    @Test
    public void blockingWithTappedCreature() {
        prepareAttackAndBlock();
        creature3.tap();

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature4, creature3}));

    }

    @Test
    public void correctBlock() {
        prepareAttackAndBlock();

        assertTrue(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature4, creature3}));

        assertTrue(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{null, null}));
    }

    @Test
    public void nonBlockedCreaturesDamagePlayer() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{null, null});
        assertEquals(Player.INIT_LIVES - creature1.getPower() - creature2.getPower(),
                game.getSecondPlayer().getLife());
    }


    @Test
    public void blockingCreatureShouldDie() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature2},
                new CreatureCard[]{creature4});
        assertFalse(ArrayUtils.containsCard(creature4, game.getSecondPlayer().getCreaturesOnTable()));
        assertTrue(ArrayUtils.containsCard(creature2, game.getCurrentPlayer().getCreaturesOnTable()));
    }

    @Test
    public void blockedCreatureShouldDie() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature2},
                new CreatureCard[]{creature3});
        assertFalse(ArrayUtils.containsCard(creature2, game.getCurrentPlayer().getCreaturesOnTable()));
        assertTrue(ArrayUtils.containsCard(creature3, game.getSecondPlayer().getCreaturesOnTable()));
    }

}
