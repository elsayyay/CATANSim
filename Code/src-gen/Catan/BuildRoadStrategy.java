package Catan;

import java.util.*;

/**
 * Strategy that evaluates all possible road placements.
 * Roads build without earning VP, so value = 0.8.
 */
public class BuildRoadStrategy implements AIStrategy {

    @Override
    public List<AIAction> evaluate(Player player, Board board, Game game) {
        List<AIAction> actions = new ArrayList<>();
        if (!player.hasResources(Cost.road())) return actions;

        for (Node a : board.getNodes()) {
            int aId = a.getNodeId();
            for (int bId : BoardTopology.nodeNeighbors.get(aId)) {
                if (aId >= bId) continue;
                Node b = board.getNode(bId);
                if (board.canPlaceRoad(player, a, b)) {
                    GameCommand cmd = new BuildRoadCommand(player, board, a, b);
                    actions.add(new AIAction(
                        "BUILD ROAD " + aId + "-" + bId,
                        0.8,
                        cmd
                    ));
                }
            }
        }
        return actions;
    }
}
