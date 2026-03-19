package Catan;

/**
 * Command to build a road between two nodes.
 * Handles payment, placement, and full undo.
 */
public class BuildRoadCommand implements GameCommand {
    private final Player player;
    private final Board board;
    private final Node nodeA;
    private final Node nodeB;
    private Road placedRoad;

    public BuildRoadCommand(Player player, Board board, Node nodeA, Node nodeB) {
        this.player = player;
        this.board = board;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    @Override
    public String execute() {
        player.pay(Cost.road());
        board.placeRoad(player, nodeA, nodeB);
        placedRoad = board.getLastPlacedRoad();
        return "BUILD ROAD " + nodeA.getNodeId() + "-" + nodeB.getNodeId();
    }

    @Override
    public void undo() {
        if (placedRoad != null) {
            board.removeRoad(placedRoad);
        }
        player.addResources(ResourceType.BRICK, 1);
        player.addResources(ResourceType.LUMBER, 1);
    }
}
