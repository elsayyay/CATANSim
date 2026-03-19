package Catan;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages undo/redo stacks for the Command Pattern.
 * Tracks executed commands and supports reversing or re-applying them.
 */
public class CommandHistory {
    private final Deque<GameCommand> undoStack = new ArrayDeque<>();
    private final Deque<GameCommand> redoStack = new ArrayDeque<>();

    public String executeCommand(GameCommand cmd) {
        String result = cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
        return result;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        if (!canUndo()) throw new IllegalStateException("Nothing to undo.");
        GameCommand cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
    }

    public void redo() {
        if (!canRedo()) throw new IllegalStateException("Nothing to redo.");
        GameCommand cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
