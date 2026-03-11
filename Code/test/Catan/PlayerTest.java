package Catan;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Player hierarchy (AIPlayer/HumanPlayer) and resource management.
 */
class PlayerTest {

    @Test
    void addDesertResourceDoesNothing() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.DESERT, 5);
        assertEquals(0, p.handSize());
    }

    @Test
    void hasResourcesReturnsTrueWhenSufficient() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.BRICK, 1);
        p.addResources(ResourceType.LUMBER, 1);
        p.addResources(ResourceType.WOOL, 1);
        p.addResources(ResourceType.GRAIN, 1);
        assertTrue(p.hasResources(Cost.settlement()));
    }

    @Test
    void payReducesHandSize() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.BRICK, 1);
        p.addResources(ResourceType.LUMBER, 1);
        p.pay(Cost.road());
        assertEquals(0, p.handSize());
    }

    @Test
    void payThrowsWhenInsufficient() {
        Player p = new AIPlayer(1);
        assertThrows(IllegalStateException.class, () -> p.pay(Cost.road()));
    }

    @Test
    void discardHalfCardsWhenOverSeven() {
        Player p = new AIPlayer(1);
        // Add 10 cards
        p.addResources(ResourceType.BRICK, 5);
        p.addResources(ResourceType.LUMBER, 5);
        assertEquals(10, p.handSize());

        p.discardHalfCards(new Random(42));
        // 10 / 2 = 5 discarded, 5 remaining
        assertEquals(5, p.handSize());
    }

    @Test
    void discardHalfCardsNoEffectWhenSevenOrFewer() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.BRICK, 7);
        assertEquals(7, p.handSize());

        p.discardHalfCards(new Random(42));
        // Should not discard when exactly 7
        assertEquals(7, p.handSize());
    }


    @Test
    void removeRandomCardFromEmptyHandReturnsNull() {
        Player p = new AIPlayer(1);
        ResourceCard card = p.removeRandomCard(new Random(0));
        assertNull(card);
    }

    @Test
    void getHandReturnsUnmodifiableView() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.GRAIN, 2);
        assertThrows(UnsupportedOperationException.class,
                () -> p.getHand().add(new ResourceCard(ResourceType.BRICK)));
    }

    @Test
    void addResourcesNegativeCountThrows() {
        Player p = new AIPlayer(1);
        assertThrows(IllegalArgumentException.class,
                () -> p.addResources(ResourceType.BRICK, -1));
    }
}
