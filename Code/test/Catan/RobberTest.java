package Catan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Robber class.
 */
class RobberTest {

    @Test
    void robberStartsOnInitialTile() {
        Robber robber = new Robber(16);
        assertEquals(16, robber.getTileId());
    }


    @Test
    void robberMoveUpdatesIsOnTile() {
        Robber robber = new Robber(16);
        assertTrue(robber.isOnTile(16));
        robber.moveTo(0);
        assertFalse(robber.isOnTile(16));
        assertTrue(robber.isOnTile(0));
    }
}
