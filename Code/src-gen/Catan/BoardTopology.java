package Catan;

import java.util.*;

public final class BoardTopology {
    public static final int TILE_COUNT = 19;
    public static final int NODE_COUNT = 54;

    public static final int[][] tileNodes = new int[TILE_COUNT][6];
    public static final int[][] EDGES;
    public static final List<Set<Integer>> nodeNeighbors = new ArrayList<>(NODE_COUNT);

    static {
        for (int i = 0; i < NODE_COUNT; i++) nodeNeighbors.add(new HashSet<>());

        tileNodes[0]  = new int[]{ 4,  5,  0,  1,  2,  3};
        tileNodes[1]  = new int[]{ 2,  1,  6,  7,  8,  9};
        tileNodes[2]  = new int[]{ 3,  2,  9, 10, 11, 12};
        tileNodes[3]  = new int[]{15,  4,  3, 12, 13, 14};
        tileNodes[4]  = new int[]{17, 18, 16,  5,  4, 15};
        tileNodes[5]  = new int[]{16, 21, 19, 20,  0,  5};
        tileNodes[6]  = new int[]{ 0, 20, 22, 23,  6,  1};
        tileNodes[7]  = new int[]{ 8,  7, 24, 25, 26, 27};
        tileNodes[8]  = new int[]{10,  9,  8, 27, 28, 29};
        tileNodes[9]  = new int[]{11, 10, 29, 30, 31, 32};
        tileNodes[10] = new int[]{34, 13, 12, 11, 32, 33};
        tileNodes[11] = new int[]{36, 37, 14, 13, 34, 35};
        tileNodes[12] = new int[]{38, 39, 17, 15, 14, 37};
        tileNodes[13] = new int[]{41, 42, 40, 18, 17, 39};
        tileNodes[14] = new int[]{40, 44, 43, 21, 16, 18};
        tileNodes[15] = new int[]{43, 45, 47, 46, 19, 21};
        tileNodes[16] = new int[]{19, 46, 48, 49, 22, 20};
        tileNodes[17] = new int[]{22, 49, 50, 51, 52, 23};
        tileNodes[18] = new int[]{ 6, 23, 52, 53, 24,  7};

        EDGES = buildEdgesFromTiles();
        rebuildNeighborsFromEdges();
    }

    private static int[][] buildEdgesFromTiles() {
        Set<Long> edgeSet = new HashSet<>();
        for (int[] ns : tileNodes) {
            for (int i = 0; i < 6; i++) {
                int a = ns[i], b = ns[(i + 1) % 6];
                int lo = Math.min(a, b), hi = Math.max(a, b);
                edgeSet.add((((long) lo) << 32) | (hi & 0xffffffffL));
            }
        }
        int[][] edges = new int[edgeSet.size()][2];
        int idx = 0;
        for (long key : edgeSet) {
            edges[idx][0] = (int) (key >> 32);
            edges[idx][1] = (int) key;
            idx++;
        }
        return edges;
    }

    public static void rebuildNeighborsFromEdges() {
        for (Set<Integer> s : nodeNeighbors) s.clear();
        for (int[] e : EDGES) {
            nodeNeighbors.get(e[0]).add(e[1]);
            nodeNeighbors.get(e[1]).add(e[0]);
        }
    }

    private BoardTopology() {}
}
