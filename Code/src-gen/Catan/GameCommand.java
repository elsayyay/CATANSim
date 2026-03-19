package Catan;

/**
 * Command Pattern interface for game actions.
 * Each command encapsulates an action that can be executed and undone.
 */
public interface GameCommand {
    String execute();
    void undo();
}
