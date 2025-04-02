package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.Messages.MESSAGE_ORDERS_LISTED_OVERVIEW;
import static powerbake.address.logic.Messages.MESSAGE_PASTRIES_LISTED_OVERVIEW;
import static powerbake.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalOrders.ORDER_ONE;
import static powerbake.address.testutil.TypicalOrders.ORDER_THREE;
import static powerbake.address.testutil.TypicalOrders.ORDER_TWO;
import static powerbake.address.testutil.TypicalPastries.CROISSANT;
import static powerbake.address.testutil.TypicalPersons.CARL;
import static powerbake.address.testutil.TypicalPersons.ELLE;
import static powerbake.address.testutil.TypicalPersons.FIONA;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        String commandWord1 = "client";
        String commandWord2 = "pastry";
        String commandWord3 = "order";
        String[] firstKeywords = {"Alice", "Bob"};
        String[] secondKeywords = {"Croissant", "Bread"};
        String[] thirdKeywords = {"Eve", "Frank"};

        FindCommand findFirstCommand = new FindCommand(commandWord1, firstKeywords);
        FindCommand findSecondCommand = new FindCommand(commandWord2, secondKeywords);
        FindCommand findThirdCommand = new FindCommand(commandWord3, thirdKeywords);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(commandWord1, firstKeywords);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
        assertFalse(findFirstCommand.equals(findThirdCommand));
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        String expectedCommandWord = "client";
        String[] keywords = {"Kurz", "Elle", "Kunz"};
        FindCommand command = new FindCommand(expectedCommandWord, keywords);
        expectedModel.updateFilteredPersonList(
                new powerbake.address.model.person.NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePastriesFound() {
        String expectedMessage = String.format(MESSAGE_PASTRIES_LISTED_OVERVIEW, 1);
        String expectedCommandWord = "pastry";
        String[] keywords = {"Croissant", "Roll"};
        FindCommand command = new FindCommand(expectedCommandWord, keywords);
        expectedModel.updateFilteredPastryList(
                new powerbake.address.model.pastry.NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CROISSANT), model.getFilteredPastryList());
    }

    @Test
    public void execute_multipleKeywords_multipleOrdersFound() {
        String expectedMessage = String.format(MESSAGE_ORDERS_LISTED_OVERVIEW, 3);
        String expectedCommandWord = "order";
        String[] keywords = {"alice", "Carl"};
        FindCommand command = new FindCommand(expectedCommandWord, keywords);
        expectedModel.updateFilteredOrderList(
                new powerbake.address.model.order.NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ORDER_ONE, ORDER_TWO, ORDER_THREE), model.getFilteredOrderList());
    }

    @Test
    public void toStringMethod() {
        String commandWord = "client";
        String[] keywords = {"keyword"};
        FindCommand findCommand = new FindCommand(commandWord, keywords);
        String expected = FindCommand.class.getCanonicalName()
                + "{commandWord=" + commandWord
                + ", nameKeywords=" + Arrays.toString(keywords) + "}";
        assertEquals(expected, findCommand.toString());
    }
}
