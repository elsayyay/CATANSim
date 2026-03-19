package Catan;

import java.util.*;

/**
 * Strategy that evaluates all possible city upgrades.
 * Cities earn a VP (upgrade from settlement), so value = 1.0.
 */
public class BuildCityStrategy implements AIStrategy {

    @Override
    public List<AIAction> evaluate(Player player, Board board, Game game) {
        List<AIAction> actions = new ArrayList<>();
        if (!player.hasResources(Cost.city())) return actions;

        for (Node n : board.getNodes()) {
            if (board.canUpgradeToCity(player, n)) {
                GameCommand cmd = new BuildCityCommand(player, board, n);
                actions.add(new AIAction(
                    "BUILD CITY at node " + n.getNodeId(),
                    1.0,
                    cmd
                ));
            }
        }
        return actions;
    }
}
