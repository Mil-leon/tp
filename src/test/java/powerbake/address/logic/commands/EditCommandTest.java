package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.commands.CommandTestUtil.DESC_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.DESC_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_NAME_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static powerbake.address.logic.commands.CommandTestUtil.VALID_PRICE_CHOCOLATECROISSANT;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.logic.commands.CommandTestUtil.showPastryAtIndex;
import static powerbake.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static powerbake.address.logic.parser.CliSyntax.PREFIX_PASTRY;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static powerbake.address.testutil.TypicalIndexes.INDEX_SECOND_PASTRY;
import static powerbake.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import powerbake.address.commons.core.index.Index;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.EditCommand.EditPastryDescriptor;
import powerbake.address.logic.commands.EditCommand.EditPersonDescriptor;
import powerbake.address.model.AddressBook;
import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;
import powerbake.address.testutil.EditPastryDescriptorBuilder;
import powerbake.address.testutil.EditPersonDescriptorBuilder;
import powerbake.address.testutil.PastryBuilder;
import powerbake.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person originalPerson = model.getFilteredPersonList().get(0);
        Person editedPerson = new PersonBuilder(originalPerson)
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY).build();

        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(), Index.fromOneBased(1),
                descriptor, true);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(originalPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_pastryAllFieldsSpecifiedUnfilteredList_success() {
        Pastry originalPastry = model.getFilteredPastryList().get(0);
        Pastry editedPastry = new PastryBuilder(originalPastry)
                .withName(VALID_NAME_CHOCOLATECROISSANT).withPrice(VALID_PRICE_CHOCOLATECROISSANT).build();

        EditPastryDescriptor descriptor1 = new EditPastryDescriptorBuilder().withName(VALID_NAME_CHOCOLATECROISSANT)
                .withPrice(VALID_PRICE_CHOCOLATECROISSANT).build();

        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(), Index.fromOneBased(1),
                descriptor1, false);

        String expectedMessage1 = String.format(EditCommand.MESSAGE_EDIT_PASTRY_SUCCESS,
                Messages.format(editedPastry));

        Model expectedModel1 = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel1.setPastry(originalPastry, editedPastry);

        assertCommandSuccess(editCommand1, model, expectedMessage1, expectedModel1);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Person originalPerson = model.getFilteredPersonList().get(0);
        Person editedPerson = new PersonBuilder(originalPerson).withPhone(VALID_PHONE_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(), Index.fromOneBased(1),
                descriptor, true);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(originalPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedPastryUnfilteredList_success() {
        Pastry originalPastry = model.getFilteredPastryList().get(0);
        Pastry editedPastry = new PastryBuilder(originalPastry).withPrice(VALID_PRICE_CHOCOLATECROISSANT).build();

        EditPastryDescriptor descriptor = new EditPastryDescriptorBuilder().withPrice(VALID_PRICE_CHOCOLATECROISSANT)
                .build();

        EditCommand editCommand = new EditCommand(PREFIX_PASTRY.toString().trim(), Index.fromOneBased(1),
                descriptor, false);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PASTRY_SUCCESS,
                Messages.format(editedPastry));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPastry(originalPastry, editedPastry);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                INDEX_FIRST_PERSON, new EditPersonDescriptor(), true);
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedPastryUnfilteredList_success() {
        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(),
                INDEX_FIRST_PASTRY, new EditPastryDescriptor(), false);

        Pastry editedPastry = model.getFilteredPastryList().get(INDEX_FIRST_PASTRY.getZeroBased());

        String expectedMessage1 = String.format(EditCommand.MESSAGE_EDIT_PASTRY_SUCCESS, Messages.format(editedPastry));

        Model expectedModel1 = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand1, model, expectedMessage1, expectedModel1);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build(), true);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredListPastry_success() {
        showPastryAtIndex(model, INDEX_FIRST_PASTRY);

        Pastry pastryInFilteredList = model.getFilteredPastryList().get(INDEX_FIRST_PASTRY.getZeroBased());
        Pastry editedPastry = new PastryBuilder(pastryInFilteredList).withName(VALID_NAME_CHOCOLATECROISSANT).build();
        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY,
                new EditPastryDescriptorBuilder().withName(VALID_NAME_CHOCOLATECROISSANT).build(), false);

        String expectedMessage1 = String.format(EditCommand.MESSAGE_EDIT_PASTRY_SUCCESS, Messages.format(editedPastry));

        Model expectedModel1 = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel1.setPastry(model.getFilteredPastryList().get(0), editedPastry);

        assertCommandSuccess(editCommand1, model, expectedMessage1, expectedModel1);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                INDEX_SECOND_PERSON, descriptor, true);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_duplicatePastryUnfilteredList_failure() {
        Pastry firstPastry = model.getFilteredPastryList().get(INDEX_FIRST_PASTRY.getZeroBased());
        EditPastryDescriptor descriptor1 = new EditPastryDescriptorBuilder(firstPastry).build();
        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(),
                INDEX_SECOND_PASTRY, descriptor1, false);

        assertCommandFailure(editCommand1, model, EditCommand.MESSAGE_DUPLICATE_PASTRY);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(), INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build(), true);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_duplicatePastryFilteredList_failure() {
        showPastryAtIndex(model, INDEX_FIRST_PASTRY);

        Pastry pastryInList = model.getAddressBook().getPastryList().get(INDEX_SECOND_PASTRY.getZeroBased());
        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(), INDEX_FIRST_PASTRY,
                new EditPastryDescriptorBuilder(pastryInList).build(), false);

        assertCommandFailure(editCommand1, model, EditCommand.MESSAGE_DUPLICATE_PASTRY);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                outOfBoundIndex, descriptor, true);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPastryIndexUnfilteredList_failure() {
        Index outOfBoundPastry = Index.fromOneBased(model.getFilteredPastryList().size() + 1);
        EditPastryDescriptor descriptor1 = new EditPastryDescriptorBuilder().withName(VALID_NAME_CHOCOLATECROISSANT)
                .build();
        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(),
                outOfBoundPastry, descriptor1, false);

        assertCommandFailure(editCommand1, model, Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonPastryIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(PREFIX_CLIENT.toString().trim(), outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build(), true);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        showPastryAtIndex(model, INDEX_FIRST_PASTRY);
        Index outOfBoundIndex1 = INDEX_SECOND_PASTRY;

        assertTrue(outOfBoundIndex1.getZeroBased() < model.getAddressBook().getPastryList().size());

        EditCommand editCommand1 = new EditCommand(PREFIX_PASTRY.toString().trim(), outOfBoundIndex1,
                new EditPastryDescriptorBuilder().withName(VALID_NAME_CHOCOLATECROISSANT).build(), false);

        assertCommandFailure(editCommand1, model, Messages.MESSAGE_INVALID_PASTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(PREFIX_CLIENT.toString().trim(),
                INDEX_FIRST_PERSON, DESC_AMY, true);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(PREFIX_CLIENT.toString().trim(),
                INDEX_FIRST_PERSON, copyDescriptor, true);

        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(PREFIX_CLIENT.toString().trim(),
                INDEX_SECOND_PERSON, DESC_AMY, true)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(PREFIX_CLIENT.toString().trim(),
                INDEX_FIRST_PERSON, DESC_BOB, true)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand("client", index, editPersonDescriptor, true);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
