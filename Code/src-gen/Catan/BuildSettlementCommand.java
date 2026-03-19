package Catan;

/**
 * Command to build a settlement at a given node.
 * Handles payment, placement, VP, and full undo.
 */
public class BuildSettlementCommand implements GameCommand {
    private final Player player;
    private final Board board;
    private final Node node;

    public BuildSettlementCommand(Player player, Board board, Node node) {
        this.player = player;
        this.board = board;
        this.node = node;
    }

    @Override
    public String execute() {
        player.pay(Cost.settlement());
        board.placeSettlement(player, node);
        return "BUILD SETTLEMENT at node " + node.getNodeId();
    }

    @Override
    public void undo() {
        node.clear();
        player.addVictoryPoints(-1);
        player.addResources(ResourceType.BRICK, 1);
        player.addResources(ResourceType.LUMBER, 1);
        player.addResources(ResourceType.WOOL, 1);
        player.addResources(ResourceType.GRAIN, 1);
    }
}
