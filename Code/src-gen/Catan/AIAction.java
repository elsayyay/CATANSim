package Catan;

/**
 * Represents a possible AI action with its evaluated value and associated command.
 * Used by the Strategy Pattern to compare and select actions.
 */
public class AIAction {
    private final String description;
    private final double value;
    private final GameCommand command;

    public AIAction(String description, double value, GameCommand command) {
        this.description = description;
        this.value = value;
        this.command = command;
    }

    public String getDescription() { return description; }
    public double getValue() { return value; }
    public GameCommand getCommand() { return command; }
}
