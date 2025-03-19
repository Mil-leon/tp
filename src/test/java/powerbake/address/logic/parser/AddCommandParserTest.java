package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import powerbake.address.logic.commands.AddCommand;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_clientAndPastryFieldPresent_failure() {
        String addExpectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_CLIENT.toString() + PREFIX_PASTRY.toString(),
                addExpectedMessage);

        assertParseFailure(parser, PREFIX_CLIENT.toString(), addExpectedMessage);

        assertParseFailure(parser, PREFIX_PASTRY.toString(), addExpectedMessage);
    }
}
