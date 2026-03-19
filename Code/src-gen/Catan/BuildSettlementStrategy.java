package Catan;

import java.util.*;

/**
 * Strategy that evaluates all possible settlement placements.
 * Settlements earn a VP, so value = 1.0.
 */
public class BuildSettlementStrategy implements AIStrategy {

    @Override
    public List<AIAction> evaluate(Player player, Board board, Game game) {
        List<AIAction> actions = new ArrayList<>();
        if (!player.hasResources(Cost.settlement())) return actions;

        for (Node n : board.getNodes()) {
            if (board.canPlaceSettlement(player, n)) {
                GameCommand cmd = new BuildSettlementCommand(player, board, n);
                actions.add(new AIAction(
                    "BUILD SETTLEMENT at node " + n.getNodeId(),
                    1.0,
                    cmd
                ));
            }
        }
        return actions;
    }
}
