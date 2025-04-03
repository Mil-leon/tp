package powerbake.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static powerbake.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static powerbake.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_ORDER;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static powerbake.address.testutil.TypicalIndexes.INDEX_SECOND_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static powerbake.address.testutil.TypicalIndexes.INDEX_THIRD_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.CommandResult;
import powerbake.address.logic.commands.DeleteCommand;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.AddressBook;
import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;
import powerbake.address.model.order.Order;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;
import powerbake.address.testutil.TypicalAddressBook;


/**
 * Unit tests for {@link DeleteCommandParser}.
 *
 * This test class ensures that DeleteCommandParser correctly parses valid and invalid
 * arguments for deleting clients and pastries and performs corresponding operations.
 *
 * As we are only doing white-box testing, our test cases do not cover path variations outside
 * of the DeleteCommandParser code. For example, inputs "1" and "1 abc" take the same path
 * through the DeleteCommand logic and are therefore not covered here.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    /**
     * Tests that valid arguments for deleting clients at various indices are successfully parsed.
     */
    @Test
    public void parse_validClientIndices() {
        assertParseSuccess(parser, PREFIX_CLIENT + "1",
                new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON));
        assertParseSuccess(parser, PREFIX_CLIENT + "2",
                new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_SECOND_PERSON));
        assertParseSuccess(parser, PREFIX_CLIENT + "3",
                new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_THIRD_PERSON));
    }

    /**
     * Tests that valid arguments for deleting pastries at various indices are successfully parsed.
     */
    @Test
    public void parse_validPastryIndices() {
        assertParseSuccess(parser, PREFIX_PASTRY + "1",
                new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY));
        assertParseSuccess(parser, PREFIX_PASTRY + "2",
                new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_SECOND_PASTRY));
        assertParseSuccess(parser, PREFIX_PASTRY + "3",
                new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_THIRD_PASTRY));
    }

    /**
     * Tests that valid arguments for deleting orders at various indices are successfully parsed.
     */
    @Test
    public void parse_validOrderIndices() {
        assertParseSuccess(parser, PREFIX_ORDER + "1",
                new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_PASTRY));
        assertParseSuccess(parser, PREFIX_ORDER + "2",
                new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_SECOND_PASTRY));
        assertParseSuccess(parser, PREFIX_ORDER + "3",
                new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_THIRD_PASTRY));
    }

    /**
     * Tests that invalid arguments for deleting clients (non-integer or missing index).
     */
    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, PREFIX_CLIENT + "a",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_CLIENT.toString().trim(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

        assertParseFailure(parser, PREFIX_CLIENT + "1 extraStuffHere",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Tests that invalid arguments for deleting pastries (non-integer or missing index).
     */
    @Test
    public void parse_invalidPastryArgs_throwsParseException() {
        assertParseFailure(parser, PREFIX_PASTRY + "a",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_PASTRY.toString().trim(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

        assertParseFailure(parser, PREFIX_PASTRY + "1 extraStuffHere",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Tests that invalid arguments for deleting orders (non-integer or missing index).
     */
    @Test
    public void parse_invalidOrderArgs_throwsParseException() {
        assertParseFailure(parser, PREFIX_ORDER + "a",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_ORDER.toString().trim(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));

        assertParseFailure(parser, PREFIX_ORDER + "1 extraStuffHere",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Tests that the parser handles case-insensitive entity types correctly (e.g., Client, CLIENT, cLiEnT).
     */
    @Test
    public void parse_caseInsensitiveEntityType() {
        assertParseSuccess(parser, "CLIENT 1",
                new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "PASTRY 1",
                new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY));
        assertParseSuccess(parser, "ORDER 1",
                new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_ORDER));


        assertParseSuccess(parser, "CLIent 1",
                new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "PAStry 2",
                new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_SECOND_PASTRY));
        assertParseSuccess(parser, "oRdEr 1",
                new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_ORDER));
    }

    /**
     * Tests that invalid arguments with negative indices.
     */
    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertParseFailure(parser, PREFIX_CLIENT + "-1",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREFIX_PASTRY + "-1",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREFIX_ORDER + "-1",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                    + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Tests that invalid arguments with zero as the index.
     */
    @Test
    public void parse_zeroIndex_throwsParseException() {
        assertParseFailure(parser, PREFIX_CLIENT + "0",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                + "\n" + DeleteCommand.MESSAGE_USAGE);

        assertParseFailure(parser, PREFIX_PASTRY + "0",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                + "\n" + DeleteCommand.MESSAGE_USAGE);


        assertParseFailure(parser, PREFIX_ORDER + "0",
                String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ParserUtil.MESSAGE_INVALID_INDEX
                    )
                + "\n" + DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Tests that successfully deleting a client removes it from the address book.
     *
     * @throws Exception if the delete operation encounters an error.
     */
    @Test
    public void execute_deletePerson_success() throws Exception {
        AddressBook addressBook = TypicalAddressBook.getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand("client", INDEX_FIRST_PERSON);
        Person personToDelete = model.getFilteredPersonList().get(0);

        CommandResult result = deleteCommand.execute(model);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete.getName());

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertFalse(model.getFilteredPersonList().contains(personToDelete));
    }

    /**
     * Tests that successfully deleting a pastry removes it from the address book.
     *
     * @throws Exception if the delete operation encounters an error.
     */
    @Test
    public void execute_deletePastry_success() throws Exception {
        AddressBook addressBook = TypicalAddressBook.getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand("pastry", INDEX_FIRST_PASTRY);
        Pastry pastryToDelete = model.getFilteredPastryList().get(0);

        CommandResult result = deleteCommand.execute(model);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName());

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertFalse(model.getFilteredPastryList().contains(pastryToDelete));
    }

    /**
     * Tests that successfully deleting an order removes it from the address book.
     *
     * @throws Exception if the delete operation encounters an error.
     */
    @Test
    public void execute_deleteOrder_success() throws Exception {
        AddressBook addressBook = TypicalAddressBook.getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_ORDER);
        Order orderToDelete = model.getFilteredOrderList().get(0);

        List<String> orderItemsSummary = orderToDelete.getOrderItems()
                .stream()
                .map(item -> item.getQuantity() + "x " + item.getPastry().getName())
                .collect(Collectors.toList());

        String orderItemsString = String.join(",  ", orderItemsSummary);
        String price = String.format("$%.2f", orderToDelete.getTotalPrice());

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("EEE, d MMM yyyy hh:mm a")
                .withLocale(Locale.US);
        String orderDate = orderToDelete.getOrderDate().format(dateTimeFormatter);

        CommandResult result = deleteCommand.execute(model);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ORDER_SUCCESS,
                orderToDelete.getOrderId(),
                orderDate,
                orderToDelete.getCustomer().getName(),
                orderItemsString,
                price);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertFalse(model.getFilteredOrderList().contains(orderToDelete));
    }

    /**
     * Tests that attempting to delete from an empty address book throws a {@link CommandException}.
     */
    @Test
    public void execute_deleteFromEmptyAddressBook_throwsCommandException() {
        AddressBook emptyAddressBook = new AddressBook();
        Model model = new ModelManager(emptyAddressBook, new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON);

        try {
            deleteCommand.execute(model);
            fail("Expected CommandException to be thrown");
        } catch (CommandException e) {
            assertEquals(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, Messages.MESSAGE_INVALID_INDEX),
                    e.getMessage());
        }
    }

    /**
     * Tests that attempting to delete a pastry using an invalid index throws a {@link CommandException}.
     */
    @Test
    public void execute_deletePastryInvalidIndex_throwsCommandException() {
        AddressBook addressBook = TypicalAddressBook.getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());

        Index idx = Index.fromOneBased(model.getFilteredPastryList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_PASTRY.toString().trim(), idx);

        try {
            deleteCommand.execute(model);
            fail("Expected CommandException to be thrown");
        } catch (CommandException e) {
            assertEquals(String.format(Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX, Messages.MESSAGE_INVALID_INDEX),
                    e.getMessage());
        }
    }

    /**
     * Tests that attempting to delete a client using an invalid index throws a {@link CommandException}.
     */
    @Test
    public void execute_deletePersonInvalidIndex_throwsCommandException() {
        AddressBook addressBook = TypicalAddressBook.getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());

        Index idx = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), idx);

        try {
            deleteCommand.execute(model);
            fail("Expected CommandException to be thrown");
        } catch (CommandException e) {
            assertEquals(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, Messages.MESSAGE_INVALID_INDEX),
                    e.getMessage());
        }
    }

    /**
     * Tests that attempting to delete an order using an invalid index throws a {@link CommandException}.
     */
    @Test
    public void execute_deleteOrderInvalidIndex_throwsCommandException() {
        AddressBook addressBook = TypicalAddressBook.getTypicalAddressBook();
        Model model = new ModelManager(addressBook, new UserPrefs());

        Index idx = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(), idx);

        try {
            deleteCommand.execute(model);
            fail("Expected CommandException to be thrown");
        } catch (CommandException e) {
            assertEquals(String.format(Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX, Messages.MESSAGE_INVALID_INDEX),
                    e.getMessage());
        }
    }
}
