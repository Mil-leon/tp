package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import powerbake.address.logic.commands.FindCommand;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingCommandWord_throwsParseException() {
        assertParseFailure(parser, "Alice", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingKeywords_throwsParseException() {
        assertParseFailure(parser, "client", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "pastry", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "order", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validclientArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        String commandWord = "client";
        String[] nameKeywords = {"Alice", "Bob"};
        FindCommand expectedFindCommand =
                new FindCommand(commandWord, nameKeywords);
        assertParseSuccess(parser, "client Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "client \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validpastryArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        String commandWord = "pastry";
        String[] nameKeywords = {"Alice", "Bob"};
        FindCommand expectedFindCommand =
                new FindCommand(commandWord, nameKeywords);
        assertParseSuccess(parser, "pastry Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "pastry \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validorderArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        String commandWord = "order";
        String[] nameKeywords = {"Alice", "Bob"};
        FindCommand expectedFindCommand =
                new FindCommand(commandWord, nameKeywords);
        assertParseSuccess(parser, "order Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "order \n Alice \n \t Bob  \t", expectedFindCommand);
    }
}
