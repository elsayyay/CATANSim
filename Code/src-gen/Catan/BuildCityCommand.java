package Catan;

/**
 * Command to upgrade a settlement to a city.
 * Saves the previous settlement for undo restoration.
 */
public class BuildCityCommand implements GameCommand {
    private final Player player;
    private final Board board;
    private final Node node;
    private Settlement previousSettlement;

    public BuildCityCommand(Player player, Board board, Node node) {
        this.player = player;
        this.board = board;
        this.node = node;
    }

    @Override
    public String execute() {
        Building occupant = node.getOccupant();
        if (occupant instanceof Settlement) {
            previousSettlement = (Settlement) occupant;
        }
        player.pay(Cost.city());
        board.upgradeToCity(player, node);
        return "BUILD CITY at node " + node.getNodeId();
    }

    @Override
    public void undo() {
        node.clear();
        if (previousSettlement != null) {
            node.place(previousSettlement);
        }
        // upgradeToCity adds City.victoryPoints() which is 1
        player.addVictoryPoints(-1);
        player.addResources(ResourceType.GRAIN, 2);
        player.addResources(ResourceType.ORE, 3);
    }
}
