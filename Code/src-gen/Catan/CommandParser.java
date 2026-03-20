package Catan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses human player commands using regular expressions.
 * Supported commands: roll, go, list, build settlement/city/road.
 */
public class CommandParser {

    public enum CommandType {
        ROLL, GO, LIST, BUILD_SETTLEMENT, BUILD_CITY, BUILD_ROAD, UNDO, REDO, INVALID
    }

    public static class Command {
        public final CommandType type;
        public final int[] args;

        public Command(CommandType type, int... args) {
            this.type = type;
            this.args = args;
        }
    }

    private static final Pattern ROLL_PATTERN =
            Pattern.compile("^\\s*roll\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern GO_PATTERN =
            Pattern.compile("^\\s*go\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern LIST_PATTERN =
            Pattern.compile("^\\s*list\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern BUILD_SETTLEMENT_PATTERN =
            Pattern.compile("^\\s*build\\s+settlement\\s+(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern BUILD_CITY_PATTERN =
            Pattern.compile("^\\s*build\\s+city\\s+(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern BUILD_ROAD_PATTERN =
            Pattern.compile("^\\s*build\\s+road\\s+(\\d+)\\s*,\\s*(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UNDO_PATTERN =
            Pattern.compile("^\\s*undo\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern REDO_PATTERN =
            Pattern.compile("^\\s*redo\\s*$", Pattern.CASE_INSENSITIVE);

    public static Command parse(String input) {
        if (input == null) return new Command(CommandType.INVALID);

        Matcher m;

        m = ROLL_PATTERN.matcher(input);
        if (m.matches()) return new Command(CommandType.ROLL);

        m = GO_PATTERN.matcher(input);
        if (m.matches()) return new Command(CommandType.GO);

        m = LIST_PATTERN.matcher(input);
        if (m.matches()) return new Command(CommandType.LIST);

        m = BUILD_SETTLEMENT_PATTERN.matcher(input);
        if (m.matches()) {
            int nodeId = Integer.parseInt(m.group(1));
            return new Command(CommandType.BUILD_SETTLEMENT, nodeId);
        }

        m = BUILD_CITY_PATTERN.matcher(input);
        if (m.matches()) {
            int nodeId = Integer.parseInt(m.group(1));
            return new Command(CommandType.BUILD_CITY, nodeId);
        }

        m = BUILD_ROAD_PATTERN.matcher(input);
        if (m.matches()) {
            int from = Integer.parseInt(m.group(1));
            int to = Integer.parseInt(m.group(2));
            return new Command(CommandType.BUILD_ROAD, from, to);
        }

        m = UNDO_PATTERN.matcher(input);
        if (m.matches()) return new Command(CommandType.UNDO);

        m = REDO_PATTERN.matcher(input);
        if (m.matches()) return new Command(CommandType.REDO);

        return new Command(CommandType.INVALID);
    }
}
