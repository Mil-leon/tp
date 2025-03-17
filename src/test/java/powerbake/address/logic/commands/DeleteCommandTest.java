package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.logic.commands.CommandTestUtil.showPastryAtIndex;
import static powerbake.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static powerbake.address.testutil.Assert.assertThrows;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;
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
        DeleteCommand deleteCommand = new DeleteCommand("client", INDEX_FIRST_PERSON);

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
        DeleteCommand deleteCommand = new DeleteCommand("pastry", INDEX_FIRST_PASTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePastry(pastryToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for attempting to delete a client using an invalid index in an unfiltered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_invalidClientIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand("client", outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Test for attempting to delete a pastry using an invalid index in an unfiltered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_invalidPastryIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPastryList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand("pastry", outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX);
    }

    /**
     * Test for successfully deleting a client from a filtered list.
     */
    @Test
    public void execute_validClientIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand("client", INDEX_FIRST_PERSON);

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
        DeleteCommand deleteCommand = new DeleteCommand("pastry", INDEX_FIRST_PASTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePastry(pastryToDelete);
        showNoPastry(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test for attempting to delete a pastry from an empty filtered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_deleteFromEmptyFilteredPastryList_throwsCommandException() {
        showNoPastry(model);

        DeleteCommand deleteCommand = new DeleteCommand("pastry", INDEX_FIRST_PASTRY);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    /**
     * Test for attempting to delete a client from an empty filtered list.
     * Expected: {@link CommandException}.
     */
    @Test
    public void execute_deleteFromEmptyFilteredClientList_throwsCommandException() {
        showNoPerson(model);

        DeleteCommand deleteCommand = new DeleteCommand("client", INDEX_FIRST_PERSON);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
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
}
