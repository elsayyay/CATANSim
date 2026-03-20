package Catan;

import java.util.*;
import java.util.stream.*;

/**
 * Computer-controlled player that uses the Strategy Pattern for decision-making.
 *
 * R3.2: Evaluates all possible actions using strategies, picks the highest value:
 *   - Earning a VP (settlement/city): 1.0
 *   - Building without VP (road): 0.8
 *   - Spending cards to below 5: 0.5
 *   - Ties: random selection
 *
 * R3.3: Checks constraints before value evaluation:
 *   - >7 cards: must spend
 *   - Two road segments <= 2 apart: connect them
 *   - Opponent longest road at most 1 shorter: buy connected road
 */
public class AIPlayer extends Player {
    private final Random rng = new Random();
    private final List<AIStrategy> strategies;
    private final ConstraintChecker constraintChecker;
    private final CommandHistory commandHistory = new CommandHistory();

    public AIPlayer(int id) {
        super(id);
        this.strategies = List.of(
            new BuildSettlementStrategy(),
            new BuildCityStrategy(),
            new BuildRoadStrategy()
        );
        this.constraintChecker = new ConstraintChecker();
    }

    @Override
    public String takeTurn(Game game) {
        Board board = game.getBoard();

        // R3.3: Check constraints first — must resolve before value-based actions
        AIAction mandatory = constraintChecker.checkConstraints(this, board, game);
        if (mandatory != null) {
            String result = commandHistory.executeCommand(mandatory.getCommand());
            return mandatory.getDescription();
        }

        // R3.2: Evaluate all possible actions using strategies
        List<AIAction> allActions = new ArrayList<>();
        for (AIStrategy strategy : strategies) {
            allActions.addAll(strategy.evaluate(this, board, game));
        }

        if (allActions.isEmpty()) {
            return "PASS";
        }

        // Find maximum value
        double maxValue = allActions.stream()
                .mapToDouble(AIAction::getValue)
                .max()
                .orElse(0);

        // Filter to actions with max value, pick random for tie-breaking
        List<AIAction> best = allActions.stream()
                .filter(a -> a.getValue() == maxValue)
                .collect(Collectors.toList());

        AIAction chosen = best.get(rng.nextInt(best.size()));
        commandHistory.executeCommand(chosen.getCommand());
        return chosen.getDescription();
    }
}
