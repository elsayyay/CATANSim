package Catan;

import java.util.*;

/**
 * Utility to compute the longest road for a player using DFS.
 * Longest road = longest simple path through the player's road network.
 */
public class LongestRoadCalculator {

    public static int calculate(Player player, Board board) {
        // Build adjacency list from player's roads
        Map<Integer, Set<Integer>> adj = new HashMap<>();
        for (Road r : board.getRoads()) {
            if (r.getOwner().getId() != player.getId()) continue;
            int a = r.getA().getNodeId();
            int b = r.getB().getNodeId();
            adj.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            adj.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }

        if (adj.isEmpty()) return 0;

        int longest = 0;
        Set<String> visited = new HashSet<>();
        for (int start : adj.keySet()) {
            longest = Math.max(longest, dfs(start, adj, visited));
        }
        return longest;
    }

    private static int dfs(int node, Map<Integer, Set<Integer>> adj, Set<String> visited) {
        int max = 0;
        Set<Integer> neighbors = adj.getOrDefault(node, Collections.emptySet());
        for (int next : neighbors) {
            String edge = Math.min(node, next) + "-" + Math.max(node, next);
            if (visited.contains(edge)) continue;
            visited.add(edge);
            max = Math.max(max, 1 + dfs(next, adj, visited));
            visited.remove(edge);
        }
        return max;
    }

    /**
     * Find connected components of a player's road network.
     * Returns list of sets, each set containing node IDs in one component.
     */
    public static List<Set<Integer>> findRoadComponents(Player player, Board board) {
        Map<Integer, Set<Integer>> adj = new HashMap<>();
        for (Road r : board.getRoads()) {
            if (r.getOwner().getId() != player.getId()) continue;
            int a = r.getA().getNodeId();
            int b = r.getB().getNodeId();
            adj.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            adj.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }

        List<Set<Integer>> components = new ArrayList<>();
        Set<Integer> globalVisited = new HashSet<>();

        for (int node : adj.keySet()) {
            if (globalVisited.contains(node)) continue;
            Set<Integer> component = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(node);
            while (!queue.isEmpty()) {
                int curr = queue.poll();
                if (!component.add(curr)) continue;
                globalVisited.add(curr);
                for (int neighbor : adj.getOrDefault(curr, Collections.emptySet())) {
                    if (!component.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
            components.add(component);
        }
        return components;
    }

    /**
     * BFS shortest distance between two nodes on the board graph.
     * Returns the number of edges in the shortest path, or Integer.MAX_VALUE if unreachable.
     */
    public static int boardDistance(int fromNode, int toNode) {
        if (fromNode == toNode) return 0;
        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(new int[]{fromNode, 0});
        visited.add(fromNode);

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int node = curr[0];
            int dist = curr[1];
            for (int neighbor : BoardTopology.nodeNeighbors.get(node)) {
                if (neighbor == toNode) return dist + 1;
                if (visited.add(neighbor)) {
                    queue.add(new int[]{neighbor, dist + 1});
                }
            }
        }
        return Integer.MAX_VALUE;
    }
}
