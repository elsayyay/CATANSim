package Catan;

/**
 * Observer Pattern interface for game events (Task 3 - Design Only).
 *
 * This interface decouples game event producers (Game, Board, Player)
 * from event consumers (GameStateExporter, logging, UI).
 *
 * Concrete implementations would include:
 * - GameStateExportListener: replaces direct exporter.export() calls
 * - LoggingListener: replaces scattered System.out.println calls
 * - UIListener: could drive a graphical user interface
 *
 * Benefits:
 * - New listeners can be added without modifying Game/Board/Player
 * - GameStateExporter becomes decoupled from the game loop
 * - Logging can be toggled on/off by adding/removing the listener
 * - Clean separation of concerns between game logic and presentation
 */
public interface GameEventListener {
    void onBuildSettlement(Player player, Node location);
    void onBuildCity(Player player, Node location);
    void onBuildRoad(Player player, Node a, Node b);
    void onResourcesProduced(Player player, ResourceType type, int amount);
    void onRobberMoved(int oldTile, int newTile);
    void onTurnEnd(Player player, int round);
    void onGameEnd(Player winner);
}
