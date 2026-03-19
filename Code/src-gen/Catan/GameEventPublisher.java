package Catan;

import java.util.*;

/**
 * Observer Pattern subject for game events (Task 3 - Design Only).
 *
 * Manages a list of GameEventListener observers and provides
 * fire methods for each event type. Would live on the Game object
 * and be called from Board operations and Game state transitions.
 *
 * Usage (if implemented):
 *   GameEventPublisher publisher = new GameEventPublisher();
 *   publisher.addListener(new GameStateExportListener(exporter));
 *   publisher.addListener(new LoggingListener());
 *   // ... in Board.placeSettlement():
 *   publisher.fireBuildSettlement(player, node);
 */
public class GameEventPublisher {
    private final List<GameEventListener> listeners = new ArrayList<>();

    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void fireBuildSettlement(Player player, Node location) {
        for (GameEventListener l : listeners) l.onBuildSettlement(player, location);
    }

    public void fireBuildCity(Player player, Node location) {
        for (GameEventListener l : listeners) l.onBuildCity(player, location);
    }

    public void fireBuildRoad(Player player, Node a, Node b) {
        for (GameEventListener l : listeners) l.onBuildRoad(player, a, b);
    }

    public void fireResourcesProduced(Player player, ResourceType type, int amount) {
        for (GameEventListener l : listeners) l.onResourcesProduced(player, type, amount);
    }

    public void fireRobberMoved(int oldTile, int newTile) {
        for (GameEventListener l : listeners) l.onRobberMoved(oldTile, newTile);
    }

    public void fireTurnEnd(Player player, int round) {
        for (GameEventListener l : listeners) l.onTurnEnd(player, round);
    }

    public void fireGameEnd(Player winner) {
        for (GameEventListener l : listeners) l.onGameEnd(winner);
    }
}
