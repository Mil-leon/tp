package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.logic.commands.CommandTestUtil.showOrderAtIndex;
import static powerbake.address.logic.commands.CommandTestUtil.showPastryAtIndex;
import static powerbake.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static powerbake.address.logic.commands.DeleteCommand.MESSAGE_USAGE;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.testutil.Assert.assertThrows;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_ORDER;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;
import powerbake.address.model.order.Order;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Test for successfully deleting the first person in an unfiltered list.
     */
    @Test
    public void execute_validClientIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for successfully deleting the first pastry in an unfiltered list.
     */
    @Test
    public void execute_validPastryIndexUnfilteredList_success() {
        Pastry pastryToDelete = model.getFilteredPastryList().get(INDEX_FIRST_PASTRY.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePastry(pastryToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for successfully deleting the first order in an unfiltered list.
     */
    @Test
    public void execute_validOrderIndexUnfilteredList_success() {
        Order orderToDelete = model.getFilteredOrderList().get(INDEX_FIRST_ORDER.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_ORDER);

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

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ORDER_SUCCESS,
                orderToDelete.getOrderId(),
                orderDate,
                orderToDelete.getCustomer().getName(),
                orderItemsString,
                price);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteOrder(orderToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for attempting to delete a client using an invalid index in an unfiltered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_invalidClientIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                Messages.MESSAGE_INVALID_INDEX + "\n" + MESSAGE_USAGE));
    }

    /**
     * Test for attempting to delete a pastry using an invalid index in an unfiltered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_invalidPastryIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPastryList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_PASTRY.toString().trim(), outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                Messages.MESSAGE_INVALID_INDEX + "\n" + MESSAGE_USAGE));
    }

    /**
     * Test for attempting to delete an order using an invalid index in an unfiltered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_invalidOrderIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredOrderList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(), outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                Messages.MESSAGE_INVALID_INDEX + "\n" + MESSAGE_USAGE));
    }

    /**
     * Test for successfully deleting a client from a filtered list.
     */
    @Test
    public void execute_validClientIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for successfully deleting a pastry from a filtered list.
     */
    @Test
    public void execute_validPastryIndexFilteredList_success() {
        showPastryAtIndex(model, INDEX_FIRST_PASTRY);

        Pastry pastryToDelete = model.getFilteredPastryList().get(INDEX_FIRST_PASTRY.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePastry(pastryToDelete);
        showNoPastry(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for successfully deleting an order from a filtered list.
     */
    @Test
    public void execute_validOrderIndexFilteredList_success() {
        showOrderAtIndex(model, INDEX_FIRST_ORDER);

        Order orderToDelete = model.getFilteredOrderList().get(INDEX_FIRST_ORDER.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_ORDER);

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

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_ORDER_SUCCESS,
                orderToDelete.getOrderId(),
                orderDate,
                orderToDelete.getCustomer().getName(),
                orderItemsString,
                price);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteOrder(orderToDelete);
        showNoOrder(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for attempting to delete a pastry from an empty filtered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_deleteFromEmptyFilteredPastryList_throwsCommandException() {
        showNoPastry(model);

        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    /**
     * Test for attempting to delete an order from an empty filtered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_deleteFromEmptyFilteredOrderList_throwsCommandException() {
        showNoOrder(model);

        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(), INDEX_FIRST_ORDER);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    /**
     * Test for attempting to delete a client from an empty filtered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_deleteFromEmptyFilteredClientList_throwsCommandException() {
        showNoPerson(model);

        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    /**
     * Tests that the {@code equals} method returns {@code true} when comparing
     * the same {@code DeleteCommand} object (self-comparison).
     */
    @Test
    public void equals_sameObject_true() {
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        assertTrue(deleteCommand.equals(deleteCommand)); // Same reference

        DeleteCommand deleteCommand1 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(1));
        assertTrue(deleteCommand.equals(deleteCommand)); // Same reference

        DeleteCommand deleteCommand2 = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(1));
        assertTrue(deleteCommand.equals(deleteCommand)); // Same reference
    }

    /**
     * Tests that the {@code equals} method returns {@code true} when two
     * {@code DeleteCommand} objects have identical attributes (same
     * {@code entityType} and {@code targetIndex}).
     */
    @Test
    public void equals_equalAttributes_true() {
        DeleteCommand deleteCommand1 = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteCommand2 = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        assertTrue(deleteCommand1.equals(deleteCommand2));

        DeleteCommand deleteCommand3 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteCommand4 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(1));
        assertTrue(deleteCommand3.equals(deleteCommand4));

        DeleteCommand deleteCommand5 = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteComman6 = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(1));
        assertTrue(deleteCommand5.equals(deleteCommand5));
    }

    /**
     * Tests that the {@code equals} method returns {@code false} when a
     * {@code DeleteCommand} object is compared with an object of a different type.
     */
    @Test
    public void equals_differentType_false() {
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        String otherType = "NotACommand";
        assertFalse(deleteCommand.equals(otherType));

        DeleteCommand deleteCommand1 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(1));
        assertFalse(deleteCommand1.equals(otherType));

        DeleteCommand deleteCommand2 = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(1));
        assertFalse(deleteCommand2.equals(otherType));
    }

    /**
     * Tests that the {@code equals} method returns {@code false} when comparing
     * two {@code DeleteCommand} objects with different {@code entityType} values.
     */
    @Test
    public void equals_differentEntityType_false() {
        DeleteCommand deleteClientCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deletePastryCommand = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(1));
        assertFalse(deleteClientCommand.equals(deletePastryCommand));

        DeleteCommand deleteOrderCommand = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(1));
        assertFalse(deleteOrderCommand.equals(deletePastryCommand));
        assertFalse(deleteOrderCommand.equals(deleteClientCommand));
    }

    /**
     * Tests that the {@code equals} method returns {@code false} when two
     * {@code DeleteCommand} objects have the same {@code entityType} but
     * different {@code targetIndex} values.
     */
    @Test
    public void equals_differentTargetIndex_false() {
        DeleteCommand deleteCommand1 = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteCommand2 = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(2));
        assertFalse(deleteCommand1.equals(deleteCommand2));

        DeleteCommand deleteCommand3 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteCommand4 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(2));
        assertFalse(deleteCommand3.equals(deleteCommand4));

        DeleteCommand deleteCommand5 = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteCommand6 = new DeleteCommand(PREFIX_ORDER.toString().trim(),
                Index.fromOneBased(2));
        assertFalse(deleteCommand5.equals(deleteCommand6));
    }

    /**
     * Tests that an invalid entity type (e.g., "invalidEntity").
     */
    @Test
    public void execute_invalidEntityType_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand("invalid ", INDEX_FIRST_ORDER);
        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    /**
     * Tests that the {@code equals} method returns {@code false} when comparing
     * two {@code DeleteCommand} objects with completely different attributes
     * (both {@code entityType} and {@code targetIndex} values are different).
     */
    @Test
    public void equals_differentAttributes_false() {
        DeleteCommand deleteCommand1 = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        DeleteCommand deleteCommand2 = new DeleteCommand(PREFIX_PASTRY.toString().trim(),
                Index.fromOneBased(2));
        assertFalse(deleteCommand1.equals(deleteCommand2));
    }

    /**
     * Tests that the {@code equals} method returns {@code false} when
     * a {@code DeleteCommand} object is compared with {@code null}.
     */
    @Test
    public void equals_nullComparison_false() {
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(),
                Index.fromOneBased(1));
        assertFalse(deleteCommand.equals(null));
    }

    /**
     * Tests the {@code toString()} method to verify the proper string
     * representation of the {@code DeleteCommand}.
     */
    @Test
    public void toString_validAttributes() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(PREFIX_CLIENT.toString().trim(), targetIndex);

        String expectedString = "powerbake.address.logic.commands.DeleteCommand{targetIndex=1}";
        assertEquals(expectedString, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    /**
     * Utility method to update the model's filtered list to show no pastries.
     */
    private void showNoPastry(Model model) {
        model.updateFilteredPastryList(p -> false); // Predicate always evaluates to false
        assertEquals(0, model.getFilteredPastryList().size());
    }

    /**
     * Utility method to update the model's filtered list to show no orders.
     */
    private void showNoOrder(Model model) {
        model.updateFilteredOrderList(p -> false); // Predicate always evaluates to false
        assertEquals(0, model.getFilteredOrderList().size());
    }
}
