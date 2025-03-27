package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import powerbake.address.logic.commands.ViewCommand;

public class ViewCommandParserTest {
    private ViewCommandParser parser = new ViewCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // no leading and trailing whitespaces (client)
        assertParseSuccess(parser, "client", new ViewCommand("client"));

        // no leading and trailing whitespaces (pastry)
        assertParseSuccess(parser, "pastry", new ViewCommand("pastry"));

        // whitespace only preamble (client)
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "client", new ViewCommand("client"));

        // whitespace only preamble (pastry)
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "pastry", new ViewCommand("pastry"));
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
}
