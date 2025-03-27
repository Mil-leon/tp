package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.commands.ViewCommand;

public class ViewCommandParserTest {
    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index index = Index.fromOneBased(1);
        // no leading and trailing whitespaces (client)
        assertParseSuccess(parser, "client", new ViewCommand("client"));

        // no leading and trailing whitespaces (pastry)
        assertParseSuccess(parser, "pastry", new ViewCommand("pastry"));

        // whitespace only preamble (client)
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "client", new ViewCommand("client"));

        // whitespace only preamble (pastry)
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "pastry", new ViewCommand("pastry"));

        // no leading and trailing whitespaces (order)
        assertParseSuccess(parser, "order", new ViewCommand("order"));

        // whitespace only preamble (order)
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "order", new ViewCommand("order"));

        // no leading and trailing whitespaces (order with index)
        assertParseSuccess(parser, "order 1", new ViewCommand(index));

        // whitespace only preamble (order with index)
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "order 1", new ViewCommand(index));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing field
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgument_failure() {
        // invalid value
        assertParseFailure(parser, "invalid_nonsense",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        // invalid index
        assertParseFailure(parser, "order 0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexNegative_failure() {
        // invalid index
        assertParseFailure(parser, "order -1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexNonNumeric_failure() {
        // invalid index
        assertParseFailure(parser, "order a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgumentClient_failure() {
        // invalid value
        assertParseFailure(parser, "client 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgumentPastry_failure() {
        // invalid value
        assertParseFailure(parser, "pastry 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
    }
}
