package Catan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CommandParser using regex-based parsing.
 * Covers all command types including edge cases and boundary values.
 */
class CommandParserTest {

    // --- Valid basic command tests ---

    @Test
    void parseBasicCommandswithVariations() {
        CommandParser.Command cmd = CommandParser.parse("roll");
        assertEquals(CommandParser.CommandType.ROLL, cmd.type);
        CommandParser.Command cmd2 = CommandParser.parse("ROLL");
        assertEquals(CommandParser.CommandType.ROLL, cmd2.type);
        CommandParser.Command cmd3 = CommandParser.parse("  roll  ");
        assertEquals(CommandParser.CommandType.ROLL, cmd3.type);
        CommandParser.Command cmd4 = CommandParser.parse("go");
        assertEquals(CommandParser.CommandType.GO, cmd4.type);
        CommandParser.Command cmd5 = CommandParser.parse("GO");
        assertEquals(CommandParser.CommandType.GO, cmd5.type);
        CommandParser.Command cmd6 = CommandParser.parse(" GO ");
        assertEquals(CommandParser.CommandType.GO, cmd6.type);
        CommandParser.Command cmd7 = CommandParser.parse("list");
        assertEquals(CommandParser.CommandType.LIST, cmd7.type);
        CommandParser.Command cmd8 = CommandParser.parse(" list ");
        assertEquals(CommandParser.CommandType.LIST, cmd8.type);
        CommandParser.Command cmd9 = CommandParser.parse("LIST");
        assertEquals(CommandParser.CommandType.LIST, cmd9.type);
    }


    // --- Build settlement tests ---

    @Test
    void parseBuildSettlementBoundaryZero() {
        CommandParser.Command cmd = CommandParser.parse("build settlement 0");
        assertEquals(CommandParser.CommandType.BUILD_SETTLEMENT, cmd.type);
        assertEquals(0, cmd.args[0]);
    }

    @Test
    void parseBuildSettlementBoundaryMax() {
        CommandParser.Command cmd = CommandParser.parse("build settlement 53");
        assertEquals(CommandParser.CommandType.BUILD_SETTLEMENT, cmd.type);
        assertEquals(53, cmd.args[0]);
    }


    // --- Invalid command tests ---

    @Test
    void parseEmptyString() {
        CommandParser.Command cmd = CommandParser.parse("");
        assertEquals(CommandParser.CommandType.INVALID, cmd.type);
    }

    @Test
    void parseNullInput() {
        CommandParser.Command cmd = CommandParser.parse(null);
        assertEquals(CommandParser.CommandType.INVALID, cmd.type);
    }

    @Test
    void parseGibberish() {
        CommandParser.Command cmd = CommandParser.parse("foobar");
        assertEquals(CommandParser.CommandType.INVALID, cmd.type);
    }

    @Test
    void parseBuildWithoutType() {
        CommandParser.Command cmd = CommandParser.parse("build");
        assertEquals(CommandParser.CommandType.INVALID, cmd.type);
    }

    @Test
    void parseBuildSettlementNoNode() {
        CommandParser.Command cmd = CommandParser.parse("build settlement");
        assertEquals(CommandParser.CommandType.INVALID, cmd.type);
    }

    @Test
    void parseBuildRoadOneNode() {
        CommandParser.Command cmd = CommandParser.parse("build road 5");
        assertEquals(CommandParser.CommandType.INVALID, cmd.type);
    }
}
