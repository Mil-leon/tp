package powerbake.address.logic.parser;

import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_PASTRY_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static powerbake.address.logic.commands.CommandTestUtil.NAME_DESC_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.NAME_DESC_CINNAMONROLL;
import static powerbake.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static powerbake.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static powerbake.address.logic.commands.CommandTestUtil.PRICE_DESC_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.PRICE_DESC_CINNAMONROLL;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PRICE_CHOCOLATECROISSANT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static powerbake.address.testutil.TypicalPastries.CHOCOLATECROISSANT;

import org.junit.jupiter.api.Test;

import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.AddCommand;
import powerbake.address.logic.commands.AddPastryCommand;
import powerbake.address.model.pastry.Name;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.pastry.Price;
import powerbake.address.testutil.PastryBuilder;

public class AddPastryCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Pastry expectedPastry = new PastryBuilder(CHOCOLATECROISSANT).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_CHOCOLATECROISSANT + PRICE_DESC_CHOCOLATECROISSANT,
                new AddPastryCommand(expectedPastry));
    }

    @Test
    public void parse_pastryRepeatedValue_failure() {
        String validExpectedPastryString = NAME_DESC_CHOCOLATECROISSANT + PRICE_DESC_CHOCOLATECROISSANT;

        // multiple pastry
        assertParseFailure(parser, validExpectedPastryString + NAME_DESC_CINNAMONROLL,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PASTRY));

        // multiple price
        assertParseFailure(parser, validExpectedPastryString + PRICE_DESC_CINNAMONROLL,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PRICE));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPastryString + NAME_DESC_CINNAMONROLL + PRICE_DESC_CINNAMONROLL
                        + validExpectedPastryString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PASTRY, PREFIX_PRICE));

        // valid value followed by invalid value

        // invalid pastry name
        assertParseFailure(parser, validExpectedPastryString + INVALID_PASTRY_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PASTRY));

        // invalid price
        assertParseFailure(parser, validExpectedPastryString + INVALID_PRICE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PRICE));
    }

    @Test
    public void parse_pastryCompulsoryFieldMissing_failure() {
        String addPastryExpectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPastryCommand.MESSAGE_USAGE);
        String addExpectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_CHOCOLATECROISSANT + PRICE_DESC_CHOCOLATECROISSANT,
                addExpectedMessage);

        // missing price prefix
        assertParseFailure(parser, NAME_DESC_CHOCOLATECROISSANT + VALID_PRICE_CHOCOLATECROISSANT,
                addPastryExpectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_CHOCOLATECROISSANT + VALID_PRICE_CHOCOLATECROISSANT,
                addExpectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_PASTRY_DESC + PRICE_DESC_CHOCOLATECROISSANT, Name.MESSAGE_CONSTRAINTS);

        // invalid price
        assertParseFailure(parser, NAME_DESC_CHOCOLATECROISSANT + INVALID_PRICE_DESC, Price.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_PASTRY_DESC + INVALID_PRICE_DESC, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_CHOCOLATECROISSANT + PRICE_DESC_CHOCOLATECROISSANT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
