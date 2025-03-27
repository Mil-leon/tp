package powerbake.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.testutil.Assert.assertThrows;
import static powerbake.address.testutil.TypicalPastries.CROISSANT;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import powerbake.address.commons.core.GuiSettings;
import powerbake.address.logic.Messages;
import powerbake.address.logic.commands.exceptions.CommandException;
import powerbake.address.model.AddressBook;
import powerbake.address.model.Model;
import powerbake.address.model.ReadOnlyAddressBook;
import powerbake.address.model.ReadOnlyUserPrefs;
import powerbake.address.model.order.Order;
import powerbake.address.model.pastry.Pastry;
import powerbake.address.model.person.Person;
import powerbake.address.testutil.PastryBuilder;

public class AddPastryCommandTest {

    @Test
    public void constructor_nullPastry_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPastryCommand(null));
    }

    @Test
    public void execute_pastryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPastryAdded modelStub = new ModelStubAcceptingPastryAdded();
        Pastry validPastry = new PastryBuilder().build();

        CommandResult commandResult = new AddPastryCommand(validPastry).execute(modelStub);

        assertEquals(String.format(AddPastryCommand.MESSAGE_SUCCESS, Messages.format(validPastry)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPastry), modelStub.pastryAdded);
    }

    @Test
    public void execute_duplicatePastry_throwsCommandException() {
        Pastry validPastry = new PastryBuilder().build();
        AddCommand addCommand = new AddPastryCommand(validPastry);
        ModelStub modelStub = new ModelStubWithPastry(validPastry);

        assertThrows(CommandException.class,
                AddPastryCommand.MESSAGE_DUPLICATE_PASTRY, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Pastry croissant = new PastryBuilder().withName("Croissant").build();
        Pastry bagel = new PastryBuilder().withName("Bagel").build();
        AddCommand addCroissantCommand = new AddPastryCommand(croissant);
        AddCommand addBagelCommand = new AddPastryCommand(bagel);

        // same object -> returns true
        assertTrue(addCroissantCommand.equals(addCroissantCommand));

        // same values -> returns true
        AddCommand addCroissantCommandCopy = new AddPastryCommand(croissant);
        assertEquals(addCroissantCommand, addCroissantCommandCopy);

        // different types -> returns false
        assertFalse(addCroissantCommand.equals(1));

        // null -> returns false
        assertFalse(addCroissantCommand.equals(null));

        // different person -> returns false
        assertFalse(addCroissantCommand.equals(addBagelCommand));
    }

    @Test
    public void toStringMethod() {
        AddPastryCommand addCroissantCommand = new AddPastryCommand(CROISSANT);
        String expected = AddPastryCommand.class.getCanonicalName() + "{toAdd=" + CROISSANT + "}";
        assertEquals(expected, addCroissantCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPastry(Pastry pastry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPastry(Pastry pastry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePastry(Pastry target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPastry(Pastry target, Pastry editedPastry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Pastry> getFilteredPastryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Order> getFilteredOrderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPastryList(Predicate<Pastry> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredOrderList(Predicate<Order> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single pastry.
     */
    private class ModelStubWithPastry extends ModelStub {
        private final Pastry pastry;

        ModelStubWithPastry(Pastry pastry) {
            requireNonNull(pastry);
            this.pastry = pastry;
        }

        @Override
        public boolean hasPastry(Pastry pastry) {
            requireNonNull(pastry);
            return this.pastry.isSamePastry(pastry);
        }
    }

    /**
     * A Model stub that always accept the pastry being added.
     */
    private class ModelStubAcceptingPastryAdded extends ModelStub {
        final ArrayList<Pastry> pastryAdded = new ArrayList<>();

        @Override
        public boolean hasPastry(Pastry pastry) {
            requireNonNull(pastry);
            return pastryAdded.stream().anyMatch(pastry::isSamePastry);
        }

        @Override
        public void addPastry(Pastry pastry) {
            requireNonNull(pastry);
            pastryAdded.add(pastry);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
