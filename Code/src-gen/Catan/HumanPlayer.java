package Catan;

import java.util.*;

/**
 * Human-controlled player that reads commands from the command line.
 * Supports: roll, list, build settlement/city/road, undo, redo, go.
 */
public class HumanPlayer extends Player {
    private final Scanner scanner;
    private final CommandHistory commandHistory = new CommandHistory();

    public HumanPlayer(int id, Scanner scanner) {
        super(id);
        this.scanner = scanner;
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public String takeTurn(Game game) {
        Board board = game.getBoard();
        int round = game.getRound();
        boolean hasRolled = false;
        commandHistory.clear();

        System.out.println("=== Player " + getId() + "'s turn (Human) ===");
        System.out.println("Commands: roll, list, build settlement <nodeId>, build city <nodeId>, build road <from>,<to>, undo, redo, go");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            CommandParser.Command cmd = CommandParser.parse(input);

            switch (cmd.type) {
                case ROLL:
                    if (hasRolled) {
                        System.out.println("Already rolled this turn.");
                        break;
                    }
                    int roll = game.rollDice();
                    System.out.println("[" + round + "] / [" + getId() + "]: ROLL " + roll);
                    if (roll == 7) {
                        game.handleRobber(this);
                    } else {
                        board.produceResources(roll);
                    }
                    hasRolled = true;
                    break;

                case LIST:
                    listHand();
                    break;

                case BUILD_SETTLEMENT:
                    if (!hasRolled) { System.out.println("Must roll first."); break; }
                    handleBuildSettlement(board, round, cmd.args[0]);
                    break;

                case BUILD_CITY:
                    if (!hasRolled) { System.out.println("Must roll first."); break; }
                    handleBuildCity(board, round, cmd.args[0]);
                    break;

                case BUILD_ROAD:
                    if (!hasRolled) { System.out.println("Must roll first."); break; }
                    handleBuildRoad(board, round, cmd.args[0], cmd.args[1]);
                    break;

                case UNDO:
                    if (!commandHistory.canUndo()) {
                        System.out.println("Nothing to undo.");
                    } else {
                        commandHistory.undo();
                        System.out.println("Action undone.");
                    }
                    break;

                case REDO:
                    if (!commandHistory.canRedo()) {
                        System.out.println("Nothing to redo.");
                    } else {
                        commandHistory.redo();
                        System.out.println("Action redone.");
                    }
                    break;

                case GO:
                    if (!hasRolled) {
                        System.out.println("Must roll first.");
                        break;
                    }
                    return "END TURN";

                case INVALID:
                    System.out.println("Invalid command. Try: roll, list, build settlement <n>, build city <n>, build road <n>,<n>, undo, redo, go");
                    break;
            }
        }
    }

    private void listHand() {
        EnumMap<ResourceType, Integer> counts = countHand();
        System.out.println("--- Your hand (" + handSize() + " cards) ---");
        for (Map.Entry<ResourceType, Integer> entry : counts.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("--- Victory Points: " + getVictoryPoints() + " ---");
    }

    private void handleBuildSettlement(Board board, int round, int nodeId) {
        if (nodeId < 0 || nodeId >= BoardTopology.NODE_COUNT) {
            System.out.println("Invalid node ID. Must be 0-" + (BoardTopology.NODE_COUNT - 1) + ".");
            return;
        }
        if (!hasResources(Cost.settlement())) {
            System.out.println("Not enough resources. Need: 1 BRICK, 1 LUMBER, 1 WOOL, 1 GRAIN.");
            return;
        }
        Node at = board.getNode(nodeId);
        if (!board.canPlaceSettlement(this, at)) {
            System.out.println("Cannot place settlement at node " + nodeId + ".");
            return;
        }
        GameCommand cmd = new BuildSettlementCommand(this, board, at);
        String result = commandHistory.executeCommand(cmd);
        System.out.println("[" + round + "] / [" + getId() + "]: " + result);
    }

    private void handleBuildCity(Board board, int round, int nodeId) {
        if (nodeId < 0 || nodeId >= BoardTopology.NODE_COUNT) {
            System.out.println("Invalid node ID. Must be 0-" + (BoardTopology.NODE_COUNT - 1) + ".");
            return;
        }
        if (!hasResources(Cost.city())) {
            System.out.println("Not enough resources. Need: 2 GRAIN, 3 ORE.");
            return;
        }
        Node at = board.getNode(nodeId);
        if (!board.canUpgradeToCity(this, at)) {
            System.out.println("Cannot upgrade to city at node " + nodeId + ".");
            return;
        }
        GameCommand cmd = new BuildCityCommand(this, board, at);
        String result = commandHistory.executeCommand(cmd);
        System.out.println("[" + round + "] / [" + getId() + "]: " + result);
    }

    private void handleBuildRoad(Board board, int round, int fromId, int toId) {
        if (fromId < 0 || fromId >= BoardTopology.NODE_COUNT
                || toId < 0 || toId >= BoardTopology.NODE_COUNT) {
            System.out.println("Invalid node ID. Must be 0-" + (BoardTopology.NODE_COUNT - 1) + ".");
            return;
        }
        if (!hasResources(Cost.road())) {
            System.out.println("Not enough resources. Need: 1 BRICK, 1 LUMBER.");
            return;
        }
        Node a = board.getNode(fromId);
        Node b = board.getNode(toId);
        if (!board.canPlaceRoad(this, a, b)) {
            System.out.println("Cannot place road from " + fromId + " to " + toId + ".");
            return;
        }
        GameCommand cmd = new BuildRoadCommand(this, board, a, b);
        String result = commandHistory.executeCommand(cmd);
        System.out.println("[" + round + "] / [" + getId() + "]: " + result);
    }
}
