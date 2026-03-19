package Catan;

import java.util.List;

/**
 * Strategy Pattern interface for AI decision-making.
 * Each strategy evaluates possible actions of a specific type
 * and returns them with their computed values.
 */
public interface AIStrategy {
    List<AIAction> evaluate(Player player, Board board, Game game);
}
