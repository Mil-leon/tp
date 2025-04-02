package powerbake.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static powerbake.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static powerbake.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static powerbake.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static powerbake.address.testutil.TypicalPersons.CARL;
import static powerbake.address.testutil.TypicalPersons.ELLE;
import static powerbake.address.testutil.TypicalPersons.FIONA;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import powerbake.address.model.Model;
import powerbake.address.model.ModelManager;
import powerbake.address.model.UserPrefs;
import powerbake.address.model.person.NameContainsKeywordsPredicate;

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
        expectedModel.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
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
