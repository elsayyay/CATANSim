package Catan;

import java.util.*;

/**
 * Checks R3.3 constraints for the AI player.
 * Constraints are checked in priority order and must be resolved
 * before value-based action selection.
 */
public class ConstraintChecker {

    /**
     * Check all R3.3 constraints and return a mandatory action if one applies.
     * Returns null if no constraint is active.
     */
    public AIAction checkConstraints(Player player, Board board, Game game) {
        // Constraint 1: >7 cards — must spend them
        if (player.handSize() > 7) {
            AIAction spendAction = findBestSpendAction(player, board, game);
            if (spendAction != null) return spendAction;
        }

        // Constraint 2: two road segments <= 2 units apart — try to connect
        AIAction connectAction = tryConnectNearbyRoads(player, board);
        if (connectAction != null) return connectAction;

        // Constraint 3: opponent longest road at most 1 shorter — buy connected road
        AIAction defensiveRoad = tryDefensiveLongestRoad(player, board, game);
        if (defensiveRoad != null) return defensiveRoad;

        return null;
    }

    private AIAction findBestSpendAction(Player player, Board board, Game game) {
        List<AIAction> actions = new ArrayList<>();

        // Collect all possible build actions
        List<AIStrategy> strategies = List.of(
            new BuildSettlementStrategy(),
            new BuildCityStrategy(),
            new BuildRoadStrategy()
        );
        for (AIStrategy s : strategies) {
            actions.addAll(s.evaluate(player, board, game));
        }

        if (actions.isEmpty()) return null;

        // Pick the highest-value action to spend resources
        actions.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return actions.get(0);
    }

    private AIAction tryConnectNearbyRoads(Player player, Board board) {
        if (!player.hasResources(Cost.road())) return null;

        List<Set<Integer>> components = LongestRoadCalculator.findRoadComponents(player, board);
        if (components.size() < 2) return null;

        // Check all pairs of components for endpoints <= 2 apart
        for (int i = 0; i < components.size(); i++) {
            for (int j = i + 1; j < components.size(); j++) {
                AIAction action = tryConnectComponents(player, board, components.get(i), components.get(j));
                if (action != null) return action;
            }
        }
        return null;
    }

    private AIAction tryConnectComponents(Player player, Board board, Set<Integer> compA, Set<Integer> compB) {
        // Find the closest pair of endpoints between components
        int bestDist = Integer.MAX_VALUE;
        int bestFrom = -1;
        int bestTo = -1;

        for (int a : compA) {
            for (int b : compB) {
                int dist = LongestRoadCalculator.boardDistance(a, b);
                if (dist <= 2 && dist < bestDist) {
                    bestDist = dist;
                    bestFrom = a;
                    bestTo = b;
                }
            }
        }

        if (bestDist > 2) return null;

        // Try to build a road on the path between bestFrom and bestTo
        // Find first valid road placement towards the target
        return findRoadTowards(player, board, bestFrom, bestTo);
    }

    private AIAction findRoadTowards(Player player, Board board, int from, int to) {
        // BFS to find shortest path, then build first road on that path
        Queue<List<Integer>> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(List.of(from));
        visited.add(from);

        while (!queue.isEmpty()) {
            List<Integer> path = queue.poll();
            int current = path.get(path.size() - 1);
            if (current == to && path.size() >= 2) {
                // Try to build road on the first edge of this path
                int a = path.get(0);
                int b = path.get(1);
                Node nodeA = board.getNode(a);
                Node nodeB = board.getNode(b);
                if (board.canPlaceRoad(player, nodeA, nodeB)) {
                    GameCommand cmd = new BuildRoadCommand(player, board, nodeA, nodeB);
                    return new AIAction("BUILD ROAD " + a + "-" + b + " (connecting segments)", 0.8, cmd);
                }
            }
            for (int neighbor : BoardTopology.nodeNeighbors.get(current)) {
                if (visited.add(neighbor)) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }
        return null;
    }

    private AIAction tryDefensiveLongestRoad(Player player, Board board, Game game) {
        if (!player.hasResources(Cost.road())) return null;

        int myLongest = LongestRoadCalculator.calculate(player, board);
        boolean threatened = false;

        for (Player other : game.getPlayers()) {
            if (other.getId() == player.getId()) continue;
            int otherLongest = LongestRoadCalculator.calculate(other, board);
            if (otherLongest >= myLongest - 1) {
                threatened = true;
                break;
            }
        }

        if (!threatened) return null;

        // Build a connected road segment to extend longest road
        for (Node a : board.getNodes()) {
            int aId = a.getNodeId();
            for (int bId : BoardTopology.nodeNeighbors.get(aId)) {
                if (aId >= bId) continue;
                Node b = board.getNode(bId);
                if (board.canPlaceRoad(player, a, b)) {
                    GameCommand cmd = new BuildRoadCommand(player, board, a, b);
                    return new AIAction("BUILD ROAD " + aId + "-" + bId + " (defending longest road)", 0.8, cmd);
                }
            }
        }
        return null;
    }
}
