package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.logic.commands.CommandTestUtil.showNoPastry;
import static powerbake.address.logic.commands.CommandTestUtil.showPastryAtIndex;
import static powerbake.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.Messages;
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

    // Test deleting valid client from an unfiltered list
    @Test
    public void execute_validClientIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand("client", INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    // Test deleting valid pastry from an unfiltered list
    @Test
    public void execute_validPastryIndexUnfilteredList_success() {
        Pastry pastryToDelete = model.getFilteredPastryList().get(INDEX_FIRST_PASTRY.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand("pastry", INDEX_FIRST_PASTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PASTRY_SUCCESS, pastryToDelete.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePastry(pastryToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    // Test invalid index for clients
    @Test
    public void execute_invalidClientIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand("client", outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // Test invalid index for pastries
    @Test
    public void execute_invalidPastryIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredPastryList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand("pastry", outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX);
    }

    // Test deleting client from filtered list
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

    // Test deleting pastry from filtered list
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
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
